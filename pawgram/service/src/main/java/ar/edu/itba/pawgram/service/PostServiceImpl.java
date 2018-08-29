package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.interfaces.PlainPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentService commentService;

    @Override
    @Transactional(rollbackFor = PostCreateException.class)
    public Post createPost(String title, String description, List<byte[]> raw_images, String contact_phone,
                           LocalDateTime event_date, Category category, Pet pet,
                           boolean is_male, Location location, User owner) throws PostCreateException {
        return postDao.createPost(title,description,raw_images,contact_phone,event_date,category,pet,is_male,location,owner).build();
    }

    @Override
    public List<PlainPost> getPlainPostsPaged(long page, int pageSize) {
        return postDao.getPlainPostsRange(pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsPaged(Location location, long page, int pageSize) {
        return postDao.getPlainPostsRange(location,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryPaged(Category category, long page, int pageSize) {
        return postDao.getPlainPostsByCategoryRange(category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryPaged(Location location, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByCategoryRange(location,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    @Transactional
    public Post getFullPostById(long postId) {
        Post.PostBuilder pb = postDao.getFullPostById(postId);
        if(pb == null)
            return null;
        return pb.commentFamilies(commentService.getCommentsByPostId(postId)).build();
    }

    @Override
    @Transactional
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
    public List<PlainPost> getPlainPostsPaged(Location location, int range, long page, int pageSize) {
        return postDao.getPlainPostsRange(location, range, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByCategoryPaged(Location location, int range, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByCategoryRange(location, range, category, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordPaged(String keyword, long page, int pageSize) {
        return postDao.getPlainPostsByKeywordRange(keyword,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordPaged(String keyword, Location location, long page, int pageSize) {
        return postDao.getPlainPostsByKeywordRange(keyword, location, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordPaged(String keyword, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByKeywordRange(keyword,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByKeywordPaged(String keyword, Location location, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByKeywordRange(keyword,location,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdPaged(long userId, long page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdPaged(long userId, Location location, long page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId, location, pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdPaged(long userId, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<PlainPost> getPlainPostsByUserIdPaged(long userId, Location location, Category category, long page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId, location, category, pageSize,(page - 1) * pageSize);
    }

    @Override
    public boolean deletePostById(long postId) {
        return postDao.deletePostById(postId);
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

    @Override
    public long getTotalPostsByKeyword(String keyword) {
        return postDao.getTotalPostsByKeyword(keyword);
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Category category) {
        return postDao.getTotalPostsByKeyword(keyword,category);
    }

    @Override
    public long getTotalPostsByUserId(long userId) {
        return postDao.getTotalPostsByUserId(userId);
    }

    @Override
    public long getTotalPostsByUserId(long userId, Category category) {
        return postDao.getTotalPostsByUserId(userId, category);
    }

    @Override
    public long getMaxPage(int pageSize) {
        long total = getTotalPosts();
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPage(int pageSize, Location location, int range) {
        long total = getTotalPosts(location, range);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByCategory(int pageSize, Category category) {
        long total = getTotalPostsByCategory(category);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByCategory(int pageSize, Location location, int range, Category category) {
        long total = getTotalPostsByCategory(location, range, category);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByKeyword(int pageSize, String keyword) {
        long total = getTotalPostsByKeyword(keyword);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByKeyword(int pageSize, String keyword, Category category) {
        long total = getTotalPostsByKeyword(keyword,category);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByUserId(int pageSize, long userId) {
        long total = getTotalPostsByUserId(userId);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByUserId(int pageSize, long userId, Category category) {
        long total = getTotalPostsByUserId(userId,category);
        return (long) Math.ceil((float) total / pageSize);
    }
}
