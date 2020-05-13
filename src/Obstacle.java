import processing.core.PImage;
import java.util.*;

public class Obstacle implements Entity {
    private String id;
    private Point position;
    private final List<PImage> images;
    private final int imageIndex;


    public Obstacle(String id,
                    Point position,
                    List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

    public PImage getCurrentImage() { return (this.images.get(imageIndex)); }

}
