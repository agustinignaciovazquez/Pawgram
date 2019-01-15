package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.InvalidNotificationException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class InvalidNotificationExceptionMapper implements ExceptionMapper<InvalidNotificationException> {
    private static Logger LOGGER = LoggerFactory.getLogger(InvalidSearchZoneExceptionMapper.class);

    @Override
    public Response toResponse(final InvalidNotificationException exception) {
        LOGGER.debug("Notification mark as seen exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionDTO("Notification Bad Request")).type(MediaType.APPLICATION_JSON).build();
    }

}