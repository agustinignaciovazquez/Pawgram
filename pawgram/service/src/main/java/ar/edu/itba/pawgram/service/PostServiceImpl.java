package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.exception.PostCreateException;
import ar.edu.itba.pawgram.interfaces.service.CommentService;
import ar.edu.itba.pawgram.interfaces.persistence.PostDao;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.interfaces.service.PostService;
import ar.edu.itba.pawgram.model.*;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.service.utils.HaversineDistance;
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
    private HaversineDistance haversineDistance;

    private final static int MIN_WORD_SIZE = 3;

    @Override
    @Transactional(rollbackFor = PostCreateException.class)
    public Post createPost(String title, String description, List<byte[]> raw_images, String contact_phone,
                           Date event_date, Category category, Pet pet,
                           boolean is_male, Location location, User owner) throws PostCreateException {
        Post post = postDao.createPost(title,description,raw_images,contact_phone,event_date,category,pet,is_male,location,owner);
        List<PostImage> postImages = null;
        if(raw_images != null) {
            try {
                postImages = postImageService.createPostImage(post, raw_images);
            } catch (FileUploadException e) {
                //e.printStackTrace(); DEBUG ONLY
                throw new PostCreateException();
            }
        }
        return setPostDistance(Post.getBuilderFromProduct(post).postImages(postImages).build(),location);
    }

    @Override
    public List<Post> getPlainPostsPaged(int page, int pageSize) {
        return postDao.getPlainPostsRange(pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsPaged(Location location, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsRange(location,pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByCategoryPaged(Category category, int page, int pageSize) {
        return postDao.getPlainPostsByCategoryRange(category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsByCategoryPaged(Location location, Category category, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsByCategoryRange(location,category,pageSize,(page - 1) * pageSize),location);
    }

    @Override
    @Transactional
    public Post getFullPostById(long postId) {
        final Post post = postDao.getFullPostById(postId);
        if(post == null)
            return null;
        return Post.getBuilderFromProduct(post).commentFamilies(commentService.getCommentsByPostId(postId)).build();
    }

    @Override
    @Transactional
    public Post getFullPostById(long postId, Location location) {
        final Post post = postDao.getFullPostById(postId);
        if(post == null)
            return null;
        return setPostDistance(Post.getBuilderFromProduct(post).commentFamilies(commentService.getCommentsByPostId(postId)).build(),location);
    }

    @Override
    public Post getPlainPostById(long postId) {
        return postDao.getPlainPostById(postId);
    }

    @Override
    @Transactional
    public List<Post> getPlainPostsPaged(Location location, int range, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsRange(location, range, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByCategoryPaged(Location location, int range, Category category, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsByCategoryRange(location, range, category, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByKeywordPaged(String keyword, int page, int pageSize) {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return Collections.emptyList();
        return postDao.getPlainPostsByKeywordRange(validKeywords,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsByKeywordPaged(String keyword, Location location, int page, int pageSize) {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return Collections.emptyList();
        return setPostsDistance(postDao.getPlainPostsByKeywordRange(validKeywords, location, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByKeywordPaged(String keyword, Category category, int page, int pageSize) {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return Collections.emptyList();
        return postDao.getPlainPostsByKeywordRange(validKeywords,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsByKeywordPaged(String keyword, Location location, Category category, int page, int pageSize) {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return Collections.emptyList();
        return setPostsDistance(postDao.getPlainPostsByKeywordRange(validKeywords,location,category,pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByUserIdPaged(long userId, int page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsByUserIdPaged(long userId, Location location, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsByUserIdRange(userId, location, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    public List<Post> getPlainPostsByUserIdPaged(long userId, Category category, int page, int pageSize) {
        return postDao.getPlainPostsByUserIdRange(userId,category,pageSize,(page - 1) * pageSize);
    }

    @Override
    public List<Post> getPlainPostsByUserIdPaged(long userId, Location location, Category category, int page, int pageSize) {
        return setPostsDistance(postDao.getPlainPostsByUserIdRange(userId, location, category, pageSize,(page - 1) * pageSize),location);
    }

    @Override
    @Transactional
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
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return 0;
        return postDao.getTotalPostsByKeyword(validKeywords);
    }

    @Override
    public long getTotalPostsByKeyword(String keyword, Category category) {
        Set<String> validKeywords = getValidKeywords(keyword);
        if (validKeywords.isEmpty())
            return 0;
        return postDao.getTotalPostsByKeyword(validKeywords,category);
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

    private Post setPostDistance(Post p, Location currentLocation){
        Double distance = haversineDistance.distance(currentLocation.getLatitude(),currentLocation.getLongitude(),
                p.getLocation().getLatitude(),p.getLocation().getLongitude());
        p.setDistance(distance.intValue());
        return p;
    }

    private List<Post> setPostsDistance(List<Post> posts, Location currentLocation){
        for(Post p: posts){
            setPostDistance(p,currentLocation);
        }
        return posts;
    }

    private Set<String> getValidKeywords(String keyword){
        final String[] keywords = keyword.trim().split(" ");
        final Set<String> validKeywords = new HashSet<>();

        for (final String word : keywords) {
            if (word.length() >= MIN_WORD_SIZE)
                validKeywords.add(word);
        }

        return validKeywords;
    }
}
