package ar.edu.itba.pawgram.webapp.controller;

import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Location;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import ar.edu.itba.pawgram.webapp.exception.PostNotFoundException;
import ar.edu.itba.pawgram.webapp.exception.UnauthorizedException;
import ar.edu.itba.pawgram.webapp.form.CommentForm;
import ar.edu.itba.pawgram.webapp.form.CommentsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/post")
@Controller
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @ModelAttribute("commentsForm")
    public CommentsForm formComments() {
        return new CommentsForm();
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.GET)
    public ModelAndView getProduct(@PathVariable final long postId, @RequestParam(value = "latitude", required = false, defaultValue = "0") double latitude,
                                   @RequestParam(value = "longitude", required = false, defaultValue = "0") double longitude) throws PostNotFoundException {


        final Post post = postService.getFullPostById(postId, new Location(longitude,latitude));

        if (post == null) {
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
            throw new PostNotFoundException();
        }


        final CommentForm postedForm;

        if (replyCommentIndex.isPresent()) {
            postedForm = form.getChildForm(replyCommentIndex.get());
        }
        else {
            postedForm = form.getParentForm();
        }

        final ModelAndView mav = new ModelAndView("redirect:/post/" + postId);

        if (errors.hasErrors()) {
            String errorForm = replyCommentIndex.isPresent() ? replyCommentIndex.get().toString() : "parent";
            setErrorState(postId, form, errors, attr, errorForm);
            return mav;
        }

        final Comment comment;
        if (parentId.isPresent()) {
            comment = commentService.createComment(postedForm.getContent(), parentId.get(), postId, loggedUser.getId());
        }
        else {
            comment = commentService.createParentComment(postedForm.getContent(), postId, loggedUser.getId());
        }

        attr.addFlashAttribute("comment", comment.getId());
        return mav;
    }

    private void setErrorState(long postId, CommentsForm form, final BindingResult errors, RedirectAttributes attr, String errorForm) {
        attr.addFlashAttribute("org.springframework.validation.BindingResult.commentsForm", errors)
                .addFlashAttribute("commentsForm", form)
                .addFlashAttribute("form", errorForm);
    }
}
