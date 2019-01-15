package ar.edu.itba.pawgram.webapp.exceptionmapper;

import ar.edu.itba.pawgram.interfaces.exception.MaxSearchZoneReachedException;
import ar.edu.itba.pawgram.webapp.dto.ExceptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MaxSearchZoneReachedExceptionMapper implements ExceptionMapper<MaxSearchZoneReachedException> {
    private static Logger LOGGER = LoggerFactory.getLogger(MaxSearchZoneReachedExceptionMapper.class);

    @Override
    public Response toResponse(final MaxSearchZoneReachedException exception) {
        LOGGER.debug("Search Zone create exception \n Stack trace {}",exception.getMessage());
        return Response.status(Response.Status.FORBIDDEN).entity(new ExceptionDTO("Search Zone Maximum Reached")).type(MediaType.APPLICATION_JSON).build();
    }

}
