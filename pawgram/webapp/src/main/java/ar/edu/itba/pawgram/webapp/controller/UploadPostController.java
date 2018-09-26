package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.exception.UnauthorizedException;
import ar.edu.itba.pawgram.webapp.form.PostForm;
import ar.edu.itba.pawgram.webapp.util.CaseInsensitiveConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequestMapping("/post/create")
@Controller
public class UploadPostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadPostController.class);

    @Autowired
    private PostService postService;

    @ModelAttribute("uploadForm")
    public PostForm uploadForm() {
        return new PostForm();
    }

    @RequestMapping(value={"","/"})
    public ModelAndView showCategories(@ModelAttribute("loggedUser") final User loggedUser) {
        LOGGER.debug("User with id {} accessed upload categories", loggedUser.getId());

        final ModelAndView mav = new ModelAndView("upload_select_category");
        mav.addObject("categories", Category.values());
        return mav;
    }

    @RequestMapping(value = "/category/{category}")
    public ModelAndView formCompletion(@ModelAttribute("loggedUser") final User loggedUser, @PathVariable(value = "category") Category category) {
        LOGGER.debug("User with id {} accessed upload category {}", loggedUser.getId(),category);

        final ModelAndView mav = new ModelAndView("upload_post");
        mav.addObject("currentCategory", category);
        mav.addObject("categories", Category.values());
        mav.addObject("pets", Pet.values());
        return mav;
    }

    @RequestMapping(value= "/category/{category}/process", method = {RequestMethod.POST})
    public ModelAndView upload(@PathVariable(value = "category") Category category,
                                                @Valid @ModelAttribute("uploadForm") final PostForm formPost,
                                                final BindingResult errors, @ModelAttribute("loggedUser") final User loggedUser,
                                                final RedirectAttributes attr) throws UnauthorizedException, PostCreateException, IOException {

        LOGGER.debug("User with id {} accessed upload POST", loggedUser.getId());

        if (errors.hasErrors()) {
            LOGGER.warn("User with id {} failed to post post: form has errors: {}", loggedUser.getId(), errors.getAllErrors());
            return errorState(formPost,category, errors, attr);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        LocalDateTime dateTime = LocalDateTime.parse(formPost.getEvent_date(), formatter);
        final Post post =  postService.createPost(formPost.getTitle(),formPost.getDescription(),formPost.getAllRawImages(),
                formPost.getContact_phone(),dateTime,
                category,formPost.getPet(),formPost.getIs_male(),
                formPost.getLocation(),loggedUser);

        LOGGER.info("User with id {} posted post with id {}", loggedUser.getId(), post.getId());

        return new ModelAndView("redirect:/post/" + post.getId());
    }

    private ModelAndView errorState(PostForm form, Category category, final BindingResult errors, RedirectAttributes attr) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.uploadForm", errors);
        attr.addFlashAttribute("uploadForm", form);
        return new ModelAndView("redirect:/post/create/"+category);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Category.class,new CaseInsensitiveConverter<>(Category.class));
    }
}
