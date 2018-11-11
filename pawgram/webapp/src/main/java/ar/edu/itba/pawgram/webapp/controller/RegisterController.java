package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.interfaces.service.EmailService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@RequestMapping("/register")
@Controller
public class RegisterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private EmailService ms;
    @Autowired
    private MessageSource messageSource;

    @ModelAttribute("registerForm")
    public UserForm registerForm() {
        LOGGER.debug("Accessed register");
        return new UserForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/process", method = { RequestMethod.POST })
    public ModelAndView create(@ModelAttribute("registerForm") @Valid final UserForm registerUserForm,
                               final BindingResult errors, final RedirectAttributes attr, final Locale locale) {
        LOGGER.debug("Accessed register POST");
        if (errors.hasErrors()) {
            LOGGER.warn("Failed to register user: form has error: {}", errors.getAllErrors());
            return errorState(registerUserForm, errors, attr);
        }

        final User user;
        try {
            user = securityUserService.registerUser(registerUserForm.getName(),registerUserForm.getSurname(),
                    registerUserForm.getMail(),registerUserForm.getPasswordForm().getPassword(),null);
        } catch (DuplicateEmailException e) {
            LOGGER.warn("Failed to register user: duplicate email {}", e.getMessage());
            errors.rejectValue("mail", "DuplicateEmail");
            return errorState(registerUserForm, errors, attr);
        }

        LOGGER.info("New user with id {} registered", user.getId());
        try {
            String [] args = {user.getName() + " " +user.getSurname()};
            ms.sendWelcomeEmail(user,messageSource.getMessage("welcomeMailMessage",args,locale),
                    messageSource.getMessage("welcomeMailSubject",null,locale));
        } catch (SendMailException e) {
            LOGGER.warn("Could not send welcome email registered \n Stacktrace {}", e.getMessage());
        }
        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }

    private ModelAndView errorState(final UserForm registerUserForm, final BindingResult errors, final RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.registerForm", errors);
        attr.addFlashAttribute("registerForm", registerUserForm);
        return new ModelAndView("redirect:/register");
    }
}
