package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.exception.DuplicateEmailException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidUserException;
import ar.edu.itba.pawgram.interfaces.persistence.UserDao;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@ComponentScan("ar.edu.itba.pawgram.webapp.config")
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findById(long id) {
        final User user = getPlainUserById(id);
        //if we later need some lazy fetchs

        return user;
    }

    private User getPlainUserById(final long userId) {
        return em.find(User.class, userId);
    }

    @Override
    public User findByMail(String mail) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.mail = :mail", User.class);
        query.setParameter("mail", mail);

        final List<User> list = query.getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User create(String name, String surname, String mail, String password, String profile_url) throws DuplicateEmailException {
        final String enc_password = bCryptPasswordEncoder.encode(password);
        final User user = new User(name,surname,mail,enc_password,profile_url);
        try {
            em.persist(user);
            em.flush();   // In order to be able to catch exception
            return user;
        } catch (PersistenceException e) {
            throw new DuplicateEmailException("There already exists an user with mail: " + mail);
        }
    }

    @Override
    public User changePassword(long id, String password) throws InvalidUserException {
        final User user = getPlainUserById(id);

        if (user == null)
            throw new InvalidUserException();

        String enc_password = bCryptPasswordEncoder.encode(password);
        user.setPassword(enc_password);
        return user;
    }

    @Override
    public User changeName(long id, String name, String surname) throws InvalidUserException {
        final User user = getPlainUserById(id);
        if(user == null)
            throw new InvalidUserException();

        user.setName(name);
        user.setSurname(surname);
        return user;
    }

    @Override
    public User changeProfile(long id, String img_url) throws InvalidUserException {
        final User user = getPlainUserById(id);

        if (user == null)
            throw new InvalidUserException();

        user.setProfile_img_url(img_url);
        return user;
    }

    public String getProfilePictureByUserId(long id) {
        String url = null;
        final User user = getPlainUserById(id);

        if (user != null)
            url = user.getProfile_img_url();

        return url;
    }
}
