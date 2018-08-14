package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.User;

public interface UserService {
	public User findById(final long id);
}
