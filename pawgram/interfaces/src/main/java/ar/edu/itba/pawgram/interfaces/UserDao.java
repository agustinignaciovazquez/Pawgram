package ar.edu.itba.pawgram.interfaces;

import ar.edu.itba.pawgram.model.User;

public interface UserDao {
	
	/**
	* Find an existing user by id
	*
	* @param id of the user
	* @return user if id is correct null otherwise
	*/
	public User findById(final long id);
	
	/**
	* Find an existing user by e-mail
	*
	* @param mail of the user
	* @return user if id is correct null otherwise
	*/
	public User findByMail(final String mail);
	
	/**
	* Create a new user.
	*
	* @param username The name of the user.
	* @return The created user.
	*/
	public User create(String name, String surname, String mail, String password);

}
