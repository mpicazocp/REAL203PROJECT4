import processing.core.PImage;

import java.util.List;


public abstract class Miner extends MovingEntity {

    private boolean FREAKING_OUT = false;

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

    public void react(ImageStore imageStore){
        FREAKING_OUT = true;

        List<PImage> newImages = imageStore.getImageList("madMiner");
        super.setPanic(newImages);
    }

    public boolean isFREAKING_OUT() {
        return FREAKING_OUT;
    }

}
