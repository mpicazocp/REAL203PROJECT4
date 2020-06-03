import processing.core.PImage;
import java.util.*;
public class MinerFull extends Miner{

    public MinerFull(

            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
     super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    protected void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if(!isFREAKING_OUT()){
            Optional<Entity> fullTarget =
                    findNearest(world, super.getPosition(), Blacksmith.class);

            if (fullTarget.isPresent() && this.moveTo(world,
                    fullTarget.get(), scheduler))
            {
                this.transformFull(world, scheduler, imageStore);
            }
            else {
                EventScheduler.scheduleEvent(scheduler, this,
                        Factory.createActivityAction(this, world, imageStore),
                        super.getActionPeriod());
            }
        } else {
            Point fleePoint = super.getPosition();
            if(fleePoint.getX() < fleePoint.getY()){
                fleePoint = new Point(0, fleePoint.getY());
            } else {
                fleePoint = new Point(fleePoint.getX(), 0);
            }

            this.moveTo(world,fleePoint,scheduler);

        }

    }

    private void transformFull(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        MinerNotFull miner = Factory.createMinerNotFull(super.getId(), super.getResourceLimit(),
                super.getPosition(), super.getActionPeriod(),
                super.getAnimationPeriod(),
                super.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

     protected Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    protected boolean moveHelper(WorldModel world, Entity target, EventScheduler scheuler){return true;}



}
