package ar.edu.itba.pawgram.webapp.dto;

import ar.edu.itba.pawgram.model.Notification;
import ar.edu.itba.pawgram.model.PostImage;
import ar.edu.itba.pawgram.model.User;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PostImageListDTO {
    private List<PostImageDTO> postImages;
    private long totalCount;
    private long count;

    public PostImageListDTO() {}

    public PostImageListDTO(final List<PostImage> pis, long totalCount, final URI baseUri) {
        this.setTotalCount(totalCount);
        this.postImages = new LinkedList<>();
        this.setCount(pis.size());

        for (PostImage pi : pis)
            this.postImages.add(new PostImageDTO(pi,baseUri));
    }

    public List<PostImageDTO> getPostImages() {
        return postImages;
    }

    public void setPostImages(List<PostImageDTO> postImages) {
        this.postImages = postImages;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
