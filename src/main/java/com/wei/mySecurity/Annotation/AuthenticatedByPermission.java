package com.wei.mySecurity.Annotation;



import java.lang.annotation.*;


@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticatedByPermission{
    String[] value() default "";
    String operationValue() default "";
}
