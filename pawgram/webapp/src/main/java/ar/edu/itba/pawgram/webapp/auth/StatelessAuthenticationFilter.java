package ar.edu.itba.pawgram.webapp.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class StatelessAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final Authentication auth = tokenAuthenticationService.getAuthentication((HttpServletRequest) request);

        if (auth != null)
            SecurityContextHolder.getContext().setAuthentication(auth);	// Log in with user corresponding to the token in the request

        chain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);		// Log out
    }
}