import processing.core.PImage;
import java.util.List;

public abstract class AnimatedNotMovingEntity extends ActionEntity{
    //Quake; At this point Entities can be animated but do not move

    private final int animationPeriod;

    public AnimatedNotMovingEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod(){return this.animationPeriod;}

    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        EventScheduler.scheduleEvent(scheduler,  this,
                Factory.createActivityAction( this, world, imageStore),
                this.getActionPeriod());
        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createAnimationAction(this,
                Functions.getQuakeAnimationRepeatCount()),
                this.getAnimationPeriod());
    }

}