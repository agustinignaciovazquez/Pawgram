package ar.edu.itba.pawgram.webapp.validator;

import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.controller.RecoverController;
import ar.edu.itba.pawgram.webapp.form.ChangePasswordForm;
import ar.edu.itba.pawgram.webapp.form.RecoverForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TokenValidator implements Validator {
    @Autowired
    private UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return RecoverController.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RecoverForm form = (RecoverForm) target;
        User user = userService.findByMail(form.getMail());
        if( user == null){
            errors.rejectValue("mail", "NotRegisteredMail");
            return;
        }
        if(user != null) {
            if (!form.getCurrentToken().equals(userService.getResetToken(user))) {
                errors.rejectValue("currentToken", "TokenMatch");
            }
        }

    }

}
