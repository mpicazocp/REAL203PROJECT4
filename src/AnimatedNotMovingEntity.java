public interface AnimatedNotMovingEntity extends ActionEntity{
    //Quake; At this point Entities can be animated but do not move
    int getAnimationPeriod();
    void nextImage();

}
