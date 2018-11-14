package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SubscribeService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean subscribePost(long postId, long userId) {
        final Post post = postService.getPlainPostById(postId);
        final User user = userService.findById(userId);

        if (post == null || user == null || post.getOwner().equals(user))
            return false;

        return user.subscribePost(post);
    }

    @Override
    @Transactional
    public boolean unsubscribePost(long postId, long userId) {
        final Post post = postService.getPlainPostById(postId);
        final User user = userService.findById(userId);

        if (post == null || user == null || post.getOwner().equals(user))
            return false;

        return user.unSubscribePost(post);
    }

    @Override
    @Transactional
    public boolean isSubscribedToPost(long postId, long userId) {
        final Post post = postService.getPlainPostById(postId);
        final User user = userService.findById(userId);

        if (post == null || user == null)
            return false;

        return (post.getUserSubscriptions().contains(user));
    }

    @Override
    @Transactional
    public Set<Post> getUserSubscribedPosts(long userId) {
        final User user = userService.findById(userId);
        //Lazy initialization
        user.getPostSubscriptions().size();
        return user.getPostSubscriptions();
    }
}
