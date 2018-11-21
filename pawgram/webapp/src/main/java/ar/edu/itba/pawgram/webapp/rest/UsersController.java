package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import ar.edu.itba.pawgram.webapp.dto.PostListDTO;
import ar.edu.itba.pawgram.webapp.dto.UserDTO;
import ar.edu.itba.pawgram.webapp.dto.form.FormChangePassword;
import ar.edu.itba.pawgram.webapp.dto.form.FormPicture;
import ar.edu.itba.pawgram.webapp.dto.form.FormUser;
import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
import ar.edu.itba.pawgram.webapp.exceptionmapper.ValidationMapper;
import ar.edu.itba.pawgram.webapp.utils.PaginationLinkFactory;
import ar.edu.itba.pawgram.webapp.validators.DTOConstraintValidator;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@Path("users")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class UsersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationLinkFactory linkFactory;

    @Autowired
    private DTOConstraintValidator DTOValidator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/")
    public Response getAuthenticatedUser() {
        LOGGER.debug("Accessed getAuthenticatedUser");

        final User authenticatedUser = securityUserService.getLoggedInUser();

        return Response.ok(new UserDTO(authenticatedUser, uriContext.getBaseUri())).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") final int id) {
        final User user = userService.findById(id);

        LOGGER.debug("Accessed getUserById with id {}", id);

        if (user != null) {
            return Response.ok(new UserDTO(user, uriContext.getBaseUri())).build();
        } else {
            LOGGER.warn("Cannot render user profile, user with id {} not found", id);
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id}/subscriptions")
    public Response getUserSubscriptions(
            @PathParam("id") final long id,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {

        LOGGER.debug("Accessed getUserSubscriptions with id {}", id);

        page = nonNegativePage(page);
        pageSize = validPageSizeRange(pageSize);

        final User user = userService.findById(id);

        if (user == null) {
            LOGGER.debug("Failed to get user with ID: {} created subscriptions, user not found");
            return Response.status(Status.NOT_FOUND).build();
        }

        final long totalSubscriptions = userService.getTotalSubscriptionsByUserId(id);
        final long maxPage = userService.getMaxSubscribedPageWithSize(id, pageSize);
        final List<Post> subscribedPosts = userService.getSubscribedPostsPaged(id, page, pageSize);
        final Link[] linkArray = linkFactory.createLinks(uriContext, page, maxPage).values().toArray(new Link[0]);

        return Response.ok(new PostListDTO(subscribedPosts, totalSubscriptions, uriContext.getBaseUri(),
                Optional.ofNullable(securityUserService.getLoggedInUser()))).links(linkArray).build();
    }

    @GET
    @Path("images/{id}")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final String id) throws FileException {
        LOGGER.debug("Accessed getUserProfilePicture with id {}", id);

        final byte[] picture = userService.getProfileImage(id);

        if (picture == null || picture.length == 0) {
            LOGGER.warn("Cannot render user profile picture, user with id {} not found", id);
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(picture).build();
    }


    @GET
    @Path("images/default")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getDefaultUserProfilePicture() {
        LOGGER.debug("Accessed getDefaultUserProfilePicture");
        final byte[] picture;
        try {
            File defaultImage = ResourceUtils.getFile("classpath:master_default_pic.jpg");
            picture = Files.readAllBytes(defaultImage.toPath());
        } catch (IOException e) {
            LOGGER.warn("Cannot render default user profile picture \n {}",e.getMessage());
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok(picture).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createUser(@FormDataParam("user") final FormUser formUser)
            throws DuplicateEmailException, DTOValidationException {

        LOGGER.debug("Accessed createUser");

        // @FormDataParam parameter is optional --> it may be null
        if (formUser == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(formUser, "Failed to validate user");

        final User user = securityUserService.registerUser(formUser.getName(),formUser.getSurname(),formUser.getMail(),formUser.getPassword(),null);
        final URI location = uriContext.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();

        return Response.created(location).entity(new UserDTO(user, uriContext.getBaseUri())).build();
    }

    @PUT
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(final FormChangePassword changePasswordForm) throws DTOValidationException, InvalidUserException {
        LOGGER.debug("Accessed change password");

        DTOValidator.validate(changePasswordForm, "Failed to validate change password form");

        final User authenticatedUser = securityUserService.getLoggedInUser();

        if (!passwordEncoder.matches(changePasswordForm.getCurrentPassword(), authenticatedUser.getPassword()))
            return Response.status(ValidationMapper.UNPROCESSABLE_ENTITY).entity(new ExceptionDTO("Incorrect password")).build();

        securityUserService.changePassword(authenticatedUser.getId(), changePasswordForm.getNewPassword());

        return Response.noContent().build();
    }

    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response changePicture(@BeanParam final FormPicture picture) throws DTOValidationException, FileUploadException, InvalidUserException {
        LOGGER.debug("Accessed change picture");

        DTOValidator.validate(picture, "Failed to validate picture");

        final User authenticatedUser = securityUserService.getLoggedInUser();

        userService.changeProfile(authenticatedUser.getId(), picture.getPictureBytes());

        return Response.noContent().build();
    }

    private int nonNegativePage(int page) {
        return (page < 1) ? 1 : page;
    }

    private int validPageSizeRange(int pageSize) {
        return pageSize = (pageSize < 1 || pageSize > MAX_PAGE_SIZE) ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
