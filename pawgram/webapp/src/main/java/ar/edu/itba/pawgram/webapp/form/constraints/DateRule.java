package ar.edu.itba.pawgram.webapp.form.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
//https://stackoverflow.com/questions/40482252/validation-of-a-date-with-hibernate
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateRuleValidator.class)
@Documented
public @interface DateRule {
    String message() default "{ar.edu.itba.pawgram.webapp.form.constraints.DateRule.message}}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}