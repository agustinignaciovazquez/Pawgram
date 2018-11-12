package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.exception.InvalidNotificationException;
import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.ForbiddenException;
import ar.edu.itba.pawgram.webapp.exception.NotificationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RequestMapping("/notifications")
@Controller
public class NotificationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = {"/",""})
    public ModelAndView showNotifications(@RequestParam(value = "all", required = false) Optional<Boolean> all, @ModelAttribute("loggedUser") final User loggedUser){
        LOGGER.debug("Accessed notifications");
        final List<Notification> notifications;
        if(all.isPresent()){
           notifications = notificationService.getNotifications(loggedUser, all.get());
        }else {
            notifications = notificationService.getNotifications(loggedUser, true);
        }

        ModelAndView mav = new ModelAndView("notifications");
        mav.addObject("categories", Category.values());
        mav.addObject("notifications", notifications);
        return mav;
    }

    @RequestMapping(value = "/mark/seen/{notificationId}")
    public ModelAndView markNotificationAsSeen(@PathVariable final long notificationId,
                                     @ModelAttribute("loggedUser") final User loggedUser,
                                     @RequestHeader(value = "referer", required = false, defaultValue = "/") final String referrer)
            throws NotificationNotFoundException, ForbiddenException {
        LOGGER.debug("Accessed delete notification with id {}",notificationId);

        final Notification notification = notificationService.getFullNotificationById(notificationId);
        if (notification == null) {
            LOGGER.warn("Failed to mark as seen notification with id {}: notification not found", notificationId);
            throw new NotificationNotFoundException();
        }

        if(!loggedUser.equals(notification.getUser())){
            LOGGER.warn("Failed to mark as seen notification with id {}: logged user with id {} is not notification user with id {}",
                    notificationId, loggedUser.getId(), notification.getUser().getId());
            throw new ForbiddenException();
        }

        try {
            if (notificationService.markNotificationAsSeen(notificationId))
                LOGGER.info("Notification with id {} mark as seen by user with id {}", notificationId, loggedUser.getId());
        } catch (InvalidNotificationException e) {
            //e.printStackTrace();
            LOGGER.warn("Unexpected invalid notification while marking as seen with notification id {}\n Stack trace: {}", notificationId, e.getMessage());
            throw new NotificationNotFoundException();
        }

        return new ModelAndView("redirect:" + referrer);
    }
}
