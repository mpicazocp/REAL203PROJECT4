import processing.core.PImage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public final class WorldModel
{
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
    public Set<Entity> entities;
    private static final int ORE_REACH = 1;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.position)) {
            this.setOccupancyCell(entity.position, entity);
            this.entities.add(entity);
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    public void setBackgroundCell(
            Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(
            Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public void setBackground(
             Point pos, Background background)
    {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public Optional<PImage> getBackgroundImage(
            Point pos)
    {
        if (this.withinBounds(pos)) {
            return Optional.of(Background.getCurrentImage(this.getBackgroundCell(pos)));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (this.withinBounds(newPt) && !this.isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

}
