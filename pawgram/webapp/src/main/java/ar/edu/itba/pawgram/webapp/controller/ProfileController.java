package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequestMapping("/profile")
@Controller
public class ProfileController {
    private static final int PAGE_SIZE = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

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

    @RequestMapping("/{userId}")

    public ModelAndView user(@PathVariable final long userId,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "latitude", required = false) final Optional<Double> latitude,
                             @RequestParam(value = "longitude", required = false) final Optional<Double> longitude,
                             @ModelAttribute("loggedUser") final User loggedUser) throws ResourceNotFoundException {
        LOGGER.debug("Accessed user profile with ID: {}", userId);

        final ModelAndView mav = new ModelAndView("profile");
        final User user = userService.findById(userId);

        if (user == null) {
            LOGGER.warn("Cannot render user profile: user ID not found: {}", userId);
            throw new UserNotFoundException();
        }

        final long max_page = postService.getMaxPageByUserId(PAGE_SIZE,userId);
        if (page < 1 || page > max_page && max_page > 0) {
            LOGGER.warn("Page out of bounds: {}", page);
            throw new ResourceNotFoundException();
        }

        final List<PlainPost> posts;
        if(longitude.isPresent() && latitude.isPresent()) {
            posts = postService.getPlainPostsByUserIdPaged(userId, new Location(longitude.get(),latitude.get()), page, PAGE_SIZE);
        }else{
            posts = postService.getPlainPostsByUserIdPaged(userId, page, PAGE_SIZE);
        }
        LOGGER.debug("Quantity of post {} with page {} ", posts.size(),page);

        mav.addObject("categories", Category.values());
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", max_page);
        mav.addObject("profileUser", user);
        mav.addObject("userPosts", posts);
        if(longitude.isPresent() && latitude.isPresent()) {
            mav.addObject("latitude", latitude.get());
            mav.addObject("longitude", longitude.get());
        }
        return mav;
    }

    @RequestMapping("/{userId}/category/{category}")
    public ModelAndView user(@PathVariable final long userId,
                             @PathVariable(value = "category") Category category,
                             @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                             @RequestParam(value = "latitude", required = false) final Optional<Double> latitude,
                             @RequestParam(value = "longitude", required = false) final Optional<Double> longitude,
                             @ModelAttribute("loggedUser") final User loggedUser) throws ResourceNotFoundException {
        LOGGER.debug("Accessed user profile with ID: {} and category {}", userId, category);

        final ModelAndView mav = new ModelAndView("profile");
        final User user = userService.findById(userId);

        if (user == null) {
            LOGGER.warn("Cannot render user profile: user ID not found: {}", userId);
            throw new UserNotFoundException();
        }

        final long max_page = postService.getMaxPageByUserId(PAGE_SIZE,userId,category);
        if (page < 1 || page > max_page && max_page > 0) {
            LOGGER.warn("Page out of bounds: {}", page);
            throw new ResourceNotFoundException();
        }

        final List<PlainPost> posts;
        if(longitude.isPresent() && latitude.isPresent()) {
            posts = postService.getPlainPostsByUserIdPaged(userId, new Location(longitude.get(),latitude.get()), category, page, PAGE_SIZE);
        }else{
            posts = postService.getPlainPostsByUserIdPaged(userId, category, page, PAGE_SIZE);
        }
        LOGGER.debug("Quantity of post {} with page {} and category {} ", posts.size(),page, category);

        mav.addObject("categories", Category.values());
        mav.addObject("currentCategory", category);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", max_page);
        mav.addObject("profileUser", user);
        mav.addObject("userPosts", posts);
        if(longitude.isPresent() && latitude.isPresent()) {
            mav.addObject("latitude", latitude.get());
            mav.addObject("longitude", longitude.get());
        }
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
