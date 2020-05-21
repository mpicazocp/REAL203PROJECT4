import processing.core.PImage;

import java.util.List;

public abstract class Entity
{
    //blacksmith, obstacle; Most basic Entity:just sits on-screen
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int animationPeriod;

    public Entity(Point position, List<PImage> images, int animationPeriod) {
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.animationPeriod = animationPeriod;
    }

     public List<PImage> getImages(){
        return this.images;
    }

    public void setImageIndex(int index){ this.imageIndex = index;}

    public int getImageIndex(){ return this.imageIndex;}

    public int getAnimationPeriod() {return this.animationPeriod;}

     Point getPosition(){return this.position;}

     void setPosition(Point pos){this.position = pos;};

     public PImage getCurrentImage() {
        return (this.images.get(imageIndex));
    }

}

