package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidPostException;
import ar.edu.itba.pawgram.interfaces.exception.InvalidQueryException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.service.NotificationService;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.service.utils.HaversineDistance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PostImageService postImageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private HaversineDistance haversineDistance;

    @Override
    @Transactional(rollbackFor = {PostCreateException.class,InvalidPostException.class})
    public Post createPost(String title, String description, List<byte[]> raw_images, String contact_phone,
                           Date event_date, Category category, Pet pet,
                           boolean is_male, Location location, User owner) throws PostCreateException, InvalidPostException {
        Date post_date = event_date;
        if(category == Category.ADOPT){
            post_date = new Date();
        }

        if(raw_images != null && raw_images.size() > MAX_IMAGES){
            throw new InvalidPostException();
        }

        Post post = postDao.createPost(title,description,contact_phone,post_date,category,pet,is_male,location,owner);

        List<PostImage> postImages = null;
        if(raw_images != null) {
            try {
                postImages = postImageService.createPostImage(post, raw_images);
            } catch (FileUploadException e) {
                throw new PostCreateException();
            }
        }

        return Post.getBuilderFromPost(post).postImages(postImages).build();
    }

    @Override
    @Transactional(rollbackFor = {PostCreateException.class,InvalidPostException.class})
    public Post modifyPost(long postId, List<byte[]> raw_images, String title, String description, String contact_phone,
                           Date event_date, Category category, Pet pet, Boolean is_male, Location location) throws InvalidPostException, PostCreateException {
        final long totalPostImages = postImageService.getTotalImagesByPostId(postId);

        if(raw_images != null && raw_images.size() > (MAX_IMAGES - totalPostImages)){
            throw new InvalidPostException();
        }

        Post post = postDao.modifyPost(postId,title,description,contact_phone,event_date,category,pet,is_male,location);

        List<PostImage> postImages = null;
        if(raw_images != null) {
            try {
                postImages = postImageService.createPostImage(post, raw_images);
            } catch (FileUploadException e) {
                throw new PostCreateException();
            }
        }
        notificationService.createNotificationsForPost(post,null);
        return Post.getBuilderFromPost(post).postImages(postImages).build();
    }

    @Override
    public List<Post> getPlainPostsPaged(Optional<Location> location, Optional<Category> category, int page, int pageSize) {
        if(category.isPresent()){
            if(location.isPresent()){
                return haversineDistance.setPostsDistance(postDao.getPlainPostsByCategoryRange(location.get(),category.get(),pageSize,(page - 1) * pageSize),location.get());
            }
            return postDao.getPlainPostsByCategoryRange(category.get(),pageSize,(page - 1) * pageSize);
        }
        if(location.isPresent())
            return haversineDistance.setPostsDistance(postDao.getPlainPostsRange(location.get(),pageSize,(page - 1) * pageSize),location.get());
        return postDao.getPlainPostsRange(pageSize,(page - 1) * pageSize);
    }

    @Override
    @Transactional
    public Post getFullPostById(long postId) {
        final Post post = postDao.getFullPostById(postId);
        if(post == null)
            return null;
        return Post.getBuilderFromPost(post).commentFamilies(commentService.getCommentsByPostId(postId)).build();
    }

    @Override
    @Transactional
    public Post getFullPostById(long postId, Location location) {
        final Post post = postDao.getFullPostById(postId);
        if(post == null)
            return null;
        return haversineDistance.setPostDistance(Post.getBuilderFromPost(post).commentFamilies(commentService.getCommentsByPostId(postId)).build(),location);
    }

    @Override
    public Post getPlainPostById(long postId) {
        return postDao.getPlainPostById(postId);
    }

    @Override
    @Transactional
    public List<Post> getPlainPostsPaged(Location location, int range, Optional<Category> category, int page, int pageSize) {
        if(category.isPresent())
            return haversineDistance.setPostsDistance(postDao.getPlainPostsByCategoryRange(location, range, category.get(), pageSize,(page - 1) * pageSize),location);
        return haversineDistance.setPostsDistance(postDao.getPlainPostsRange(location, range, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByKeywordPaged(String keyword, Optional<Location> location, Optional<Category> category, int page, int pageSize) throws InvalidQueryException {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return Collections.emptyList();
        if(category.isPresent()){
            if(location.isPresent()){
                return haversineDistance.setPostsDistance(postDao.getPlainPostsByKeywordRange(validKeywords,location.get(),category.get(),pageSize,(page - 1) * pageSize),location.get());
            }
            return postDao.getPlainPostsByKeywordRange(validKeywords,category.get(),pageSize,(page - 1) * pageSize);
        }
        if(location.isPresent()){
            return haversineDistance.setPostsDistance(postDao.getPlainPostsByKeywordRange(validKeywords, location.get(), pageSize,(page - 1) * pageSize),location.get());
        }
        return postDao.getPlainPostsByKeywordRange(validKeywords,pageSize,(page - 1) * pageSize);
    }

    @Override
    @Transactional
    public boolean deletePostById(long postId) {
        return postDao.deletePostById(postId);
    }

    @Override
    public long getTotalPosts(Optional<Category> category) {
        if(category.isPresent())
            return postDao.getTotalPostsByCategory(category.get());
        return postDao.getTotalPosts();
    }

    @Override
    public long getTotalPosts(Location location, int range, Optional<Category> category) {
        if(category.isPresent())
            return postDao.getTotalPostsByCategory(location,range, category.get());
        return postDao.getTotalPosts(location,range);
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Optional<Category> category) throws InvalidQueryException {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return 0;
        if(category.isPresent())
            return postDao.getTotalPostsByKeyword(validKeywords, category.get());
        return postDao.getTotalPostsByKeyword(validKeywords);
    }

    @Override
    public long getMaxPage(int pageSize, Optional<Category> category) {
        long total = getTotalPosts(category);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPage(int pageSize, Location location, int range, Optional<Category> category) {
        long total = getTotalPosts(location, range, category);
        return (long) Math.ceil((float) total / pageSize);
    }

    @Override
    public long getMaxPageByKeyword(int pageSize, String keyword, Optional<Category> category) throws InvalidQueryException {
        long total = getTotalPostsByKeyword(keyword,category);
        return (long) Math.ceil((float) total / pageSize);
    }

    private Set<String> getValidKeywords(String keyword) throws InvalidQueryException {
        if(keyword.length() > MAX_WORD_SIZE)
            throw new InvalidQueryException();
        if(keyword.length() < MIN_WORD_SIZE)
            throw new InvalidQueryException();

        final String[] keywords = keyword.trim().split(" ");
        final Set<String> validKeywords = new HashSet<>();

        for (final String word : keywords) {
            if (word.length() >= MIN_WORD_SIZE && StringUtils.isAlphanumeric(word))
                validKeywords.add(word);
        }

        return validKeywords;
    }
}
