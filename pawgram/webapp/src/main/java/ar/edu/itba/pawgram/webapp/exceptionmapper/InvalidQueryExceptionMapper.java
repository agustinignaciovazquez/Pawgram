package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.InvalidQueryException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class InvalidQueryExceptionMapper implements ExceptionMapper<InvalidQueryException> {
    private static Logger LOGGER = LoggerFactory.getLogger(InvalidQueryExceptionMapper.class);

    @Override
    public Response toResponse(final InvalidQueryException exception) {
        LOGGER.debug("Search query exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionDTO("Search query Bad Request")).type(MediaType.APPLICATION_JSON).build();
    }

}
