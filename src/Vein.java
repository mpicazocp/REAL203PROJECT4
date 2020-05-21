import processing.core.PImage;
import java.util.*;
public class Vein extends ActionEntity {
    private static final Random rand = new Random();

    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final int ORE_CORRUPT_MAX = 30000;

    public Vein(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super( id, position,  images,  resourceLimit,  resourceCount,  actionPeriod,  animationPeriod );
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent()) {
            Ore ore = Factory.createOre(ORE_ID_PREFIX + super.getId(), openPt.get(),
                    ORE_CORRUPT_MIN + rand.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.getOreKey()));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        EventScheduler.scheduleEvent(scheduler, this,
                Factory.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
    }
}
