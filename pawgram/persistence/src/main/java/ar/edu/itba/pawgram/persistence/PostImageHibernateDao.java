package ar.edu.itba.pawgram.persistence;

import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PostImageHibernateDao implements PostImageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public PostImage createPostImage(long postId, String url) {
        final PostImage pi = new PostImage(url,postId);
        em.persist(pi);
        return pi;
    }

    @Override
    public List<PostImage> getImagesIdByPostId(long postId) {
        final TypedQuery<PostImage> query = em.createQuery("from PostImage as pi where pi.post.id = :postId", PostImage.class);
        query.setParameter("postId", postId);

        final List<PostImage> list = query.getResultList();
        return list;
    }

    @Override
    public long getTotalImagesByPostId(long postId) {
        final TypedQuery<Long> query = em.createQuery("select count(*) from PostImage as pi WHERE pi.postId = :postId", Long.class);
        query.setParameter("postId", postId);

        final Long total = query.getSingleResult();
        return total != null ? total : 0;
    }

    @Override
    public PostImage getPostImageById(long postId, long postImageId) {
        PostImage.PostImagePrimaryKeyIds key = new PostImage.PostImagePrimaryKeyIds( postImageId,  postId);
        return em.find(PostImage.class, key);
    }

    @Override
    public boolean deletePostImage(long postId, long postImageId) {
        final PostImage pi = getPostImageById(postId,postImageId);

        if (pi != null)
            em.remove(pi);

        return (pi != null);
    }
}
