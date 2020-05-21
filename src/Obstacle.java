import processing.core.PImage;
import java.util.*;

public class Obstacle extends Entity {

    public Obstacle(
                    Point position,
                    List<PImage> images, int animationPeriod) {
        super( position, images, animationPeriod);
    }

}
