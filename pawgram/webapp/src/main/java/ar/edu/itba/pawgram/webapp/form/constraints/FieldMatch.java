package ar.edu.itba.pawgram.webapp.form.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

// src: http://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303


/**
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.<p>
 * Error message will be associated to the second field supplied.
 *
 * Example, compare 1 pair of fields:
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
 *
 * Example, compare more than 1 pair of fields:
 * @FieldMatch.List({
 *   @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 *   @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")})
 */
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
public @interface FieldMatch {

    String message() default "{ar.edu.itba.pawgram.webapp.form.constraints.FieldMatch.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /**
     * @return The first field
     */
    String first();
    /**
     * @return The second field
     */
    String second();
    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        FieldMatch[] value();
    }
}