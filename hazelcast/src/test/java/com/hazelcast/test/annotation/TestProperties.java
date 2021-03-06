package com.hazelcast.test.annotation;

import com.hazelcast.test.MaxThreadsAware;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestProperties {
    Class<? extends MaxThreadsAware> maxThreadsCalculatorClass();
}