package ar.edu.itba.pawgram.webapp.validator;

import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.form.ForgetForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import org.springframework.validation.Validator;

@Component
public class MailValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return MailValidator.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ForgetForm form = (ForgetForm) target;
        User user = userService.findByMail(form.getMail());
        if( user == null){
            errors.rejectValue("mail", "NotRegisteredMail");
        }

    }

}
