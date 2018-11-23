package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.SendMailException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SendMailExceptionMapper implements ExceptionMapper<SendMailException> {
    private static Logger LOGGER = LoggerFactory.getLogger(SendMailExceptionMapper.class);

    @Override
    public Response toResponse(final SendMailException exception) {
        LOGGER.debug("Send mail internal server error\n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionDTO("Send mail internal server error")).type(MediaType.APPLICATION_JSON).build();
    }

}