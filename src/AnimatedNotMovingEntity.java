import processing.core.PImage;

import java.util.List;

public abstract class AnimatedNotMovingEntity extends ActionEntity{
    //Quake; At this point Entities can be animated but do not move

    public AnimatedNotMovingEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    public Point getPosition(){
        return super.getPosition();
    }

    public void setPosition(Point p){super.setPosition(p);}

    public int getActionPeriod(){return super.getActionPeriod(); }

    public int getAnimationPeriod() {return super.getAnimationPeriod();}

    public void nextImage() {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public PImage getCurrentImage() {return super.getCurrentImage();}

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
