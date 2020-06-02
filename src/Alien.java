import processing.core.PImage;
import java.util.*;

public class Alien extends MovingEntity {



    public Alien(

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
        Optional<Entity> minerTarget =
                findNearest(world, this.getPosition(), MovingEntity.class);
        long nextPeriod = super.getActionPeriod();

        if (minerTarget.isPresent()) {
            Point tgtPos = minerTarget.get().getPosition();

            if (moveTo(world, minerTarget.get(), scheduler)) {
                world.removeEntityAt(tgtPos);
            }
        }
        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    protected Point nextPosition(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - super.getPosition().getX());
        Point newPos = new Point(super.getPosition().getX() + horiz, super.getPosition().getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
        {
            int vert = Integer.signum(destPos.getY() - super.getPosition().getY());
            newPos = new Point(super.getPosition().getX(), super.getPosition().getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass() == Ore.class)))
            {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    protected boolean moveHelper(WorldModel world, Entity target, EventScheduler scheduler){
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
        return true;
    }






}