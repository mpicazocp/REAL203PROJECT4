import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class MovingEntity extends AnimatedNotMovingEntity{
    //miner, minerNF, OreBlob ; Fully functional Entities


    public MovingEntity(
    String id,
    Point position,
    List<PImage> images,
    int resourceLimit,
    int resourceCount,
    int actionPeriod,
    int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

    public   Optional<Entity> findNearest(
            WorldModel world, Point pos, Class c)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.getClass() == c) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    public boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

    public Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
//call super then add for each specific
    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduleActionsHelp(scheduler, world, imageStore);
    }

    public void scheduleActionsHelp(EventScheduler scheduler,
                                    WorldModel world,
                                    ImageStore imageStore){
        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createAnimationAction(this, 0),
                super.getAnimationPeriod());
    }

    protected abstract Point nextPosition(WorldModel world, Point desPos);

    protected abstract boolean moveHelper(WorldModel world, Entity target, EventScheduler scheduler);

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler){
        if (adjacent(super.getPosition(), target.getPosition())) {
            return moveHelper( world,  target,  scheduler);
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());
            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(nextPos, this);
            }
            return false;
        }
    }

    public boolean moveTo(WorldModel world, Point target, EventScheduler scheduler){
        if (adjacent(super.getPosition(), target)) {
            world.removeEntityAt(this.getPosition());
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target);
            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                occupant.ifPresent(scheduler::unscheduleAllEvents);

                world.moveEntity(nextPos, this);
            }
            return false;
        }
    }


    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected void setPanic(List<PImage> images){
        super.setPanic(images);
    }
}
