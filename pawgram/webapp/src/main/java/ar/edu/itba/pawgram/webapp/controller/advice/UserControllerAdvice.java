package ar.edu.itba.pawgram.webapp.controller.advice;

import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class UserControllerAdvice {

    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private NotificationService notificationService;

    @ModelAttribute("loggedUser")
    public User loggedUser() {
        return securityUserService.getLoggedInUser();
    }

    @ModelAttribute("userNotifications")
    public List<Notification> userNotifications(){
        User u = loggedUser();
        if(u == null)
            return Collections.emptyList();

        return notificationService.getNotifications(u,false);
    }


}
