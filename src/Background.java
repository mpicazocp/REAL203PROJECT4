import java.util.List;

import processing.core.PImage;

public final class Background
{
    private final String id;
    private final List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }

    public List<PImage> getImages(){
        return this.images;
    }

    public String getId(){
        return this.id;
    }

    public PImage getCurrentImage() { return this.getImages().get(imageIndex); }
}
