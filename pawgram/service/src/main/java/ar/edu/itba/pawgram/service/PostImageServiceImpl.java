package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.exception.FileException;
import ar.edu.itba.pawgram.interfaces.exception.FileUploadException;
import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.interfaces.service.FileService;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.model.Post;
import ar.edu.itba.pawgram.model.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostImageServiceImpl implements PostImageService {
    @Autowired
    private FileService fileService;
    @Autowired
    private PostImageDao postImageDao;

    private String createPostImage(byte[] raw_image) throws FileUploadException {
        return fileService.createFile(UPLOAD_FOLDER,raw_image);
    }

    @Override
    @Transactional(rollbackFor = FileUploadException.class)
    public List<PostImage> createPostImage(Post post, List<byte[]> raw_images) throws FileUploadException {
        List<PostImage> l = new ArrayList<>();
        List<String> uploadNames = new ArrayList<>();
        //First we upload every image, since exists the possibility of and exception
        for(byte[] raw_image: raw_images){
            uploadNames.add(createPostImage(raw_image));
        }

        //If we get here everything its ok so we start saving the names in the DB
        for(String uploadName: uploadNames){
            l.add(postImageDao.createPostImage(post.getId(),uploadName));
        }

        return l;
    }

    @Override
    public List<PostImage> getImagesIdByPostId(Post post) {
        return postImageDao.getImagesIdByPostId(post.getId());
    }

    @Override
    public byte[] getImage(String filename) throws FileException {
        return fileService.getFile(UPLOAD_FOLDER,filename);
    }

    @Override
    public long getTotalImagesByPostId(long postId) {
        return postImageDao.getTotalImagesByPostId(postId);
    }

    @Override
    public PostImage getPostImageById(long postId, long postImageId) {
        return postImageDao.getPostImageById(postId,postImageId);
    }

    @Override
    public boolean deletePostImage(long postId, long postImageId) {
        return postImageDao.deletePostImage(postId,postImageId);
    }
}
