package ar.edu.itba.pawgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.pawgram.interfaces.UserDao;
import ar.edu.itba.pawgram.interfaces.UserService;
import ar.edu.itba.pawgram.model.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public User findById(long id) {
		return userDao.findById(id);
	}

}
