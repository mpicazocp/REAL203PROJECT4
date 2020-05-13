import processing.core.PImage;
import java.util.*;
public class Blacksmith implements Entity {


    private Point position;
    private final List<PImage> images;
    private final int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;

    public Blacksmith(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod) {

        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

   public PImage getCurrentImage() {
        return (this.images.get(imageIndex));
    }
}


