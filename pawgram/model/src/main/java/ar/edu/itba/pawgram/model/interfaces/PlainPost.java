package ar.edu.itba.pawgram.model.interfaces;

import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;

public interface PlainPost {
    public long getId();
    public String getTitle();
    public Category getCategory();
    public String getImg_url();
    public Pet getPet();
}
