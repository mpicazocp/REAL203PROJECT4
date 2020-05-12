
public class Activity implements Action {
    private final MovingEntity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Activity(
            MovingEntity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(
            EventScheduler scheduler) { this.entity.executeActivity(this.world, this.imageStore, scheduler); }
}





