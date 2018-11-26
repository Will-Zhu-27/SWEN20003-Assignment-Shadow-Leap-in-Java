/**
 * Represents one kind of sprites, vehicle.
 * It has speed and runs there and back between two points.
 */
public abstract class Vehicle extends MovingObject {
	/** 
     * Initialize an object of class Vehicle and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     */
	public Vehicle(String imageSrc, float x, float y, float speed, boolean moveRight) {
		super(imageSrc, x, y, speed, moveRight);
		addTag(Sprite.HAZARD);
	}
	/** 
     * Initialize an object of class Vehicle and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     * @param tags target's tags.
     */
	public Vehicle(String imageSrc, float x, float y, float speed, boolean moveRight, String[] tags) {
		super(imageSrc, x, y, speed, moveRight, tags);
	}
}
