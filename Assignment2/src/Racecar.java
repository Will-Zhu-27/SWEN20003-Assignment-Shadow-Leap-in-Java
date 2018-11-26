/**
 * Realizes one sprite, racecar.
 */
public class Racecar extends Vehicle {
	private static final float RACECAR_SPEED = 0.5f;
	private static final String RACECAR_PATH = "assets/racecar.png";
	
	/** 
     * Initialize an object of class Racecar and set its configuration.
     * @param x x position.
     * @param y y position.
     * @param moveRight represents the direction of movement.
     */
	public Racecar(float x, float y, boolean moveRight) {
		super(RACECAR_PATH, x, y, RACECAR_SPEED, moveRight);
	}
}