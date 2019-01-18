package validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import play.data.validation.Constraints.RequiredValidator;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = RequiredAndNotEqualsValidator.class)
@play.data.Form.Display(name="constraint.required")
public @interface RequiredAndNotEquals {
    String message() default RequiredValidator.message;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String [] notEquals() default {};
}
