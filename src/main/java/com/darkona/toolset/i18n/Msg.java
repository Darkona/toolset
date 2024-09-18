package com.darkona.toolset.i18n;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Msg {

    public static final String SPRING_MESSAGES_BASENAME = "spring.messages.basename";
    public static final String DEFAULT_FILENAME = "classpath:/i18n/messages";
    private static Environment ENV;
    private static ReloadableResourceBundleMessageSource messageSource;
    private static ReloadableResourceBundleMessageSource errorSource;
    private final ApplicationContext context;

    Msg(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    private void init() {
        ENV = context.getEnvironment();
        initialize();
    }

    public static void initialize(){
        if (messageSource == null) {
            messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename(getMsgBaseName());
            messageSource.setDefaultEncoding("UTF-8");
        }

        if (errorSource == null) {
            errorSource = new ReloadableResourceBundleMessageSource();
            errorSource.setBasename("classpath:/i18n/errors");
            errorSource.setDefaultEncoding("UTF-8");
        }

    }


    private static String getMsgBaseName(){

        return  (ENV != null) ? ENV.getProperty(SPRING_MESSAGES_BASENAME, DEFAULT_FILENAME) : DEFAULT_FILENAME;
    }

    private static ReloadableResourceBundleMessageSource getMessageSource() {
        initialize();
        return messageSource;
    }

    private static ReloadableResourceBundleMessageSource getErrorSource() {
        initialize();
        return errorSource;
    }

    public static String get(@NotNull String key){
        return get(key, key);
    }

    public static String get(@NotNull String key, String defaultMessage){
        return getMessageSource().getMessage(key, null, defaultMessage, null);
    }


    public static String format(@NotNull String key, Object... args){
        return String.format(get(key), args);
    }

    public static String getError(@NotNull String key){
        return getError(key, key);
    }

    public static String getError(@NotNull String key, String defaultMessage){
        return getErrorSource().getMessage(key, null, defaultMessage, null);
    }


}
