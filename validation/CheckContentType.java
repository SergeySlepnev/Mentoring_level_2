package com.spdev.validation;

import com.spdev.validation.impl.ContentTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContentTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckContentType {

    String message() default "{error.invalid_content_type}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
