package ar.edu.itba.pawgram.interfaces.service;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.model.User;

public interface SecurityUserService {
    public User getLoggedInUser();
    public User registerUser(final String name, final String surname, final String mail, final String password) throws DuplicateEmailException, DuplicateEmailException;
    public User changePassword(int userId, String password);
}
