import java.util.*;
import java.util.function.Function;

public final class EventScheduler
{
    private PriorityQueue<Event> eventQueue;
    private Map<Entity, List<Event>> pendingEvents;
    private double timeScale;

    public EventScheduler(double timeScale) {
        this.eventQueue = new PriorityQueue<>(new EventComparator());
        this.pendingEvents = new HashMap<>();
        this.timeScale = timeScale;
    }

    public static void scheduleActions(
            Entity entity,
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (entity.getKind()) {
            case MINER_FULL:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                scheduleEvent(scheduler, entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                scheduleEvent(scheduler, entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case ORE:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                break;

            case ORE_BLOB:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                scheduleEvent(scheduler, entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case QUAKE:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                scheduleEvent(scheduler, entity, Functions.createAnimationAction(entity,
                        Functions.getQuakeAnimationRepeatCount()),
                        entity.getAnimationPeriod());
                break;

            case VEIN:
                scheduleEvent(scheduler, entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.getActionPeriod());
                break;

            default:
        }
    }

    public static void scheduleEvent(
            EventScheduler scheduler,
            Entity entity,
            Action action,
            long afterPeriod)
    {
        long time = System.currentTimeMillis() + (long)(afterPeriod
                * scheduler.timeScale);
        Event event = new Event(action, time, entity);

        scheduler.eventQueue.add(event);

        // update list of pending events for the given entity
        List<Event> pending = scheduler.pendingEvents.getOrDefault(entity,
                new LinkedList<>());
        pending.add(event);
        scheduler.pendingEvents.put(entity, pending);
    }


    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            scheduleActions(entity, scheduler, world, imageStore);
        }
    }

    public void unscheduleAllEvents(
            Entity entity)
    {
        List<Event> pending = this.pendingEvents.remove(entity);

        if (pending != null) {
            for (Event event : pending) {
                this.eventQueue.remove(event);
            }
        }
    }

    public void removePendingEvent(
            Event event)
    {
        List<Event> pending = this.pendingEvents.get(event.getEntity());

        if (pending != null) {
            pending.remove(event);
        }
    }

    public void updateOnTime(long time) {
        while (!this.eventQueue.isEmpty()
                && this.eventQueue.peek().getTime() < time) {
            Event next = this.eventQueue.poll();

            this.removePendingEvent(next);

            next.getAction().executeAction(this);
        }
    }

}
