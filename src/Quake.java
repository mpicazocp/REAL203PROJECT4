import processing.core.PImage;
import java.util.*;
public class Quake implements MovingEntity {

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

    public int getActionPeriod(){return this.actionPeriod; }

    public int getAnimationPeriod() {return this.animationPeriod;}

    public void nextImage() {
        imageIndex = (imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage() {return (this.images.get(imageIndex));}


    public void executeQuakeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }


    private static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.position, pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.position, pos);

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
        EventScheduler.scheduleEvent(scheduler, this, Factory.createAnimationAction(this,
                Functions.getQuakeAnimationRepeatCount()),
                this.getAnimationPeriod());
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            entity.scheduleActions(scheduler, world, imageStore);
        }
    }
}
