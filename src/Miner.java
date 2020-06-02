import processing.core.PImage;

import java.util.List;


public abstract class Miner extends MovingEntity {
    private static final String MINER_KEY = "miner";
    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

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

        List<PImage> images = imageStore.getImageList("madMiner");


    }
}
