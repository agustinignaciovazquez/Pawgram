package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	@Autowired
	private FileService fileService;

	@Override
	@Transactional // In order to fetch lazy attributes
	public User findById(long id) {
		return userDao.findById(id);
	}

	@Override
	@Transactional(rollbackFor = DuplicateEmailException.class)
	public User create(String name, String surname, String mail, String password, String profile_url) throws DuplicateEmailException {
		return userDao.create(name, surname, mail, password,profile_url);
	}

	@Override
	@Transactional
	public User changePassword(long id, String password) {
		return  userDao.changePassword(id, password);
	}

	@Override
	@Transactional
	public User changeName(long id, String name, String surname) {
		return userDao.changeName(id, name, surname);
	}

	@Override
	@Transactional
	public User changeProfile(long id, byte[] data) throws FileUploadException {
		final String img_url = fileService.createFile(PROFILE_IMAGE_UPLOAD_FOLDER,data);
		return userDao.changeProfile(id,img_url);
	}

	@Override
	public byte[] getProfileImage(String filename) throws FileException {
		return fileService.getFile(PROFILE_IMAGE_UPLOAD_FOLDER,filename);
	}

	@Override
	public String getResetToken(User user) {
		StringBuilder token = new StringBuilder(user.getPassword());
		token.append("SOME_SECRET_HASH_TO_DIGEST");
		token.append(user.getMail());
		return DigestUtils.md5DigestAsHex(token.toString().getBytes());
	}

	@Override
	public User findByMail(String mail) {
		return userDao.findByMail(mail);
	}

}
