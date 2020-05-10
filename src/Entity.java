
public interface Entity
{
    Point position = null;

    Point getPosition();

     void setPosition(Point pos);


    void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}

