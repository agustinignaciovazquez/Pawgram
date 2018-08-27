package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

public class RegisterController {
    @Autowired
    private SecurityUserService securityUserService;

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final UserForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register/process", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        final User user = securityUserService.registerUser(form.getName(),form.getSurname(),form.getMail(),form.getPasswordForm().getPassword());

        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getMail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }
}
