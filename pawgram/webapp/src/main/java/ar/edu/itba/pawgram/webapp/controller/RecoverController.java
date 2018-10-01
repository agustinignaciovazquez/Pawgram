package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.interfaces.service.EmailService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.UserNotFoundException;
import ar.edu.itba.pawgram.webapp.form.ForgetForm;
import ar.edu.itba.pawgram.webapp.form.RecoverForm;
import ar.edu.itba.pawgram.webapp.validator.MailValidator;
import ar.edu.itba.pawgram.webapp.validator.TokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RequestMapping("/login/forget")
@Controller
public class RecoverController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverController.class);
    @Autowired
    private UserService us;

    @Autowired
    private EmailService ms;

    @Autowired
    private TokenValidator tokenValidator;

    @Autowired
    private MailValidator mailValidator;

    @ModelAttribute("recoverForm")
    public RecoverForm recoverForm(){
        return new RecoverForm();
    }

    @ModelAttribute("forgetForm")
    public ForgetForm forgetForm(){
        return new ForgetForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView index() {
        return new ModelAndView("recover_index");
    }

    @RequestMapping(value = "/process", method = {RequestMethod.POST})
    public ModelAndView recoverPassword(@Valid @ModelAttribute("forgetForm") final ForgetForm forgetForm,
                                        final BindingResult errors, RedirectAttributes attr) throws UserNotFoundException, SendMailException {

        LOGGER.debug("Accessed reset password POST w/ email", forgetForm.getMail());

        final ModelAndView mav = new ModelAndView("redirect:/login/forget/");
        mailValidator.validate(forgetForm,errors);

        if (errors.hasErrors()) {
            LOGGER.warn("Failed to send forget token: form has errors: {}", errors.getAllErrors());
            setErrorState(forgetForm, errors, attr);
            return mav;
        }

        final User user = us.findByMail(forgetForm.getMail());
        if(user == null)
            throw new UserNotFoundException(); // THIS SHOULD NEVER HAPPEN BECAUSE OF THE VALIDATOR IN LINE 65 JUST IN CASE WE LEAVE IT HERE
        ms.sendRecoverEmail(user);

        LOGGER.info("Successfully send reset token for user with id {}", user.getId());

        return new ModelAndView("redirect:/login/forget/recover");
    }

    private void setErrorState(final ForgetForm forgetForm, final BindingResult errors,
                               final RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.forgetForm", errors);
        attr.addFlashAttribute("forgetForm", forgetForm);
        attr.addFlashAttribute("passError", true);
    }
    @RequestMapping(value={"/recover"})
    public ModelAndView recover() {
        return new ModelAndView("recover_user");
    }

    @RequestMapping(value = "/recover/process", method = {RequestMethod.POST})
    public ModelAndView recoverPassword(@Valid @ModelAttribute("recoverForm") final RecoverForm recoverForm,
                                       final BindingResult errors, RedirectAttributes attr) throws UserNotFoundException {

        LOGGER.debug("Accessed reset password POST w/ email", recoverForm.getMail());

        final ModelAndView mav = new ModelAndView("redirect:/login/forget/recover/");

        tokenValidator.validate(recoverForm, errors);

        if (errors.hasErrors()) {
            LOGGER.warn("Failed to reset password: form has errors: {}", errors.getAllErrors());
            setErrorState(recoverForm, errors, attr);
            return mav;
        }

        final User user = us.findByMail(recoverForm.getMail());
        if(user == null)
            throw new UserNotFoundException(); // THIS SHOULD NEVER HAPPEN BECAUSE OF THE VALIDATOR IN LINE 102 JUST IN CASE WE LEAVE IT HERE
        us.changePassword(user.getId(), recoverForm.getPasswordForm().getPassword());

        LOGGER.info("User with id {} successfully reset his password", user.getId());

        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getMail(), recoverForm.getPasswordForm().getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }

    private void setErrorState(final RecoverForm recoverForm, final BindingResult errors,
                               final RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.recoverForm", errors);
        attr.addFlashAttribute("recoverForm", recoverForm);
        attr.addFlashAttribute("passError", true);
    }
}
