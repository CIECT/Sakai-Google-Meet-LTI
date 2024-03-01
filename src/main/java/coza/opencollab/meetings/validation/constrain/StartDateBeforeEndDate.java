package coza.opencollab.meetings.validation.constrain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import coza.opencollab.meetings.validation.validator.StartBeforeEndValidator;

@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StartDateBeforeEndDate {

    String message() default "Start date must be before end date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
