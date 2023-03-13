package com.company;

import java.lang.annotation.*;

@Repeatable(MyAnnos.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnno {
    String value() default "hi";
    String name() default "0";
}


@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnos {
    MyAnno[] value();
}