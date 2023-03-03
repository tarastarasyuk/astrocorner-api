package com.itzroma.astrocornerapi.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface RateLimiter {

    @AliasFor("value")
    int permits() default 1;

    @AliasFor("permits")
    int value() default 1;

    String key() default "";

}
