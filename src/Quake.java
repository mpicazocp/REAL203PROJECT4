import processing.core.PImage;
import java.util.*;
public class Quake implements AnimatedNotMovingEntity {

    private String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public Quake(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public List<PImage> getImages(){
        return this.images;
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point p){this.position = p;}

    public int getActionPeriod(){return this.actionPeriod; }

    public int getAnimationPeriod() {return this.animationPeriod;}

    public void nextImage() {
        imageIndex = (imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {return (this.images.get(imageIndex));}

    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        EventScheduler.scheduleEvent(scheduler,  this,
                Factory.createActivityAction( this, world, imageStore),
                this.getActionPeriod());
        EventScheduler.scheduleEvent(scheduler, this, Factory.createAnimationAction(this,
                Functions.getQuakeAnimationRepeatCount()),
                this.getAnimationPeriod());
    }
}
