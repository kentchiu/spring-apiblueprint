package com.kentchiu.spring.attribute;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Date;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD})
public @interface AttributeInfo {

    String path() default "";

    Type type() default Type.UNKNOWN;

    boolean required() default false;

    String description() default "";

    String format() default "";

    boolean ignore() default false;

    String defaultValue() default "";

    enum Type {
        UNKNOWN, ARRAY, CHARACTER, NUMBER, STRING, BOOLEAN, OBJECT, DATE, ENUM;


        public static AttributeInfo.Type valueOf(Class<?> type) {
            AttributeInfo.Type result;
            if (Enum.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.ENUM;
            } else if (Number.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.NUMBER;
            } else if (Character.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.CHARACTER;
            } else if (Boolean.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.BOOLEAN;
            } else if (Date.class.isAssignableFrom(type)) {
                result = Type.DATE;
            } else if (String.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.STRING;
            } else if (type.isArray() || Collection.class.isAssignableFrom(type)) {
                result = AttributeInfo.Type.ARRAY;
            } else {
                result = AttributeInfo.Type.OBJECT;
            }
            return result;
        }
    }


}