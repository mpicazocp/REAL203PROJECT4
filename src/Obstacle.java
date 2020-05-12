import processing.core.PImage;
import java.util.*;

public class Obstacle implements Entity {
    // private static final Random rand = new Random();

    private String id;
    private Point position;
    private final List<PImage> images;
    private final int imageIndex;


    public Obstacle(String id,
                    Point position,
                    List<PImage> images) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


    public List<PImage> getImages() {
        return this.images;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
    }

    public String getId() {
        return this.id;
    }

    public PImage getCurrentImage() {

        return (this.images.get(imageIndex));

    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore,
                                EventScheduler scheduler) {
    };
}
 /*   private static Optional<Entity> nearestEntity(
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

    private static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.getX() - p2.getX();
        int deltaY = p1.getY() - p2.getY();

        return deltaX * deltaX + deltaY * deltaY;
    }

/*    private  Optional<Entity> findNearest(
            WorldModel world, Point pos)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.getClass() == this.getClass()) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

 /*   private boolean transformNotFull(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Functions.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private void transformFull(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = Functions.createMinerNotFull(this.id, this.resourceLimit,
                this.position, this.actionPeriod,
                this.animationPeriod,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }
*/






  /*  public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            entity.scheduleActions(scheduler, world, imageStore);
        }
    }
}

    /*private static boolean moveToFull(
            Entity miner,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(miner.position, target.position)) {
            return true;
        }
        else {
            Point nextPos = miner.nextPositionMiner( world, target.position);

            if (!miner.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(nextPos, miner);
            }
            return false;
        }
    }

    private static boolean moveToOreBlob(
            Entity blob,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(blob.position, target.position)) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = blob.nextPositionOreBlob(world, target.position);

            if (!blob.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(nextPos, blob);
            }
            return false;
        }
    }

*/

    /*private static boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }*/



