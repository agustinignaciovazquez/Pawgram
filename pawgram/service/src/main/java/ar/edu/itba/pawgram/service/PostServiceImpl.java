package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.CommentDao;
import ar.edu.itba.pawgram.interfaces.CommentService;
import ar.edu.itba.pawgram.interfaces.PostDao;
import ar.edu.itba.pawgram.interfaces.PostService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentService commentService;

    @Override
    public Post createPost(String title, String description, String img_url, String contact_phone,
                           LocalDateTime event_date, Category category, Pet pet,
                           boolean is_male, Location location, User owner) {
        return postDao.createPost(title,description,img_url,contact_phone,event_date,category,pet,is_male,location,owner).build();
    }

    @Override
    public List<PlainPost> getPlainPosts(Location location, int range) {
        return postDao.getPlainPosts(location,range);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategory(Location location, int range, Category category) {
        return postDao.getPlainPostsByCategory(location,range,category);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeyword(String keyword, Location location) {
        return postDao.getPlainPostsByKeyword(keyword,location);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserId(long userId, Location location) {
        return postDao.getPlainPostsByUserId(userId, location);
    }

    @Override
    public Post getFullPostById(long postId, Location location) {
        Post.PostBuilder pb = postDao.getFullPostById(postId, location);
        if(pb == null)
            return null;
        return pb.commentFamilies(commentService.getCommentsByPostId(postId)).build();
    }

    @Override
    public PlainPost getPlainPostById(long postId) {
        return postDao.getPlainPostById(postId);
    }

    @Override
    public List<PlainPost> getPlainPostsPaged(Location location, int range, int page, int pageSize) {
        return postDao.getPlainPostsRange(location, range, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryPaged(Location location, int range, Category category, int page, int pageSize) {
        return postDao.getPlainPostsByCategoryRange(location, range, category, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordPaged(String keyword, Location location, int page, int pageSize) {
        return postDao.getPlainPostsByKeywordRange(keyword, location, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdPaged(long userId, Location location, int page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId, location, pageSize,(page - 1) * pageSize);
    }

    @Override
    public boolean deletePostById(long postId, User user) {
        return postDao.deletePostById(postId,user);
    }

    @Override
    public long getTotalPosts() {
        return postDao.getTotalPosts();
    }

    @Override
    public long getTotalPostsByCategory(Category category) {
        return postDao.getTotalPostsByCategory(category);
    }

    @Override
    public long getTotalPosts(Location location, int range) {
        return postDao.getTotalPosts(location,range);
    }

    @Override
    public long getTotalPostsByCategory(Location location, int range, Category category) {
        return postDao.getTotalPostsByCategory(location,range,category);
    }
}
