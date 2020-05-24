import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    //blacksmith, obstacle; Most basic Entity:just sits on-screen
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Entity(Point position, List<PImage> images) {
        this.position = position;
        this.images = images;
        imageIndex = 0;
    }

    public PImage getCurrentImage() {
        return (getImages().get(imageIndex));
    }

    public void setImageIndex(int index){ imageIndex = index;}

    public int getImageIndex() { return imageIndex; }

    public List<PImage> getImages(){
        return this.images;
    }

     Point getPosition(){return this.position;}

     void setPosition(Point pos){this.position = pos;};


}

