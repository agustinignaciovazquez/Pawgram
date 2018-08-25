package ar.edu.itba.pawgram.webapp.form.constraints;


import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipartFileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {

    private int min;
    private int max;

    public void initialize(FileSize constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null)
            return true;

        long size = value.getSize();
        return size >= min && size <= max;
    }

}
