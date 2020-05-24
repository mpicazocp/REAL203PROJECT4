import processing.core.PImage;
import java.util.List;

public abstract class ActionEntity extends Entity {
    //vein, ore; Have calls to Action but no animation. These entities appear on-screen but do not change at all.

    private final String id;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;


    public ActionEntity(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod){
        super(position, images);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
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

    public int getResourceLimit(){return this.resourceLimit;}

    public int getResourceCount(){return this.resourceCount;}

    public void setResourceCount(int i){this.resourceCount = i;}

    public String getId(){return this.id;}

    public int getActionPeriod(){return this.actionPeriod;}

    protected abstract void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler);
}
