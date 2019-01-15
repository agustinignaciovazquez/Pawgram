package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.service.MessageService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Message;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.webapp.dto.ChatDTO;
import ar.edu.itba.pawgram.webapp.dto.MessageDTO;
import ar.edu.itba.pawgram.webapp.dto.form.FormMessage;
import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
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

@Path("posts")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class MessagesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaginationLinkFactory linkFactory;

    @Autowired
    private DTOConstraintValidator validator;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/{id}")
    public Response getMessages(@PathParam("id") final long otherId, @DefaultValue("1") @QueryParam("page") int page,
                                @DefaultValue("" + DEFAULT_PAGE_SIZE) @QueryParam("per_page") int pageSize) {
        final User loggedUser = securityUserService.getLoggedInUser();
        final User otherUser = userService.findById(otherId);
        if(loggedUser == null){
            LOGGER.debug("Failed to fetch messages anonymous user ");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(otherUser == null){
            LOGGER.debug("Failed to fetch messages with user {}, user not found", otherId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // Ignoring invalid values, default stays
        page = (page < 1) ? 1 : page;
        pageSize = (pageSize < 1 || pageSize > MAX_PAGE_SIZE) ? DEFAULT_PAGE_SIZE : pageSize;

        LOGGER.debug("Accessing messages list, page: {}, per_page: {}", page, pageSize);

        final long totalMessages = messageService.getTotalMessages(loggedUser,otherUser);
        final long maxPage = messageService.getMaxPage(loggedUser,otherUser,pageSize);
        final List<Message> messages = messageService.getMessages(loggedUser,otherUser,page,pageSize);

        final Map<String, Link> links = linkFactory.createLinks(uriContext, page, maxPage);
        final Link[] linkArray = links.values().toArray(new Link[0]);

        LOGGER.debug("Links: {}", links);
        return Response.ok(new ChatDTO(messages, totalMessages, uriContext.getBaseUri(),
               loggedUser,otherUser)).links(linkArray).build();
    }

    @POST
    @Path("{otherId}/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response commentPost(@PathParam("otherId") final long otherId, final FormMessage formMessage) throws DTOValidationException {
        final User otherUser = userService.findById(otherId);

        if (otherUser == null) {
            LOGGER.debug("Failed to send message to user {}, not found", otherId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final User loggedUser = securityUserService.getLoggedInUser();
        if(loggedUser == null){
            LOGGER.debug("Failed to send message to user {}, anonymous user", otherId);
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        validator.validate(formMessage, "Failed to validate message");

        final Message message = messageService.sendMessage(loggedUser,otherUser,formMessage.getMessage());

        return Response.ok(new MessageDTO(message, uriContext.getBaseUri())).build();
    }
}
