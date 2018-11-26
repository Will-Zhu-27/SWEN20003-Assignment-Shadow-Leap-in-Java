/**
 * Represents one kind of sprites. The player can stay on it and is taken with the same speed.
 */
public abstract class RideableObject extends MovingObject {
	/** 
     * Initialize an object of class RideableObject and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     */
	public RideableObject(String imageSrc, float x, float y, float speed, boolean moveRight) {
		super(imageSrc, x, y, speed, moveRight);
		addTag(Sprite.RIDE);
	}
}