package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class PostCreateExceptionMapper implements ExceptionMapper<PostCreateException> {
    private static Logger LOGGER = LoggerFactory.getLogger(PostCreateExceptionMapper.class);

    @Override
    public Response toResponse(final PostCreateException exception) {
        LOGGER.debug("Post upload exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ExceptionDTO("Post upload error")).type(MediaType.APPLICATION_JSON).build();
    }

}
