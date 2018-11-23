package ar.edu.itba.pawgram.webapp.dto.form;

import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.model.structures.Location;
import ar.edu.itba.pawgram.webapp.form.constraints.DateRule;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class FormPost {
    @NotBlank
    @Size(max = 64)
    private String title;

    @NotBlank
    @Size(max = 2048)
    private String description;

    @NotNull
    @Size(max = 32)
    @Pattern(regexp = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")//regex phone number
    private String contact_phone;

    @NotNull
    @DateRule
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date event_date;

    @NotNull
    @Pattern(regexp="lost|found|adopt|emergency", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String category;

    @NotNull
    @Pattern(regexp="dog|cat|other", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String pet;

    @NotNull
    private Boolean is_male;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public FormPost(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public Date getEvent_date() {
        return event_date;
    }

    public void setEvent_date(Date event_date) {
        this.event_date = event_date;
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

    public String getCategory() {
        return category;
    }

    public Category getAsCategory() {
        return Category.fromString(category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPet() {
        return pet;
    }

    public Pet getAsPet() {
        return Pet.fromString(pet);
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public Location getLocation(){
        return new Location(longitude,latitude);
    }
}
