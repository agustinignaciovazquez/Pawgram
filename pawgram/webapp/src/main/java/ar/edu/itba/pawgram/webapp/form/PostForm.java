package ar.edu.itba.pawgram.webapp.form;

import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.webapp.form.wrapper.MultipartFileImageWrapper;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PostForm {
    private static final int MAX_IMAGES = 4;

    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @Valid
    private MultipartFileImageWrapper[] images;

    @Size(max = 32)
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")//regex phone number
    private String contact_phone;

    @NotBlank
    @DateTimeFormat(pattern="yyyy-MM-dd")//check this later
    private String event_date;

    //private Category category = Category.LOST;
    @NotNull
    private Pet pet;

    @NotNull
    private Boolean is_male;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;

    public PostForm() {
        images = new MultipartFileImageWrapper[MAX_IMAGES];
    }
    public List<byte[]> getAllRawImages() throws IOException {
        List<byte[]> l = new ArrayList<>();
        for(MultipartFileImageWrapper image: images){
            if(image != null && image.hasFile())
                l.add(image.getFile().getBytes());
        }
        return l;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFileImageWrapper[] getImages() { return images; }

    public void setImage(MultipartFileImageWrapper[] images) { this.images = images; }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    /*public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }*/

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public Boolean getIs_male() {
        return is_male;
    }

    public void setIs_male(Boolean is_male) {
        this.is_male = is_male;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Location getLocation(){ return new Location(longitude,latitude); }
}
