package ar.edu.itba.pawgram.webapp.form.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = MultipartFileSizeValidator.class)
@Documented
public @interface FileSize {

    String message() default "{ar.edu.itba.pawgram.webapp.form.constraints.FileSize.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    int min() default 0;
    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;

    @Target({ FIELD, METHOD, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FileSize[] value();
    }
}