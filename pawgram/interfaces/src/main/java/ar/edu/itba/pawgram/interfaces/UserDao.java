package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.User;

public interface UserDao {
	public User findById(final long id);
}
