package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileExceptionMapper implements ExceptionMapper<FileException> {
    private static Logger LOGGER = LoggerFactory.getLogger(DuplicateEmailMapper.class);

    @Override
    public Response toResponse(final FileException exception) {
        LOGGER.debug("File exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(new ExceptionDTO("File not found")).type(MediaType.APPLICATION_JSON).build();
    }

}