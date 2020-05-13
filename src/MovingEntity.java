import java.util.List;
import java.util.Optional;

public interface MovingEntity extends AnimatedNotMovingEntity{
    //miner, minerNF, OreBlob ; Fully functional Entities

    Optional<Entity> findNearest(WorldModel world, Point pos, Class c);

    Optional<Entity> nearestEntity(List<Entity> entities, Point pos);

    int distanceSquared(Point p1, Point p2);
    boolean adjacent(Point p1, Point p2);
}
