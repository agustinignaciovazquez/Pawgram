package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.UnauthorizedException;
import ar.edu.itba.pawgram.webapp.form.ChangeInfoForm;
import ar.edu.itba.pawgram.webapp.form.ChangePasswordForm;
import ar.edu.itba.pawgram.webapp.form.ChangeProfileImageForm;
import ar.edu.itba.pawgram.webapp.form.PasswordForm;
import ar.edu.itba.pawgram.webapp.validator.PasswordChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@RequestMapping("/customize")
@SessionAttributes(value={"changePasswordForm","changeProfilePictureForm","changeInfoForm"})
@Controller
public class CustomizeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizeController.class);
    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordChangeValidator passwordChangeValidator;

    @RequestMapping(value = "/profile/password", method = {RequestMethod.POST})
    public ModelAndView changePassword(@Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm,
                                       final BindingResult errors, @ModelAttribute("loggedUser") final User loggedUser,
                                       RedirectAttributes attr) throws UnauthorizedException {

        LOGGER.debug("User with id {} accessed change password POST", loggedUser.getId());

        final ModelAndView mav = new ModelAndView("redirect:/profile/" + loggedUser.getId());

        changePasswordForm.setCurrentPassword(loggedUser.getPassword());
        passwordChangeValidator.validate(changePasswordForm, errors);

        if (errors.hasErrors()) {
            LOGGER.warn("Failed to change password: form has errors: {}", errors.getAllErrors());
            setErrorState(changePasswordForm, errors, attr, loggedUser);
            return mav;
        }

        final PasswordForm passwordForm = changePasswordForm.getPasswordForm();
        securityUserService.changePassword(loggedUser.getId(), passwordForm.getPassword());
        attr.addFlashAttribute("passFeedback", true);
        attr.addFlashAttribute("changePasswordForm", new ChangePasswordForm());

        LOGGER.info("User with id {} successfully changed his password", loggedUser.getId());

        return mav;
    }

    private void setErrorState(final ChangePasswordForm changePasswordForm, final BindingResult errors,
                               final RedirectAttributes attr, final User loggedUser) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordForm", errors);
        attr.addFlashAttribute("changePasswordForm", changePasswordForm);
        attr.addFlashAttribute("passError", true);
    }

    @RequestMapping(value="/profile/profilePicture", method = {RequestMethod.POST})
    public ModelAndView changeProfilePicture(@Valid @ModelAttribute("changeProfilePictureForm") final ChangeProfileImageForm changeProfilePictureForm ,
                                             final BindingResult errors, @ModelAttribute("loggedUser") final User loggedUser,
                                             final RedirectAttributes attr) throws UnauthorizedException {

        LOGGER.debug("User with id {} accessed change profile picture POST", loggedUser.getId());

        final ModelAndView mav = new ModelAndView("redirect:/profile/" + loggedUser.getId());

        if (errors.hasErrors()) {
            LOGGER.warn("Failed to change profile picture: form has errors: {}", errors.getAllErrors());
            setErrorState(changeProfilePictureForm, errors, attr, loggedUser);
            return mav;
        }

        attr.addFlashAttribute("imgFeedback", true);

        try {
            userService.changeProfile(loggedUser.getId(), changeProfilePictureForm.getProfilePicture().getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to load profile picture: {}", e.getMessage());
            e.printStackTrace();
        }

        return mav;
    }

    private void setErrorState(final ChangeProfileImageForm changeProfilePictureForm, final BindingResult errors,
                               final RedirectAttributes attr, final User loggedUser) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.changeProfilePictureForm", errors);
        attr.addFlashAttribute("changeProfilePictureForm", changeProfilePictureForm);
        attr.addFlashAttribute("imgError", true);
    }

    @RequestMapping(value="/profile/info", method = {RequestMethod.POST})
    public ModelAndView changeNameAndSurname(@Valid @ModelAttribute("changeInfoForm") final ChangeInfoForm changeInfoForm ,
                                             final BindingResult errors, @ModelAttribute("loggedUser") final User loggedUser,
                                             final RedirectAttributes attr) throws UnauthorizedException {

        LOGGER.debug("User with id {} accessed change info POST", loggedUser.getId());

        final ModelAndView mav = new ModelAndView("redirect:/profile/" + loggedUser.getId());

        if (errors.hasErrors()) {
            LOGGER.warn("Failed to change info: form has errors: {}", errors.getAllErrors());
            setErrorState(changeInfoForm, errors, attr, loggedUser);
            return mav;
        }
        userService.changeName(loggedUser.getId(),changeInfoForm.getName(),changeInfoForm.getSurname());
        attr.addFlashAttribute("changeInfoForm", new ChangeInfoForm());

        return mav;
    }
    private void setErrorState(final ChangeInfoForm changeInfoForm, final BindingResult errors,
                               final RedirectAttributes attr, final User loggedUser) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.changeInfoForm", errors);
        attr.addFlashAttribute("changeInfoForm", changeInfoForm);
    }
}