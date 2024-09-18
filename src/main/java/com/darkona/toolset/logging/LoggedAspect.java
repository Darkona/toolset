package com.darkona.toolset.logging;


import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.darkona.toolset.logging.Logged.Values;


@SuppressWarnings("ConstantValue")
@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggedAspect {

    private static final String METHOD_NAME = "m";
    private static final String METHOD_TYPE = "t";
    private static final String CLASS_NAME = "c";
    private static final String RETURN_VALUE = "rV";
    private static final String DURATION = "d";
    private static final String EXCEPTION_CLASS = "ex";
    private static final String EXCEPTION_MESSAGE = "exM";
    private static final String EXCEPTION_ORIGIN_CLASS = "exC";
    private static final String EXCEPTION_ORIGIN_METHOD = "exM";
    private static final String RETURN_CLASS = "rC";
    private static final String LINE = "L";
    private static final String NULL = "null";

    private static final String MONOCHROME_MESSAGE = "%s::%s";
    private static final String INFO_MESSAGE = LogColor.GREEN.get() + "%s::%s";
    private static final String DEBUG_MESSAGE = "%s::%s";
    private static final String WARN_MESSAGE = "%s::%s";
    private static final String ERROR_MESSAGE = "%s::%s";

    @PostConstruct
    void init() {
        System.out.println(LogStrings.custom(new Color(156, 123, 23), "@Logged initialized."));
    }

    @Around("@annotation(options)")
    public Object logMethod(ProceedingJoinPoint pjp, Logged options)
    throws Throwable {
        Data data = assembleData(pjp);
        Level level = getLoggingLevel(options.level(), Level.INFO);

        Logger log = LoggerFactory.getLogger(pjp.getSignature().getDeclaringType());
        logCall(log, level, data, options);

        try {

            var o = pjp.proceed();
            data.map.put(DURATION, String.valueOf(System.currentTimeMillis() - data.start));
            data.map.put(RETURN_CLASS, (o == null) ? NULL : o.getClass().getSimpleName());
            data.map.put(RETURN_VALUE, objectString(o));

            logExit(log, level, data, options);

            return o;

        } catch (Throwable e) {

            data.map.put(DURATION, String.valueOf(System.currentTimeMillis()- data.start));

            logException(log, e, data, options);

            throw e;
        }
    }


    private void logCall(Logger log, Level level, Data data, Logged options) {
        String message;

        if (options.callMsg() != null && !options.callMsg().isEmpty()) {

            message = applyPattern(data, options.callMsg());
            log.atLevel(level).log(message);

        } else if (options.onCall()) {
            StringBuilder sb = new StringBuilder(String.format(MONOCHROME_MESSAGE, data.map.get(CLASS_NAME), data.map.get(METHOD_NAME)))
                    .append(" called ");
            if (options.args()) {
                sb.append("with args: [")
                  .append(makePrintableArgs(data.args, options.argValues()))
                  .append("]");
            }
            message = sb.toString();
            log.atLevel(level).log(message);
        }
    }

    private void logExit(Logger log, Level level, Data data, Logged options) {
        if (options.onReturn() && options.returnMsg().isEmpty()) {
            StringBuilder sb = new StringBuilder(String.format(MONOCHROME_MESSAGE, data.map.get(CLASS_NAME), data.map.get(METHOD_NAME)))
                    .append(" returned");

            if (options.returnValue().equals(Values.ALL)) {
                sb
                        .append(" with value: ")
                        .append(data.map.get(RETURN_VALUE));
            } else if (options.returnValue().equals(Values.NULL) && data.map.get(RETURN_VALUE).equals("null")) {
                sb
                        .append(" with value: null");
            }
            if (options.time()) {
                sb
                        .append(String.format(" Time taken: %s%s", data.map.get(DURATION), "ms"));
            }
            log.atLevel(level).log(sb.toString());
        } else if (!options.returnMsg().isEmpty()) {
            log.atLevel(level).log(applyPattern(data, options.returnMsg()));
        }
    }

    private void logException(Logger log, Throwable e, Data data, Logged options) {
        var origin = e.getStackTrace()[0];

        if (options.onException() && options.exceptionMsg().isEmpty()) {
            StringBuilder sb = new StringBuilder(String.format(MONOCHROME_MESSAGE, data.map.get(CLASS_NAME), data.map.get(METHOD_NAME)));

            var returnMsg = String.format(" -> threw an exception:\n %s: %s \n\tat %s.%s (%s:%d)",
                    e.getClass().getName(),
                    e.getLocalizedMessage(),
                    origin.getClassName(),
                    origin.getMethodName(),
                    origin.getFileName(),
                    origin.getLineNumber()
            );

            sb.append(returnMsg);

            if (options.time()) {
                sb.append(String.format(" Time taken: %s%s", data.map.get(DURATION), "ms"));
            }

            log.atLevel(getLoggingLevel(options.exceptionLevel(), Level.ERROR)).log(sb.toString());

        } else if (!options.exceptionMsg().isEmpty()) {

            data.map.put(EXCEPTION_CLASS, e.getClass().getName());
            data.map.put(EXCEPTION_MESSAGE, e.getLocalizedMessage());
            data.map.put(EXCEPTION_ORIGIN_CLASS, origin.getClassName());
            data.map.put(EXCEPTION_ORIGIN_METHOD, origin.getMethodName());
            data.map.put(LINE, String.valueOf(origin.getLineNumber()));


            log.atLevel(getLoggingLevel(options.exceptionLevel(), Level.ERROR)).log(applyPattern(data, options.exceptionMsg()));
        }
    }

    private Data assembleData(ProceedingJoinPoint pjp) {

        var start = System.currentTimeMillis();

        Map<String, String> map = new HashMap<>();

        map.put(CLASS_NAME, pjp.getSignature().getDeclaringType().getSimpleName());
        map.put(METHOD_NAME, pjp.getSignature().getName());
        map.put(METHOD_TYPE, pjp.getSignature().toLongString());

        var signature = (MethodSignature) pjp.getSignature();
        Arg[] args = new Arg[signature.getParameterNames().length];
        for (int i = 0; i < signature.getParameterNames().length; i++) {
            args[i] = new Arg(signature.getParameterTypes()[i].getSimpleName(), signature.getParameterNames()[i], objectString(pjp.getArgs()[i]));
        }

        return new Data(map, args, start);
    }

    private Level getLoggingLevel(String level, Level defaultLevel) {
        try {
            return Level.valueOf(level);
        } catch (Exception ignored) {
            return defaultLevel;
        }
    }


    private String applyPattern(Data data, String s) {

        String[] tokenKeys = {"%c", "%t", "%m", "%d", "%r", "%f", "%x", "%p", "%j", "%y", "%l"};
        String[] tokenValues = {
                data.map.get(CLASS_NAME),
                data.map.get(METHOD_TYPE),
                data.map.get(METHOD_NAME),
                data.map.get(DURATION),
                data.map.get(RETURN_VALUE),
                data.map.get(RETURN_CLASS),
                objectString(data.map.get(EXCEPTION_CLASS)),
                objectString(data.map.get(EXCEPTION_MESSAGE)),
                objectString(data.map.get(EXCEPTION_ORIGIN_CLASS)),
                objectString(data.map.get(EXCEPTION_ORIGIN_METHOD)),
                objectString(data.map.get(LINE))
        };

        var initialLength = 3 * data.args.length + tokenKeys.length;

        String[] keys = new String[initialLength];
        String[] vals = new String[initialLength];
        int i = 0;
        for (;i < data.args.length; i++) {

            keys[i * 3] = "%k[" + i + "]";
            keys[i * 3 + 1] = "%n[" + i + "]";
            keys[i * 3 + 2] = "%a[" + i + "]";

            vals[i * 3] = data.args[i].className;
            vals[i * 3 + 1] = data.args[i].name;
            vals[i * 3 + 2] = data.args[i].value;

            if(i * 3 + 2 == initialLength - tokenKeys.length - 1){
                break;
            }
        }
        i += 3;

        for(;i<initialLength; i++){
            keys[i] = tokenKeys[i];
            vals[i] = tokenValues[i];
        }

        return StringUtils.replaceEach(s, keys, vals);
    }

    String objectString(Object o) {
        return o == null ? "null" : o.toString();
    }

    private String makePrintableArgs(Arg[] args, Values argValues) {
        return Arrays.stream(args)
                     .map(a -> a.toString(argValues))
                     .collect(Collectors.joining(","));
    }

    private record Data(Map<String, String> map, Arg[] args, Long start) {}

    private record Arg(String className, String name, String value) {

        public String toString(Values values) {
            return String.format("(%s) \"%s\"%s", className, name,
                    values == Values.ALL ? "= " + value :
                    values == Values.NULL ? "= " + NULL : "");
        }

    }
}
