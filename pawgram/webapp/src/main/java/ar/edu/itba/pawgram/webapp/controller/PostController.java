package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.webapp.exception.ImageNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.PostNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.ResourceNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.UnauthorizedException;
import ar.edu.itba.pawgram.webapp.form.CommentForm;
import ar.edu.itba.pawgram.webapp.form.CommentsForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@RequestMapping("/post")
@Controller
public class PostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostImageService postImageService;

    @ModelAttribute("commentsForm")
    public CommentsForm formComments() {
        return new CommentsForm();
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public ModelAndView getPost(@PathVariable final long postId,
                                @RequestParam(value = "latitude", required = false) final Optional<Double> latitude,
                                @RequestParam(value = "longitude", required = false) final Optional<Double> longitude) throws PostNotFoundException {

        LOGGER.debug("Accessed post with id {}", postId);

        Post post;
        if(longitude.isPresent() && latitude.isPresent()) {
             post = postService.getFullPostById(postId, new Location(longitude.get(), latitude.get()));
        }else{
             post = postService.getFullPostById(postId);
        }
        if (post == null) {
            LOGGER.warn("Failed to render post with id {}: post not found", postId);
            throw new PostNotFoundException();
        }

        final ModelAndView mav = new ModelAndView("post");

        mav.addObject("post", post);
        mav.addObject("parentComments", post.getCommentFamilies());

        return mav;
    }

    @RequestMapping(value = "/{postId}/comment", method = RequestMethod.POST)
    public ModelAndView postComment (@PathVariable final long postId,
                                     @ModelAttribute("loggedUser") final User loggedUser,
                                     @RequestParam(value = "parentid", required = false) final Optional<Integer> parentId,
                                     @RequestParam(value = "index", required = false) final Optional<Integer> replyCommentIndex,
                                     @Valid @ModelAttribute("commentsForm") final CommentsForm form,
                                     final BindingResult errors,
                                     final RedirectAttributes attr) throws PostNotFoundException {

        final PlainPost post = postService.getPlainPostById(postId);

        if (post == null) {
            LOGGER.warn("Failed to comment post with id {}: post doesn´t exists", postId);
            throw new PostNotFoundException();
        }

        LOGGER.debug("User with id {} accessed comment POST for post with id {}", loggedUser.getId(), postId);
        final CommentForm postedForm;

        if (replyCommentIndex.isPresent()) {
            LOGGER.debug("User with id {} attempting to post comment replying to comment with id {} in position {}",
                    loggedUser.getId(), parentId.get(), replyCommentIndex.get());
            postedForm = form.getChildForm(replyCommentIndex.get());
        } else {
            LOGGER.debug("User with id {} attempting to post parent comment", loggedUser.getId());
            postedForm = form.getParentForm();
        }

        final ModelAndView mav = new ModelAndView("redirect:/post/" + postId);

        if (errors.hasErrors()) {
            LOGGER.warn("User {} failed to post comment: form has errors: {}", loggedUser.getId(), errors.getAllErrors());
            String errorForm = replyCommentIndex.isPresent() ? replyCommentIndex.get().toString() : "parent";
            setErrorState(postId, form, errors, attr, errorForm);
            return mav;
        }

        final Comment comment;
        if (parentId.isPresent()) {
            comment = commentService.createComment(postedForm.getContent(), parentId.get(), postId, loggedUser.getId());
            LOGGER.info("User with id {} posted comment with id {} in reply to comment with id {}", loggedUser.getId(), comment.getId(), parentId.get());
        }
        else {
            comment = commentService.createParentComment(postedForm.getContent(), postId, loggedUser.getId());
            LOGGER.info("User with id {} posted parent comment with id {}", loggedUser.getId(), comment.getId());
        }

        attr.addFlashAttribute("comment", comment.getId());
        return mav;
    }

    private void setErrorState(long postId, CommentsForm form, final BindingResult errors, RedirectAttributes attr, String errorForm) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.commentsForm", errors)
                .addFlashAttribute("commentsForm", form)
                .addFlashAttribute("form", errorForm);
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageId}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public byte[] getPostImage(@PathVariable final String imageId) throws ImageNotFoundException {
        final byte[] img;
        try {
            img = postImageService.getImage(imageId);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.warn("Failed to render image with id {}: image not found", imageId);
            throw new ImageNotFoundException();
        }
        return img;
    }
}