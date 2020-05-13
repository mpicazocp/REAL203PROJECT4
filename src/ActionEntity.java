import processing.core.PImage;
import java.util.List;

public interface ActionEntity extends Entity {
    //vein, ore; Have calls to Action but no animation. These entities appear on-screen but do not change at all.

    void scheduleActions(EventScheduler scheduler, WorldModel world,  ImageStore imageStore);

    List<PImage> getImages();

    int getActionPeriod();

     void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);
}
