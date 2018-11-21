package ar.edu.itba.pawgram.webapp.dto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ExceptionDTO {

    private String message;
    private List<FieldViolationDTO> errors;

    public ExceptionDTO() {}

    public ExceptionDTO(final String message) {
        this.setMessage(message);
    }

    public ExceptionDTO(final String message, final Set<? extends ConstraintViolation<?>> constraintViolations) {
        this.setMessage(message);
        errors = new ArrayList<FieldViolationDTO>(constraintViolations.size());

        constraintViolations.forEach((constraintViolation) -> {
            if (!constraintViolation.getPropertyPath().toString().isEmpty())
                errors.add(new FieldViolationDTO(constraintViolation));
        });
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<FieldViolationDTO> getErrors() {
        return errors;
    }

    public void setErrors(final List<FieldViolationDTO> errors) {
        this.errors = errors;
    }
}
