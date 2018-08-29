package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.model.User;

public interface SecurityUserService {
    public User getLoggedInUser();
    public User registerUser(final String name, final String surname, final String mail,
                             final String password, final String profile_url) throws DuplicateEmailException;
    public User changePassword(final long userId, final String password);
}
