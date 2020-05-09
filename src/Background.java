import java.util.List;

import processing.core.PImage;

public final class Background
{
    private String id;
    private List<PImage> images;
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

    public int imageIndex(){
        return this.imageIndex;
    }

    public PImage getCurrentImage() {
        //if (entity instanceof Background) {
            return this.getImages().get(imageIndex);
        //}
        /*else if (entity instanceof Entity) {
            return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
   */ }

}
