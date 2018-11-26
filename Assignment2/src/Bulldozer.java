/**
 * Represents a special sprite, Bulldozer, prevents player moving 
 * when player contacts it and pushes the player off the screen.
 */
public class Bulldozer extends Vehicle {
	private static final float BULLDOZER_SPEED = 0.05f;
	private static final String BULLDOZER_PATH = "assets/bulldozer.png";
	
    /** Initialize an object of the class Bulldozer and set its configuration
     * @param x x position.
     * @param y y position.
     * @param moveRight represents the direction of movement.
     */
	public Bulldozer(float x, float y, boolean moveRight) {
		super(BULLDOZER_PATH, x, y, BULLDOZER_SPEED, moveRight, new String[] {Sprite.SOLID});
	}
}