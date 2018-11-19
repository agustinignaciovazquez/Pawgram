package ar.edu.itba.pawgram.webapp.auth;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.interfaces.auth.SecurityUserService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PawgramUserService implements SecurityUserService {
    @Autowired
    private UserService userService;

    @Override
    public User getLoggedInUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null)
            return null;

        final String email = auth.getName();
        return userService.findByMail(email);
    }

    @Override
    public User registerUser(String name, String surname, String mail, String password, String profile_url)
            throws DuplicateEmailException {
        return userService.create(name, surname, mail, password,profile_url);
    }

    @Override
    public User changePassword(long userId, String password) throws InvalidUserException {
        return userService.changePassword(userId,password);
    }
}
