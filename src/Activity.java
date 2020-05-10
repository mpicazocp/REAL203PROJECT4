import processing.core.PImage;
import java.util.*;
public class Activity implements Action{
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Activity(

            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {

        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
        //switch (this.entity.getKind()) {
            //case MINER_FULL:
                this.entity.executeFullActivity(this.world,
                        this.imageStore, scheduler);
             /*   break;

            case MINER_NOT_FULL:
                this.entity.executeMinerNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case ORE:
                this.entity.executeOreActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case ORE_BLOB:
                this.entity.executeOreBlobActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                this.entity.executeQuakeActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case VEIN:
                this.entity.executeVeinActivity(this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeAction not supported for %s",
                        this.entity.getKind()));
        }
    }*/



}
