import processing.core.PImage;

import java.util.List;

public interface AEntity extends MovingEntity {
    //vein, ore

    void scheduleActions(EventScheduler scheduler, WorldModel world,  ImageStore imageStore);

}
