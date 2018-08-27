package ar.edu.itba.pawgram.webapp.controller.advice;

import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @Autowired
    private SecurityUserService securityUserService;

    @ModelAttribute("loggedUser")
    public User loggedUser() {
        return securityUserService.getLoggedInUser();
    }

}
