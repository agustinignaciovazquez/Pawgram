package ar.edu.itba.pawgram.webapp.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;

@SuppressWarnings("serial")
public class DTOValidationException extends Exception {

    private final Set<? extends ConstraintViolation<?>> constraintViolations;

    public DTOValidationException(final String message, final Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message);
        this.constraintViolations = constraintViolations;
    }

    public Set<? extends ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

}
