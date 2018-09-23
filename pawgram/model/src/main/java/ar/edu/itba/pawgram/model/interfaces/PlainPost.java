package ar.edu.itba.pawgram.model.interfaces;

import ar.edu.itba.pawgram.model.Category;
import ar.edu.itba.pawgram.model.Pet;
import ar.edu.itba.pawgram.model.PostImage;

import java.util.List;

public interface PlainPost {
    public long getId();
    public String getTitle();
    public Category getCategory();
    public List<PostImage> getPostImages();
    public int getDistance();
    public boolean isMale();
    public Pet getPet();
}
