import org.newdawn.slick.Input;

/**
 * Represents a special sprite, goes there and back between two points.
 */
public class Bike extends Vehicle {
	private static final float BIKE_SPEED = 0.2f;
	private static final String BIKE_PATH = "assets/bike.png";
	private static final int RETURN_POINT1 = 24;
	private static final int RETURN_POINT2 = 1000;
	
    /** Initialize an object of the class Bike and set its configuration
     * @param x x position.
     * @param y y position.
     * @param moveRight represents the direction of movement.
     */
	public Bike(float x, float y, boolean moveRight) {
		super(BIKE_PATH, x, y, BIKE_SPEED, moveRight);
	}
	
	@Override
	public void update(Input input, int delta) {
		// according to the direction of movement, move the bike
		move(getSpeed() * delta * (getMoveRight() ? 1 : -1), 0);
		
		// Check whether bike needs to revert direction
		if (getX() <= RETURN_POINT1) {
			setMoveRight(true);
		}
		if (getX() >= RETURN_POINT2) {
			setMoveRight(false);
		}
	}
}