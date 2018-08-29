package ar.edu.itba.pawgram.service;

import ar.edu.itba.pawgram.interfaces.persistence.PostImageDao;
import ar.edu.itba.pawgram.interfaces.service.FileService;
import ar.edu.itba.pawgram.interfaces.service.PostImageService;
import ar.edu.itba.pawgram.model.PostImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostImageServiceImpl implements PostImageService {
    @Autowired
    private FileService fileService;
    @Autowired
    private PostImageDao postImageDao;

    @Override
    public String createPostImage(long postId, byte[] raw_image) throws IOException {
        return fileService.createFile(UPLOAD_FOLDER,raw_image);
    }

    @Override
    public List<PostImage> createPostImage(long postId, List<byte[]> raw_images) throws IOException {
        List<PostImage> l = new ArrayList<>();
        List<String> uploadNames = new ArrayList<>();
        //First we upload every image, since exists the possibility of and exception
        for(byte[] raw_image: raw_images){
            uploadNames.add(createPostImage(postId,raw_image));
        }

        //If we get here everything its ok so we start saving the names in the DB
        for(String uploadName: uploadNames){
            l.add(postImageDao.createPostImage(postId,uploadName));
        }

        return l;
    }

    @Override
    public List<PostImage> getImagesIdByPostId(long postId) {
        return postImageDao.getImagesIdByPostId(postId);
    }
}
