import processing.core.PImage;

import java.util.List;

public abstract class Miner extends MovingEntity {

    public Miner(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod){
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);

    }

    public void react(){


    }
}
