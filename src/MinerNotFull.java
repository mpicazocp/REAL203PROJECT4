import processing.core.PImage;
import java.util.*;
public class MinerNotFull extends MovingEntity {

    public MinerNotFull(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod) {

        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


    protected void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler) {
        Optional<Entity> notFullTarget =
                findNearest(world, super.getPosition(), Ore.class);

        if (!notFullTarget.isPresent() || !this.moveTo(world,
                notFullTarget.get(), scheduler)
                || !this.transformNotFull(world, scheduler, imageStore)) {
            EventScheduler.scheduleEvent(scheduler, this,
                    Factory.createActivityAction(this, world, imageStore),
                    super.getActionPeriod());
        }
    }

    private boolean transformNotFull(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore) {
        if (super.getResourceCount() >= super.getResourceLimit()) {
            MinerFull miner = Factory.createMinerFull(super.getId(), super.getResourceLimit(),
                    super.getPosition(), super.getActionPeriod(),
                    super.getAnimationPeriod(),
                    super.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
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

    protected boolean moveHelper(WorldModel world, Entity target, EventScheduler scheduler){
        super.setResourceCount(super.getResourceCount() + 1);
        world.removeEntity(target);
        scheduler.unscheduleAllEvents(target);
        return true;
    }



}
