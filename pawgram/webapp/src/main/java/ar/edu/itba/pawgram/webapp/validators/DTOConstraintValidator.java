package ar.edu.itba.pawgram.webapp.validators;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DTOConstraintValidator {

    @Autowired
    private Validator validator;

    public <T> void validate(T dto, String message, Class<?>... groups) throws DTOValidationException {
        final Set<ConstraintViolation<T>> constraintViolations = validator.validate(dto, groups);

        if (!constraintViolations.isEmpty())
            throw new DTOValidationException(message, constraintViolations);
    }
}
