package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.ImageNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.ResourceNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.UserNotFoundException;
import ar.edu.itba.pawgram.webapp.form.ChangeInfoForm;
import ar.edu.itba.pawgram.webapp.form.ChangePasswordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RequestMapping("/profile")
@Controller
public class ProfileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
    @Autowired
    private UserService userService;

    @ModelAttribute("changePasswordForm")
    public ChangePasswordForm passwordForm(@ModelAttribute("loggedUser") final User loggedUser){
        return new ChangePasswordForm();
    }

    @ModelAttribute("changeProfilePictureForm")
    public ChangePasswordForm pictureForm(@ModelAttribute("loggedUser") final User loggedUser){
        return new ChangePasswordForm();
    }

    @ModelAttribute("changeInfoForm")
    public ChangeInfoForm infoForm(@ModelAttribute("loggedUser") final User loggedUser){
        return new ChangeInfoForm();
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView user(@PathVariable final long userId) throws UserNotFoundException {
        LOGGER.debug("Accessed user profile with ID: {}", userId);

        final ModelAndView mav = new ModelAndView("profile");
        final User user = userService.findById(userId);

        if (user == null) {
            LOGGER.warn("Cannot render user profile: user ID not found: {}", userId);
            throw new UserNotFoundException();
        }

        mav.addObject("profileUser", user);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getProfileImage(@PathVariable final String imageId) throws ImageNotFoundException {
        final byte[] img;
        try {
            img = userService.getProfileImage(imageId);
        } catch (IOException e) {
            //e.printStackTrace(); DEBUG ONLY
            LOGGER.warn("Failed to render profile image with id {}: image not found\n Stacktrace {}", imageId, e.getMessage());
            throw new ImageNotFoundException();
        }
        return img;
    }
}
