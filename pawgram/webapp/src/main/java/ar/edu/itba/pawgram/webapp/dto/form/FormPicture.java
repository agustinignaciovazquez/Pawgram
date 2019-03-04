package ar.edu.itba.pawgram.webapp.dto.form;

import ar.edu.itba.pawgram.webapp.form.constraints.FileMediaType;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.validation.constraints.NotNull;

public class FormPicture {

    @FileMediaType(value = { "image/jpeg", "image/png" })
    @FormDataParam("picture")
    private FormDataBodyPart pictureBodyPart;

    public FormPicture() {}

    public FormPicture(final FormDataBodyPart pictureBodyPart) {
        this.pictureBodyPart = pictureBodyPart;
    }

    public FormDataBodyPart getPictureBodyPart() {
        return pictureBodyPart;
    }

    public boolean isPicture(){
        return (pictureBodyPart != null);
    }

    public void setPictureBodyPart(final FormDataBodyPart pictureBodyPart) {
        this.pictureBodyPart = pictureBodyPart;
    }

    public byte[] getPictureBytes() {
        return getPictureBodyPart().getValueAs(byte[].class);
    }
}
