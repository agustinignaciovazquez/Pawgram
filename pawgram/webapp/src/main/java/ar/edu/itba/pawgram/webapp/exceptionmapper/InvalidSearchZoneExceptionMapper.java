package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.InvalidSearchZoneException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidSearchZoneExceptionMapper implements ExceptionMapper<InvalidSearchZoneException> {
    private static Logger LOGGER = LoggerFactory.getLogger(InvalidSearchZoneExceptionMapper.class);

    @Override
    public Response toResponse(final InvalidSearchZoneException exception) {
        LOGGER.debug("Search Zone create exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionDTO("Search Zone Bad Request")).type(MediaType.APPLICATION_JSON).build();
    }

}