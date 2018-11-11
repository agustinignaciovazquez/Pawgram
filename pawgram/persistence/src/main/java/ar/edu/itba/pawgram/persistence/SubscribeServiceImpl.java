package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.interfaces.service.SubscribeService;
import ar.edu.itba.pawgram.interfaces.service.UserService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SubscribeServiceImpl implements SubscribeService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Override
    public boolean subscribePost(long postId, long userId) {
        final Post post = postService.getPlainPostById(postId);
        final User user = userService.findById(userId);

        if (post == null || user == null)
            return false;

        return user.subscribePost(post);
    }

    @Override
    public boolean unsubscribePost(long postId, long userId) {
        final Post post = postService.getPlainPostById(postId);
        final User user = userService.findById(userId);

        if (post == null || user == null)
            return false;

        return user.unSubscribePost(post);
    }
}
