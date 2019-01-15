package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.exception.InvalidNotificationException;
import ar.edu.itba.pawgram.interfaces.service.MessageService;
import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.dto.NotificationDTO;
import ar.edu.itba.pawgram.webapp.dto.NotificationListDTO;
import ar.edu.itba.pawgram.webapp.dto.PostListDTO;
import ar.edu.itba.pawgram.webapp.utils.PaginationLinkFactory;
import ar.edu.itba.pawgram.webapp.validators.DTOConstraintValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("notifications")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class NotificationsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsController.class);
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private PaginationLinkFactory linkFactory;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/{id}")
    public Response getNotificationById(@PathParam("id") final long id) throws InvalidNotificationException {
        LOGGER.debug("Accesed getPostById with ID: {}", id);
        final User loggedUser = securityUserService.getLoggedInUser();
        if(loggedUser == null){
            LOGGER.debug("Failed to fetch notifications from anonymous user ");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final Notification notification = notificationService.getFullNotificationById(id);
        if (notification == null) {
            LOGGER.warn("Notification with ID: {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!notification.getUser().equals(loggedUser)){
            LOGGER.warn("Forbidden user {} trying to access notification with id {} he/she is not owner of", loggedUser.getId(), id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        //mark notification as seen
        notificationService.markNotificationAsSeen(id);
        return Response.ok(new NotificationDTO(notification, uriContext.getBaseUri(), Optional.ofNullable(loggedUser))).build();
    }

    @GET
    @Path("/")
    public Response getUnseenNotifications(@DefaultValue("1") @QueryParam("page") int page,
                                     @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {
       return getNotifications(false,page,pageSize);
    }

    @GET
    @Path("/all")
    public Response getAllNotifications(@DefaultValue("1") @QueryParam("page") int page,
                                     @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {
        return getNotifications(true,page,pageSize);
    }

    private Response getNotifications(boolean include_seen, @DefaultValue("1") @QueryParam("page") int page,
                                     @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {
        final User loggedUser = securityUserService.getLoggedInUser();
        if(loggedUser == null){
            LOGGER.debug("Failed to fetch notifications from anonymous user ");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // Ignoring invalid values, default stays
        page = (page < 1) ? 1 : page;
        pageSize = (pageSize < 1 || pageSize > MAX_PAGE_SIZE) ? DEFAULT_PAGE_SIZE : pageSize;

        LOGGER.debug("Accessing notifications list, page: {}, per_page: {}", page, pageSize);

        final long totalNotifications = notificationService.getTotalNotificationByUser(loggedUser,include_seen);
        final long maxPage = notificationService.getMaxPageNotificationByUser(loggedUser,include_seen,pageSize);
        final List<Notification> notifications = notificationService.getNotifications(loggedUser,include_seen,page,pageSize);

        final Map<String, Link> links = linkFactory.createLinks(uriContext, page, maxPage);
        final Link[] linkArray = links.values().toArray(new Link[0]);

        LOGGER.debug("Links: {}", links);
        return Response.ok(new NotificationListDTO(notifications, totalNotifications, uriContext.getBaseUri(),
                Optional.ofNullable(loggedUser))).links(linkArray).build();
    }
}
