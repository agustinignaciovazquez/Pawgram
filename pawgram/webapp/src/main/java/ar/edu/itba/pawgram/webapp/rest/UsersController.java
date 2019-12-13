package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.exception.*;
import ar.edu.itba.pawgram.interfaces.service.EmailService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import ar.edu.itba.pawgram.webapp.dto.PostListDTO;
import ar.edu.itba.pawgram.webapp.dto.UserDTO;
import ar.edu.itba.pawgram.webapp.dto.form.*;
import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
import ar.edu.itba.pawgram.webapp.exceptionmapper.ValidationMapper;
import ar.edu.itba.pawgram.webapp.utils.PaginationLinkFactory;
import ar.edu.itba.pawgram.webapp.validators.DTOConstraintValidator;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
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
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailService ms;

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
    @Path("/mail/{mail}")
    public Response getUserByMail(@PathParam("mail") final String mail) {
        final User user = userService.findByMail(mail);

        LOGGER.debug("Accessed getUserByMail with mail {}", mail);

        if (user != null) {
            return Response.ok(new UserDTO(user, uriContext.getBaseUri())).build();
        } else {
            LOGGER.warn("Cannot render user profile, user with mail {} not found", mail);
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/check/{mail}")
    public Response checkEmailExists(@PathParam("mail") final String mail) {
        final User user = userService.findByMail(mail);

        LOGGER.debug("Accessed check duplicated mail with mail {}", mail);
        if (user != null) {
            return Response.status(Status.OK).build();
        } else {
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
        final User loggedUser = securityUserService.getLoggedInUser();
        if (user == null) {
            LOGGER.debug("Failed to get user with ID: {} created subscriptions, user not found");
            return Response.status(Status.NOT_FOUND).build();
        }
        //For the moment we don't allow other users to see subscriptions we leave code like this in case we want to change it in the future
        if(loggedUser == null){
            LOGGER.debug("Forbidden anonymous user trying to access subscriptions with id {} he/she is not owner of", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (!user.equals(loggedUser)) {
            LOGGER.warn("Forbidden user {} trying to access subscriptionswith id {} he/she is not owner of", user.getId(), id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final long totalSubscriptions = userService.getTotalSubscriptionsByUserId(id);
        final long maxPage = userService.getMaxSubscribedPageWithSize(id, pageSize);
        final List<Post> subscribedPosts = userService.getSubscribedPostsPaged(id, page, pageSize);
        final Link[] linkArray = linkFactory.createLinks(uriContext, page, maxPage).values().toArray(new Link[0]);

        return Response.ok(new PostListDTO(subscribedPosts, totalSubscriptions, uriContext.getBaseUri(),
                Optional.ofNullable(securityUserService.getLoggedInUser()))).links(linkArray).build();
    }

    @GET
    @Path("/{id}/user_posts")
    public Response getUserPosts(
            @PathParam("id") final long id,@QueryParam("category") final Category category,@QueryParam("latitude") Double latitude,
            @QueryParam("longitude") Double longitude, @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {

        LOGGER.debug("Accessed getUserPosts with id {}", id);
        final Optional<Category> categoryOpt = Optional.ofNullable(category);
        final Location location = (latitude != null && longitude != null)? new Location(longitude,latitude):null;
        final Optional<Location> locationOpt = Optional.ofNullable(location);

        page = nonNegativePage(page);
        pageSize = validPageSizeRange(pageSize);

        final User user = userService.findById(id);

        if (user == null) {
            LOGGER.debug("Failed to get user with ID: {} created posts, user not found");
            return Response.status(Status.NOT_FOUND).build();
        }

        final long totalSubscriptions = userService.getTotalPostsByUserId(user.getId(),categoryOpt);
        final long maxPage = userService.getMaxPageByUserId(pageSize,user.getId(),categoryOpt);
        final List<Post> createdPosts = userService.getPlainPostsByUserIdPaged(user.getId(),locationOpt,categoryOpt,page,pageSize);

        final Link[] linkArray = linkFactory.createLinks(uriContext, page, maxPage).values().toArray(new Link[0]);

        return Response.ok(new PostListDTO(createdPosts, totalSubscriptions, uriContext.getBaseUri(),
                Optional.ofNullable(securityUserService.getLoggedInUser()))).links(linkArray).build();
    }

    @GET
    @Path("images/{id}")
    @Produces(value = {"image/png", "image/jpeg"})
    public Response getUserProfilePicture(@PathParam("id") final String id) {
        LOGGER.debug("Accessed getUserProfilePicture with id {}", id);

        final byte[] picture;
        try {
            picture = userService.getProfileImage(id);
        } catch (FileException e) {
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
    public Response createUser(@FormDataParam("user") final FormUser formUser, @BeanParam final FormPicture formPicture, @Context HttpServletRequest request)
            throws DuplicateEmailException, DTOValidationException, FileUploadException, InvalidUserException {

        LOGGER.debug("Accessed createUser");
        Locale locale = request.getLocale();

        // @FormDataParam parameter is optional --> it may be null
        if (formUser == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(formUser, "Failed to validate user");
        if(formPicture != null && formPicture.isPicture())
            DTOValidator.validate(formPicture, "Failed to validate picture");

        final User user = securityUserService.registerUser(formUser.getName(),formUser.getSurname(),formUser.getMail(),formUser.getPassword(),null);
        if(formPicture != null && formPicture.isPicture() && user != null)
            userService.changeProfile(user.getId(),formPicture.getPictureBytes());

        final URI location = uriContext.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();

        try {
            String [] args = {user.getName() + " " +user.getSurname()};
            ms.sendWelcomeEmail(user,messageSource.getMessage("welcomeMailMessage",args,locale),
                    messageSource.getMessage("welcomeMailSubject",null,locale));
        } catch (SendMailException e) {
            LOGGER.warn("Could not send welcome email registered \n Stacktrace {}", e.getMessage());
        }

        LOGGER.info("New user created with id {}", user.getId());
        return Response.created(location).entity(new UserDTO(user, uriContext.getBaseUri())).build();
    }

    @PUT
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(final FormChangePassword changePasswordForm) throws DTOValidationException, InvalidUserException {
        LOGGER.debug("Accessed change password");

        // @FormDataParam parameter is optional --> it may be null
        if (changePasswordForm == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(changePasswordForm, "Failed to validate change password form");

        final User authenticatedUser = securityUserService.getLoggedInUser();
        if(authenticatedUser == null){
            LOGGER.debug("Failed to modify password, user not found");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (!passwordEncoder.matches(changePasswordForm.getCurrentPassword(), authenticatedUser.getPassword()))
            return Response.status(ValidationMapper.UNPROCESSABLE_ENTITY).entity(new ExceptionDTO("Incorrect password")).build();

        securityUserService.changePassword(authenticatedUser.getId(), changePasswordForm.getNewPassword());
        LOGGER.info("User with id {} changed password",authenticatedUser.getId());
        return Response.noContent().build();
    }

    @PUT
    @Path("/get_recover_token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTokenForRecovery(final FormMail recoverPasswordForm, @Context HttpServletRequest request) throws DTOValidationException,
            InvalidUserException, SendMailException {
        LOGGER.debug("Accessed recover password");
        Locale locale = request.getLocale();

        // @FormDataParam parameter is optional --> it may be null
        if (recoverPasswordForm == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(recoverPasswordForm, "Failed to validate recover password form");

        final User user = userService.findByMail(recoverPasswordForm.getMail());
        if(user == null)
            throw new InvalidUserException();

        ms.sendRecoverEmail(user,messageSource.getMessage("recoverMailMessage",null,locale),
                messageSource.getMessage("recoverMailSubject",null,locale));

        LOGGER.info("User with id {} send token to reset password", user.getId());
        return Response.noContent().build();
    }

    @PUT
    @Path("/recover_password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response recoverPassword(final FormRecoverPassword recoverPasswordForm) throws DTOValidationException, InvalidUserException {
        LOGGER.debug("Accessed recover password");

        // @FormDataParam parameter is optional --> it may be null
        if (recoverPasswordForm == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(recoverPasswordForm, "Failed to validate recover password form");

        final User user = userService.findByMail(recoverPasswordForm.getMail());
        if(user == null)
           throw new InvalidUserException();

        final String token = userService.getResetToken(user);
        if (!recoverPasswordForm.getToken().equals(token))
            return Response.status(ValidationMapper.UNPROCESSABLE_ENTITY).entity(new ExceptionDTO("Incorrect token")).build();

        securityUserService.changePassword(user.getId(), recoverPasswordForm.getNewPassword());
        LOGGER.info("User with id {} reseted his password", user.getId());
        return Response.noContent().build();
    }

    @PUT
    @Path("/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response changePicture(@BeanParam final FormPicture picture) throws DTOValidationException, FileUploadException, InvalidUserException {
        LOGGER.debug("Accessed change picture");

        // @FormDataParam parameter is optional --> it may be null
        if (picture == null)
            return Response.status(Status.BAD_REQUEST).build();

        DTOValidator.validate(picture, "Failed to validate picture");

        final User authenticatedUser = securityUserService.getLoggedInUser();

        if(authenticatedUser == null){
            LOGGER.debug("Failed to change profile picture, user not found");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

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
