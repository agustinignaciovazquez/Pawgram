package ar.edu.itba.pawgram.webapp.controller.advice;

import ar.edu.itba.pawgram.interfaces.service.SecurityUserService;
import ar.edu.itba.pawgram.webapp.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorControllerAdvice {

    @Autowired
    private SecurityUserService securityUserService;

    private ModelAndView buildModelAndView(String jspName) {
        ModelAndView mav = new ModelAndView(jspName);
        mav.addObject("loggedUser", securityUserService.getLoggedInUser());
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ModelAndView resourceNotFound() {
        return buildModelAndView("error/404");
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView productNotFound() {
        return buildModelAndView("error/404post");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound() {
        return buildModelAndView("error/404user");
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ModelAndView imageNotFound() {
        return buildModelAndView("error/404image");
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value=HttpStatus.UNAUTHORIZED)
    public ModelAndView unauthorized() {
        return buildModelAndView("error/401");
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(value=HttpStatus.FORBIDDEN)
    public ModelAndView Forbidden() {
        return buildModelAndView("error/403");
    }

    @ExceptionHandler(InvalidQueryException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ModelAndView invalidQuery() {
        return buildModelAndView("error/400badQuery");
    }
}
