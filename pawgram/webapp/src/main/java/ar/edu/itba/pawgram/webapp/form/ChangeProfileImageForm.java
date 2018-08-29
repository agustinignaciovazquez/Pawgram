package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.webapp.form.constraints.FileMediaType;
import ar.edu.itba.pawgram.webapp.form.constraints.FileSize;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class ChangeProfileImageForm {
    @FileMediaType({MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @FileSize(min = 1)
    private MultipartFile profilePicture;

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }
}
