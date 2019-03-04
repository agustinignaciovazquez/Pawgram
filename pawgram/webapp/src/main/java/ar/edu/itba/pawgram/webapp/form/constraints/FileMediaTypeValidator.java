package ar.edu.itba.pawgram.webapp.form.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

public class FileMediaTypeValidator implements ConstraintValidator<FileMediaType, FormDataBodyPart> {

    private String[] values;

    public void initialize(FileMediaType constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    public boolean isValid(FormDataBodyPart value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        String contentType = value.getMediaType().toString();

        for (String mediaType : values) {
            if (mediaType.equals(contentType))
                return true;
        }

        return false;
    }


}
