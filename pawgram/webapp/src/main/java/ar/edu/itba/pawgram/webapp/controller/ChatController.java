package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.MessageService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Chat;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.ForbiddenException;
import ar.edu.itba.pawgram.webapp.exception.UserNotFoundException;
import ar.edu.itba.pawgram.webapp.form.MessageForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@RequestMapping("/messages")
@Controller
public class ChatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @ModelAttribute("messageForm")
    public MessageForm formComments() {
        return new MessageForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView index(@ModelAttribute("loggedUser") final User loggedUser) {
        LOGGER.debug("Accessed chat from user {}",loggedUser.getId());
        ModelAndView mav = new ModelAndView("chat");
        mav.addObject("categories", Category.values());
        mav.addObject("messagedUsers",messageService.getMessageUsers(loggedUser));
        return mav;
    }

    @RequestMapping("/user/{userId}")
    public ModelAndView user(@PathVariable final long userId, @ModelAttribute("loggedUser") final User loggedUser) throws
            UserNotFoundException, ForbiddenException {
        LOGGER.debug("Accessed chat from user {} with user with ID: {}",loggedUser.getId(), userId);

        final ModelAndView mav = new ModelAndView("chat");
        final User user = userService.findById(userId);

        if (user == null) {
            LOGGER.warn("Cannot render user chat: user ID not found: {}", userId);
            throw new UserNotFoundException();
        }

        if(user.equals(loggedUser)){
            LOGGER.warn("User with id {} tried to chat with himself", userId);
            throw new ForbiddenException();
        }

        mav.addObject("categories", Category.values());
        mav.addObject("messagedUsers", messageService.getMessageUsers(loggedUser));
        mav.addObject("messages", messageService.getMessages(loggedUser,user));
        return mav;
    }
    @RequestMapping(value = "/user/{userId}/send", method = RequestMethod.POST)
    public ModelAndView postComment (@PathVariable final long userId,
                                     @ModelAttribute("loggedUser") final User loggedUser,
                                     @Valid @ModelAttribute("messageForm") final MessageForm form,
                                     final BindingResult errors,
                                     final RedirectAttributes attr) throws UserNotFoundException, ForbiddenException  {
        LOGGER.debug("User with ID: {} tries send message form with ID {}",loggedUser.getId(), userId);


        final User user = userService.findById(userId);

        if (user == null) {
            LOGGER.warn("Cannot render user chat: user ID not found: {}", userId);
            throw new UserNotFoundException();
        }

        if(user.equals(loggedUser)){
            LOGGER.warn("User with id {} tried to chat with himself", userId);
            throw new ForbiddenException();
        }

        final ModelAndView mav = new ModelAndView("redirect:/chat/user/"+user.getId());

        if (errors.hasErrors()) {
            LOGGER.warn("User {} failed to send message to user with id {}: form has errors: {}",
                    loggedUser.getId(), userId, errors.getAllErrors());
            setErrorState(userId, form, errors, attr);
            return mav;
        }

        final Message message = messageService.sendMessage(loggedUser,user,form.getMessage());

        attr.addFlashAttribute("message", message.getId());
        return mav;
    }

    private void setErrorState(long userId, MessageForm form, final BindingResult errors, RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.messageForm", errors)
                .addFlashAttribute("messageForm", form);
    }
}
