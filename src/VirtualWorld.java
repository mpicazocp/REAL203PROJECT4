import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;


    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static final String ALIEN_ID = "alien";
    private static final String ALIEN_KEY = "alien";
    private static final int ALIEN_ACTION_PERIOD = 666;
    private static final int ALIEN_ANIMATION_PERIOD = 100;

    private static final int CRATER_RADIUS = 2;
    private static final int SCARE_RADIUS = 7;

    private static double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(imageStore, this);
        loadWorld(world, imageStore);
        //make if statement
        scheduleActions( world, scheduler,imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if(entity instanceof MovingEntity) {
                MovingEntity casted = (MovingEntity) entity;
                casted.scheduleActions(scheduler, world, imageStore);
            }else if(entity instanceof ActionEntity) {
                ActionEntity casted = (ActionEntity) entity;
                casted.scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView( dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                              imageStore.getImageList(
                                                     DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    private static void loadImages(
            ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(VirtualWorld.IMAGE_LIST_FILE_NAME));
            Functions.loadImages(in, imageStore, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadWorld(
            WorldModel world, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(VirtualWorld.LOAD_FILE_NAME));
            Functions.load(in, world, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }



    private static void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public void mousePressed(){
        Point pressed = mouseToPoint(mouseX, mouseY);

        Alien alien = Factory.createAlien(ALIEN_ID, pressed, ALIEN_ACTION_PERIOD,
                ALIEN_ANIMATION_PERIOD, imageStore.getImageList(ALIEN_KEY));
        world.forceAddEntity(alien);
        alien.scheduleActions(scheduler, world,imageStore);
        //update the backgrounds
        for(int y = pressed.getY() - CRATER_RADIUS; abs(pressed.getY() - y) <= CRATER_RADIUS; y++){
            for(int x = pressed.getX() - CRATER_RADIUS; abs(pressed.getX() - x) <= CRATER_RADIUS; x++){
                if(sqrt(pow(x - pressed.getX(),2) + pow(y - pressed.getY(),2)) <= CRATER_RADIUS && x >= 0 && y >= 0 && x < 30 && y < 30){
                    //change background
                    Background cell = new Background("crater", imageStore.getImageList("crater"));
                    world.setBackgroundCell(new Point(x,y), cell);
                }
            }
        }

        //freak out all the miners nearby
        for(int y = pressed.getY() - SCARE_RADIUS; abs(pressed.getY() - y) <= SCARE_RADIUS; y++){
            for(int x = pressed.getX() - SCARE_RADIUS; abs(pressed.getX() - x) <= SCARE_RADIUS; x++){
                if(sqrt(pow(x - pressed.getX(),2) + pow(y - pressed.getY(),2)) <= SCARE_RADIUS && x >= 0 && y >= 0){

                    //freak miner!
                    Optional<Entity> entity = world.getOccupant(new Point(x,y));
                    if(!entity.isEmpty() && (entity.get().getClass().equals(MinerNotFull.class) || entity.get().getClass().equals(MinerFull.class))){
                        ((Miner)entity.get()).react(imageStore);
                    }

                }
            }
        }
}

    private Point mouseToPoint(int x, int y){
        return new Point((x /TILE_WIDTH) + view.getX(), (y /TILE_HEIGHT) + view.getY());}

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);

    }
}
