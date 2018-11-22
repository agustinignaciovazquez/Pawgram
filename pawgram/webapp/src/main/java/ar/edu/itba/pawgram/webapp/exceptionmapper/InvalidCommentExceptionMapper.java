package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.InvalidCommentException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidCommentExceptionMapper implements ExceptionMapper<InvalidCommentException> {
    private static Logger LOGGER = LoggerFactory.getLogger(InvalidCommentExceptionMapper.class);

    @Override
    public Response toResponse(final InvalidCommentException exception) {
        LOGGER.debug("Comment exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(new ExceptionDTO("Invalid comment")).type(MediaType.APPLICATION_JSON).build();
    }

}
