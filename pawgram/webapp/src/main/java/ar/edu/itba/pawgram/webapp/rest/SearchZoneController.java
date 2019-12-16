package ar.edu.itba.pawgram.webapp.rest;

import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.exception.InvalidSearchZoneException;
import ar.edu.itba.pawgram.interfaces.exception.MaxSearchZoneReachedException;
import ar.edu.itba.pawgram.interfaces.service.SearchZoneService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.SearchZone;
import ar.edu.itba.pawgram.model.User;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.webapp.dto.NotificationListDTO;
import ar.edu.itba.pawgram.webapp.dto.PostDTO;
import ar.edu.itba.pawgram.webapp.dto.SearchZoneDTO;
import ar.edu.itba.pawgram.webapp.dto.SearchZoneListDTO;
import ar.edu.itba.pawgram.webapp.dto.form.FormSearchZone;
import ar.edu.itba.pawgram.webapp.exception.DTOValidationException;
import ar.edu.itba.pawgram.webapp.validators.DTOConstraintValidator;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Optional;

@Path("sz")
@Controller
@Produces(value = {MediaType.APPLICATION_JSON})
public class SearchZoneController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    private SearchZoneService searchZoneService;

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private DTOConstraintValidator validator;

    @Context
    private UriInfo uriContext;

    @GET
    @Path("/{id}")
    public Response getSZById(@PathParam("id") final long id) {
        LOGGER.debug("Accesed getPostById with ID: {}", id);

        final SearchZone sz = searchZoneService.getFullSearchZoneById(id);
        final User user = securityUserService.getLoggedInUser();

        if (sz == null) {
            LOGGER.warn("Searcho zone with ID: {} not found", id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(user == null){
            LOGGER.debug("Failed to fetch sz {}, user not found", id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (!user.equals(sz.getUser())) {
            LOGGER.warn("Forbidden user {} trying to access search zone with id {} he/she is not owner of", user.getId(), id);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        return Response.ok(new SearchZoneDTO(sz, uriContext.getBaseUri(),user)).build();
    }

    @GET
    @Path("/")
    public Response getAllSZ() {
        final User loggedUser = securityUserService.getLoggedInUser();
        if(loggedUser == null){
            LOGGER.debug("Failed to fetch szs from anonymous user ");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        LOGGER.debug("Accessing search zones list");

        final List<SearchZone> szs = searchZoneService.getPlainSearchZonesByUser(loggedUser.getId());
        final long totalNotifications = searchZoneService.getTotalSearchZonesByUser(loggedUser.getId());

        return Response.ok(new SearchZoneListDTO(szs, totalNotifications, uriContext.getBaseUri(),
                loggedUser)).build();
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response createSZ(@FormDataParam("sz") final FormSearchZone formSearchZone) throws DTOValidationException, MaxSearchZoneReachedException, InvalidSearchZoneException {
        final User user = securityUserService.getLoggedInUser();

        // @FormDataParam parameter is optional --> it may be null
        if (formSearchZone == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        if(user == null){
            LOGGER.debug("Failed to create sz, anonymous user");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        validator.validate(formSearchZone, "Failed to validate message");

        SearchZone sz = searchZoneService.createSearchZone(formSearchZone.getLocation(),formSearchZone.getRange(),user);
        return Response.ok(new SearchZoneDTO(sz, uriContext.getBaseUri(),user)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSZ(@PathParam("id") long searchZoneId) {
        final User user = securityUserService.getLoggedInUser();
        final SearchZone sz = searchZoneService.getFullSearchZoneById(searchZoneId);
        if(user == null){
            LOGGER.debug("Failed to delete sz, anonymous user");
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if(sz == null){
            LOGGER.debug("Failed to delete sz {}, sz not found", searchZoneId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!user.equals(sz.getUser())){
            LOGGER.debug("Failed to delete sz {}, user {} is not the owner",searchZoneId,user.getId());
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        searchZoneService.deleteZoneById(searchZoneId);
        return Response.noContent().build();
    }
}
