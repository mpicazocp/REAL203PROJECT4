import processing.core.PImage;
import java.util.*;
public class Animation implements Action{
    private final MovingEntity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Animation(
            MovingEntity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {

        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }


    public void executeAction(
            EventScheduler scheduler, Entity entity)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            EventScheduler.scheduleEvent(scheduler, this.entity,
                    Factory.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }


}
