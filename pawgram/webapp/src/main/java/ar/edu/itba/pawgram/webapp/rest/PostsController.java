package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.service.*;
import ar.edu.itba.pawgram.model.Comment;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.webapp.dto.CommentDTO;
import ar.edu.itba.pawgram.webapp.dto.PostDTO;
import ar.edu.itba.pawgram.webapp.dto.form.FormComment;
import ar.edu.itba.pawgram.webapp.dto.form.FormPicture;
import ar.edu.itba.pawgram.webapp.dto.form.FormPost;
import ar.edu.itba.pawgram.webapp.dto.form.FormPostPictures;
import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
import ar.edu.itba.pawgram.webapp.utils.PaginationLinkFactory;
import ar.edu.itba.pawgram.webapp.validators.DTOConstraintValidator;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("posts")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class PostsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostImageService postImageService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private PaginationLinkFactory linkFactory;

    @Autowired
    private DTOConstraintValidator validator;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/{id}")
    public Response getPostById(@PathParam("id") final long id, @QueryParam("latitude") Double latitude,
                                @QueryParam("longitude") Double longitude) {
        LOGGER.debug("Accesed getProductById with ID: {}", id);

        final Post post;
        if(longitude != null && latitude != null){
            post = postService.getFullPostById(id,new Location(longitude,latitude));
        }else{
            post = postService.getFullPostById(id);
        }

        if (post == null) {
            LOGGER.warn("Post with ID: {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        else {
            return Response.ok(new PostDTO(post, uriContext.getBaseUri(), Optional.ofNullable(securityUserService.getLoggedInUser()))).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") final long id) {
        LOGGER.debug("Accesed delete post with ID: {}", id);

        final Post post = postService.getFullPostById(id);
        final User user = securityUserService.getLoggedInUser();

        if (post == null) {
            LOGGER.debug("Post to delete not found");
            return Response.noContent().build();
        }

        if (!user.equals(post.getOwner())) {
            LOGGER.warn("Forbidden user {} trying to delete post {} he/she is not owner of", user.getId(), id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(postService.deletePostById(id))
            LOGGER.warn("Post with id {} deleted by {}", id,user.getId());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{postId}/image/{postImageId}")
    public Response deletePostImage(@PathParam("postId") final long postId,@PathParam("postImageId") final long postImageId) {
        LOGGER.debug("Accesed delete post image with ID : {} and postID: {}", postImageId,postId);

        final Post post = postService.getFullPostById(postId);
        final User user = securityUserService.getLoggedInUser();

        if (post == null) {
            LOGGER.debug("Post to delete not found");
            return Response.noContent().build();
        }

        if (!user.equals(post.getOwner())) {
            LOGGER.warn("Forbidden user {} trying to delete image from post {} he/she is not owner of", user.getId(), postId);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final PostImage postImage = postImageService.getPostImageById(postId,postImageId);
        if(postImage == null){
            LOGGER.warn("Failed to delete post image with post id {} and image id {}: post image not found", postId,postImageId);
            return Response.noContent().build();
        }

        if (postImageService.deletePostImage(postId,postImageId))
            LOGGER.info("Post image with id {} from post with id {} deleted by user with id {}", postImageId,postId, user.getId());

        return Response.noContent().build();
    }

    @GET
    @Path("/images/{imageId}")
    @Produces({"image/png", "image/jpeg"})
    public Response getPostImage(@PathParam("imageId") final String postImageId) {
        LOGGER.debug("Accessed get with image ID: {}", postImageId);
        final byte[] image;
        try {
            image = postImageService.getImage(postImageId);
        } catch (FileException e) {
            LOGGER.warn("Image with image ID: {} not found", postImageId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(image).build();
    }

    @POST
    @Path("{postId}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response commentPost(@PathParam("postId") final long postId, final FormComment formComment) throws DTOValidationException, InvalidCommentException {
        final Post post = postService.getPlainPostById(postId);

        if (post == null) {
            LOGGER.debug("Failed to commment post {}, not found", postId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final User user = securityUserService.getLoggedInUser();

        validator.validate(formComment, "Failed to validate comment");

        final Comment comment = formComment.hasParentId() ?
                commentService.createComment(formComment.getContent(), formComment.getParentId(), postId, user.getId()) :
                commentService.createParentComment(formComment.getContent(), postId, user.getId());

        return Response.ok(new CommentDTO(comment, uriContext.getBaseUri())).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createPost(@FormDataParam("post") final FormPost formPost, @BeanParam final FormPostPictures formPictures)
            throws DTOValidationException, PostCreateException{
        LOGGER.debug("Accessed createPost");

        // @FormDataParam parameter is optional --> it may be null
        if (formPost == null) {
            LOGGER.warn("FormDataParam null in createPost");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        performFormValidations(formPost, formPictures);

        final User creator = securityUserService.getLoggedInUser();
        final Post post;
        try {
            post = postService.createPost(formPost.getTitle(),formPost.getDescription(),formPictures.getPicturesBytes(),
                    formPost.getContact_phone(),formPost.getEvent_date(),
                    formPost.getAsCategory(),formPost.getAsPet(),formPost.getIs_male(),
                    formPost.getLocation(),creator);
        }  catch (InvalidPostException e) {
            LOGGER.warn("User with id {} failed to post post: possible upload hack attempt: {}", creator.getId(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI location = uriContext.getAbsolutePathBuilder().path(String.valueOf(post.getId())).build();

        LOGGER.info("User with id {} created post with id {}", creator.getId(), post.getId());

        return Response.created(location).entity(new PostDTO(post, uriContext.getBaseUri(),
                Optional.ofNullable(securityUserService.getLoggedInUser()))).build();
    }

    @PUT
    @Path("/{postId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response modifyPost(@PathParam("postId") final long postId, @FormDataParam("post") final FormPost formPost, @BeanParam final FormPostPictures formPictures)
            throws DTOValidationException, PostCreateException{
        LOGGER.debug("Accessed modifyPost");

        // @FormDataParam parameter is optional --> it may be null
        if (formPost == null) {
            LOGGER.warn("FormDataParam null in createPost");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        performFormValidations(formPost, formPictures);

        LOGGER.debug("User with id {} accessed modify post with id {}", loggedUser.getId(),postId);
        final User creator = securityUserService.getLoggedInUser();
        Post post = postService.getFullPostById(postId);

        if (post == null) {
            LOGGER.warn("Failed to modify post with id {}: post not found", postId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!creator.equals(post.getOwner())){
            LOGGER.warn("Failed to modify post with id {}: logged user with id {} is not post creator with id {}",
                    postId, creator.getId(), post.getOwner().getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        try {
            post = postService.createPost(formPost.getTitle(),formPost.getDescription(),formPictures.getPicturesBytes(),
                    formPost.getContact_phone(),formPost.getEvent_date(),
                    formPost.getAsCategory(),formPost.getAsPet(),formPost.getIs_male(),
                    formPost.getLocation(),creator);
        }  catch (InvalidPostException e) {
            LOGGER.warn("User with id {} failed to post post: possible upload hack attempt: {}", creator.getId(), e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        final URI location = uriContext.getAbsolutePathBuilder().path(String.valueOf(post.getId())).build();
        LOGGER.info("User with id {} modified post with id {}", creator.getId(), post.getId());
        return Response.created(location).entity(new PostDTO(post, uriContext.getBaseUri(),
                Optional.ofNullable(securityUserService.getLoggedInUser()))).build();
    }

    private void performFormValidations(final FormPost formPost, final FormPostPictures formPictures) throws DTOValidationException {
        validator.validate(formPost, "Failed to validate post");

        validator.validate(formPictures, "Failed to validate post pictures");

        for (FormDataBodyPart bodyPart : formPictures.getPictures())
            validator.validate(new FormPicture(bodyPart), "Failed to validate post pictures");
    }

    @PUT
    @Path("/{id}/subscriptions")
    public Response subscribePost(@PathParam("id") long postId) {
        final User user = securityUserService.getLoggedInUser();
        subscribeService.subscribePost(postId,user.getId());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}/subscriptions")
    public Response unsubscribePost(@PathParam("id") long postId) {
        final User user = securityUserService.getLoggedInUser();
        subscribeService.unsubscribePost(postId,user.getId());
        return Response.noContent().build();
    }
}
