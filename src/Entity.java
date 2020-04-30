import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public final class Entity
{
    private static final Random rand = new Random();

    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private static final String QUAKE_KEY = "quake";

    private EntityKind kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;

    public Entity(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        this.kind = kind;
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

    public EntityKind getKind(){
        return this.kind;
    }

    public int getActionPeriod(){
        switch (this.kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case ORE:
                case VEIN:
                case QUAKE:
                return this.actionPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getActionPeriod not supported for %s",
                                this.kind));
        }
    }

    public int getAnimationPeriod() {
        switch (this.kind) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return this.animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                this.kind));
        }
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % this.images.size();
    }

    public PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background)entity).getImages().get(
                    imageIndex);
        }
        else if (entity instanceof Entity) {
            return ((Entity)entity).images.get(((Entity)entity).imageIndex);
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

    public void executeMinerFullActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                findNearest(world, this.position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() && moveToFull(this, world,
                fullTarget.get(), scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeMinerNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                findNearest(world, this.position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !moveToNotFull(this, world,
                notFullTarget.get(),
                scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            EventScheduler.scheduleEvent(scheduler, this,
                    Functions.createActivityAction(this, world, imageStore),
                    this.actionPeriod);
        }
    }

    public void executeOreActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = this.position;

        removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        Entity blob = Functions.createOreBlob(this.id + BLOB_ID_SUFFIX, pos,
                this.actionPeriod / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + rand.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        EventScheduler.scheduleActions(blob, scheduler, world, imageStore);
    }

    public void executeOreBlobActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                findNearest(world, this.position, EntityKind.VEIN);
        long nextPeriod = this.actionPeriod;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().position;

            if (moveToOreBlob(this, world, blobTarget.get(), scheduler)) {
                Entity quake = Functions.createQuake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.actionPeriod;
                EventScheduler.scheduleActions(quake, scheduler, world, imageStore);
            }
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Functions.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public void executeQuakeActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        removeEntity(world);
    }

    public void executeVeinActivity(

            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            Entity ore = Functions.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.getOreKey()));
            world.addEntity(ore);
            EventScheduler.scheduleActions(ore, scheduler, world, imageStore);
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Functions.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }



    private boolean transformNotFull(

            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit) {
            Entity miner = Functions.createMinerFull(this.id, this.resourceLimit,
                    this.position, this.actionPeriod,
                    this.animationPeriod,
                    this.images);

            removeEntity(world);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            EventScheduler.scheduleActions(miner, scheduler, world, imageStore);

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

        removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        EventScheduler.scheduleActions(miner, scheduler, world, imageStore);
    }

    private Point nextPositionMiner(
             WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    private Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.getX() - this.position.getX());
        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().kind
                == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.getY() - this.position.getY());
            newPos = new Point(this.position.getX(), this.position.getY() + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().kind
                    == EntityKind.ORE)))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }

    private void moveEntity(WorldModel world, Point pos) {
        Point oldPos = this.position;
        if (world.withinBounds(pos) && !pos.equals(oldPos)) {
            world.setOccupancyCell(oldPos, null);
            removeEntityAt(world, pos);
            world.setOccupancyCell(pos, this);
            this.position = pos;
        }
    }

    private void removeEntity(WorldModel world) {
        removeEntityAt(world, this.position);
    }

    private static void removeEntityAt(WorldModel world, Point pos) {
        if (world.withinBounds(pos) && world.getOccupancyCell(pos) != null) {
            Entity entity = world.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.position = new Point(-1, -1);
            world.getEntities().remove(entity);
            world.setOccupancyCell(pos, null);
        }
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
            WorldModel world, Point pos, EntityKind kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.kind == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private static boolean moveToNotFull(
            Entity miner,
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (adjacent(miner.position, target.position)) {
            miner.resourceCount += 1;
            target.removeEntity(world);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = miner.nextPositionMiner(world, target.position);

            if (!miner.position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                miner.moveEntity(world,nextPos);
            }
            return false;
        }
    }

    private static boolean moveToFull(
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

                miner.moveEntity(world,nextPos);
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
            target.removeEntity(world);
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

                blob.moveEntity(world, nextPos);
            }
            return false;
        }
    }



    private static boolean adjacent(Point p1, Point p2) {
        return (p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) == 1) || (p1.getY() == p2.getY()
                && Math.abs(p1.getX() - p2.getX()) == 1);
    }


}

