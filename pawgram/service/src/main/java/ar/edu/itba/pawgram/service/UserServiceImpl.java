package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public User findById(long id) {
		return userDao.findById(id);
	}

	@Override
	public User create(String name, String surname, String mail, String password, String profile_url) throws DuplicateEmailException {
		return userDao.create(name, surname, mail, password,profile_url);
	}

	@Override
	public User changePassword(long id, String password) {
		return  userDao.changePassword(id, password);
	}

	@Override
	public User findByMail(String mail) {
		return userDao.findByMail(mail);
	}

}
