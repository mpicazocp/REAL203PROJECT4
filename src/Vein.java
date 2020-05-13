import processing.core.PImage;
import java.util.*;
public class Vein implements ActionEntity{
    private static final Random rand = new Random();

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private final int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public Vein(
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


    public List<PImage> getImages(){
        return this.images;
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point p){this.position = p;}

    public int getActionPeriod(){return this.actionPeriod;}

    public PImage getCurrentImage() {return (this.images.get(imageIndex));}

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.position);

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + this.id, openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.getOreKey()));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createActivityAction(this, world, imageStore),
                this.actionPeriod);
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createActivityAction(this, world, imageStore),
                this.getActionPeriod());

    }
}
