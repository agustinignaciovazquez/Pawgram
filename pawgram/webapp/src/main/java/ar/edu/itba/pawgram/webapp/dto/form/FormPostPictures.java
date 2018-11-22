package ar.edu.itba.pawgram.webapp.dto.form;

import ar.edu.itba.pawgram.interfaces.service.PostService;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormPostPictures {
    @Size(max = PostService.MAX_IMAGES)
    @FormDataParam("picture")
    private List<FormDataBodyPart> pictures = Collections.emptyList();

    public FormPostPictures() {}

    public List<FormDataBodyPart> getPictures() {
        return pictures == null ? Collections.emptyList() : pictures;
    }

    public void setPictures(List<FormDataBodyPart> pictures) {
        this.pictures = pictures;
    }

    public List<byte[]> getPicturesBytes() {
        final List<byte[]> picturesBytes = new ArrayList<>();
        getPictures().forEach((pic) -> picturesBytes.add(pic.getValueAs(byte[].class)));
        return picturesBytes;
    }
}
