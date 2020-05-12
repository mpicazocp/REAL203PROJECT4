import processing.core.PImage;

import java.util.List;

public interface MovingEntity extends Entity{
    //miner, minerNF,  OreBlob, quake, vein, blob
    void scheduleActions(EventScheduler scheduler, WorldModel world,  ImageStore imageStore);

    int getAnimationPeriod();

    void nextImage();

    List<PImage> getImages();

    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}
