/**
 * Realizes bus sprite.
 */
public class Bus extends Vehicle {
	private static final float BUS_SPEED = 0.15f;
	private static final String BUS_PATH = "assets/bus.png";
	
    /** Initialize an object of the class Bus and set its configuration
     * @param x x position.
     * @param y y position.
     * @param moveRight represents the direction of movement.
     */
	public Bus(float x, float y, boolean moveRight) {
		super(BUS_PATH, x, y, BUS_SPEED, moveRight);
	}
}