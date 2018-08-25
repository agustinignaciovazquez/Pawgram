package ar.edu.itba.pawgram.webapp.form.wrapper;

import ar.edu.itba.pawgram.webapp.form.constraints.FileMediaType;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileImageWrapper {

    @FileMediaType({MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public boolean hasFile() {
        return file != null && file.getSize() > 0;
    }
}
