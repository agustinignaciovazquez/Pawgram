package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FileUploadExceptionMapper implements ExceptionMapper<FileUploadException> {
    private static Logger LOGGER = LoggerFactory.getLogger(FileUploadExceptionMapper.class);

    @Override
    public Response toResponse(final FileUploadException exception) {
        LOGGER.debug("File upload exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).entity(new ExceptionDTO("File upload error")).type(MediaType.APPLICATION_JSON).build();
    }

}
