package ar.edu.itba.pawgram.webapp.exceptionmapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class DuplicateEmailMapper implements ExceptionMapper<DuplicateEmailException> {
    private static Logger LOGGER = LoggerFactory.getLogger(DuplicateEmailMapper.class);

    @Override
    public Response toResponse(final DuplicateEmailException exception) {
        LOGGER.debug("Dulpicate email exception mapper handling exception");
        return Response.status(Status.CONFLICT).entity(new ExceptionDTO("Duplicate user email")).type(MediaType.APPLICATION_JSON).build();
    }

}
