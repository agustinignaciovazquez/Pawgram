package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SubscribeService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class SubscribeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeController.class);

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/subscriptions"})
    public ModelAndView showSubscriptions( @ModelAttribute("loggedUser") final User loggedUser){
        LOGGER.debug("Accessed subscriptions");
        final Set<Post> postsSubscribed = subscribeService.getUserSubscribedPosts(loggedUser.getId());

        ModelAndView mav = new ModelAndView("subscriptions");
        mav.addObject("categories", Category.values());
        mav.addObject("subscriptions", postsSubscribed);
        return mav;
    }

    @RequestMapping(value = "/subscribe/post/{postId}", method = RequestMethod.POST)
    public ModelAndView subscribePost(@PathVariable final long postId,
                                    @ModelAttribute("loggedUser") final User loggedUser,
                                    @RequestHeader(value = "referer", required = false, defaultValue = "/") final String referrer)
            throws PostNotFoundException{

        final Post post = postService.getPlainPostById(postId);

        if (post == null) {
            LOGGER.warn("Failed to subscribe to post with id {}: post not found", postId);
            throw new PostNotFoundException();
        }

        subscribeService.subscribePost(post.getId(),loggedUser.getId());

        LOGGER.debug("User {} subscribed to post with id {}", loggedUser.getId(), postId);
        return new ModelAndView("redirect:" + referrer);
    }

    @RequestMapping(value = "/unsubscribe/post/{postId}", method = RequestMethod.POST)
    public ModelAndView unsubscribePost(@PathVariable final long postId,
                                    @ModelAttribute("loggedUser") final User loggedUser,
                                    @RequestHeader(value = "referer", required = false, defaultValue = "/") final String referrer)
            throws PostNotFoundException{

        final Post post = postService.getPlainPostById(postId);

        if (post == null) {
            LOGGER.warn("Failed to unsubscribe to post with id {}: post not found", postId);
            throw new PostNotFoundException();
        }

        subscribeService.unsubscribePost(post.getId(),loggedUser.getId());

        LOGGER.debug("User {} unsubscribed to post with id {}", loggedUser.getId(), postId);

        return new ModelAndView("redirect:" + referrer);
    }
}
