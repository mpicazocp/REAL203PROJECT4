import processing.core.PImage;
import java.util.*;

public class OreBlob implements MovingEntity {

    //private static final String ORE_ID_PREFIX = "ore -- ";
    //private static final int ORE_CORRUPT_MIN = 20000;
    //private static final int ORE_CORRUPT_MAX = 30000;

    private static final String QUAKE_KEY = "quake";


    private String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public OreBlob(

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

    public int getImageIndex(){
        return this.imageIndex;
    }

    public List<PImage> getImages(){
        return this.images;
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point p){this.position = p;}

    public int getActionPeriod(){ return this.actionPeriod; }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {return (this.images.get(imageIndex));}

    public void executeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                findNearest(world, this.getPosition(), Vein.class);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().getPosition();

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Quake quake = Factory.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    private Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == Ore.class)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == Ore.class)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }



    private static Optional<Entity> nearestEntity(
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

    private  Optional<Entity> findNearest(
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



    private boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionOreBlob(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(nextPos, this);
            }
            return false;
        }
    }

    private static boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }

 public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {

                EventScheduler.scheduleEvent(scheduler, this,
                        Factory.createActivityAction(this, world, imageStore),
                        this.getActionPeriod());
                EventScheduler.scheduleEvent(scheduler, this,
                        Factory.createAnimationAction(this, 0),
                        this.getAnimationPeriod());
    }


}
