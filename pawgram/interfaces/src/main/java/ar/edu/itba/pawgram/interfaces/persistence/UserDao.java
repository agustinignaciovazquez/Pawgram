package ar.edu.itba.pawgram.interfaces.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.model.User;

public interface UserDao {
	
	/**
	* Find an existing [@link User] by id
	*
	* @param id of the user
	* @return user if id is correct null otherwise
	*/
	public User findById(final long id);
	
	/**
	* Find an existing [@link User] by e-mail
	*
	* @param mail of the user
	* @return user if id is correct null otherwise
	*/
	public User findByMail(final String mail);
	
	 /**
	 * Create a new [@link User].
	 *
	 * @param name The name of the user.
	 * @param surname The surname of the user.
	  * @param mail The mail of the user.
	  * @param password the password of the user.
	 * @return The created user.
	  * @throws DuplicateEmailException - if user with given exception already exists
	 */
	public User create(final String name, final String surname, final String mail, final String password) throws DuplicateEmailException;

	/**
	 * Changes an [@link User] password
	 * @param id - ID of the user
	 * @param password - New user's password
	 * @return The user with the modified password or null if user doesn't exist
	 */
	public User changePassword(final long id,final String password);

}
