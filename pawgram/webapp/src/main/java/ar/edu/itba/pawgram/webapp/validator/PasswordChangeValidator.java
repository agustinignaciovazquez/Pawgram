package ar.edu.itba.pawgram.webapp.validator;

import ar.edu.itba.pawgram.webapp.form.ChangePasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import org.springframework.validation.Validator;

@Component
public class PasswordChangeValidator implements Validator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangePasswordForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordForm form = (ChangePasswordForm) target;

        if (!passwordEncoder.matches(form.getCurrentPasswordConf(), form.getCurrentPassword()))
            errors.rejectValue("currentPasswordConf", "CurrentPasswordMatch");
    }

}
