package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.interfaces.service.FileService;
import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.service.utils.HaversineDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service

public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	@Autowired
	private FileService fileService;
	@Autowired
	private HaversineDistance haversineDistance;

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
	public User changePassword(long id, String password) throws InvalidUserException {
		return  userDao.changePassword(id, password);
	}

	@Override
	@Transactional
	public User changeName(long id, String name, String surname) throws InvalidUserException {
		return userDao.changeName(id, name, surname);
	}

	@Override
	@Transactional
	public User changeProfile(long id, byte[] data) throws FileUploadException, InvalidUserException {
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

	@Override
	public List<Post> getSubscribedPostsPaged(long userId, int page, int pageSize){
		return userDao.getSubscribedPostsRange(userId,pageSize,(page - 1) * pageSize);
	}

	@Override
	public long getTotalSubscriptionsByUserId(long userId){
		return userDao.getTotalSubscriptionsByUserId(userId);
	}

	@Override
	public long getMaxSubscribedPageWithSize(long userId, int pageSize) {
		long total = getTotalSubscriptionsByUserId(userId);
		return (long) Math.ceil((float) total / pageSize);
	}

	@Override
	public List<Post> getPlainPostsByUserIdPaged(long userId, Optional<Location> location, Optional<Category> category, int page, int pageSize) {
		if(category.isPresent()) {
			if (location.isPresent()) {
				return haversineDistance.setPostsDistance(userDao.getPlainPostsByUserIdRange(userId, location.get(), category.get(), pageSize, (page - 1) * pageSize), location.get());
			}
			return userDao.getPlainPostsByUserIdRange(userId, category.get(), pageSize, (page - 1) * pageSize);
		}
		if(location.isPresent()) {
			return haversineDistance.setPostsDistance(userDao.getPlainPostsByUserIdRange(userId, location.get(), pageSize, (page - 1) * pageSize), location.get());
		}
		return userDao.getPlainPostsByUserIdRange(userId,pageSize,(page - 1) * pageSize);
	}

	@Override
	public long getTotalPostsByUserId(long userId, Optional<Category> category) {
		if(category.isPresent()) {
			return userDao.getTotalPostsByUserId(userId, category.get());
		}
		return userDao.getTotalPostsByUserId(userId);
	}

	@Override
	public long getMaxPageByUserId(int pageSize, long userId, Optional<Category> category) {
		final long total = getTotalPostsByUserId(userId, category);
		return (long) Math.ceil((float) total / pageSize);
	}

}
