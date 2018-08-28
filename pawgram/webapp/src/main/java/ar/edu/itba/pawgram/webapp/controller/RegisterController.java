package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/register")
@Controller
public class RegisterController {
    @Autowired
    private SecurityUserService securityUserService;

    @ModelAttribute("registerForm")
    public UserForm registerForm() {
        return new UserForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView register() {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/process", method = { RequestMethod.POST })
    public ModelAndView create(@ModelAttribute("registerForm") @Valid final UserForm registerUserForm,
                               final BindingResult errors, final RedirectAttributes attr) {
        if (errors.hasErrors()) {
            return errorState(registerUserForm, errors, attr);
        }
        final User user;
        try {
            user = securityUserService.registerUser(registerUserForm.getName(),registerUserForm.getSurname(),registerUserForm.getMail(),registerUserForm.getPasswordForm().getPassword(),null);
        } catch (DuplicateEmailException e) {
            errors.rejectValue("email", "DuplicateEmail");
            return errorState(registerUserForm, errors, attr);
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
