package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.User;

public interface UserService {
	/**
	* Find an existing user by id
	*
	* @param id of the user
	* @return user if id is correct null otherwise
	*/
	public User findById(final long id);
	
	/**
	* Create a new user.
	*
	* @param name The name of the user.
	* @param surname The surname of the user.
	* @param mail The mail of the user.
	* @param password The password of the user.
	* @return The created user.
	*/
	public User create(String name, String surname, String mail, String password);
}
