package com.darkona.toolset.logging;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.*;

@Target({METHOD})
@Retention(RUNTIME)
public @interface Logged {

    boolean onCall() default true;

    boolean args() default true;

    Values argValues() default Values.ALL;

    boolean onReturn() default true;

    Values returnValue() default Values.ALL;

    boolean onException() default true;

    boolean time() default true;

    String callMsg() default "";

    String returnMsg() default "";

    String exceptionMsg() default "";

    String level() default "INFO";

    String exceptionLevel() default "ERROR";

    enum Values {
        NONE, ALL, NULL
    }


}
