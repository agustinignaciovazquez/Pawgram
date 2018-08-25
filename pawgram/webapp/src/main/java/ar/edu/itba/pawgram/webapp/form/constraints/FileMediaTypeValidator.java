package ar.edu.itba.pawgram.webapp.form.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileMediaTypeValidator implements ConstraintValidator<FileMediaType, MultipartFile> {

    private String[] values;

    public void initialize(FileMediaType constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return true;

        String contentType = value.getContentType();

        for(String mediaType : values){
            if(mediaType.equals(contentType))
                return true;
        }

        return false;
    }


}
