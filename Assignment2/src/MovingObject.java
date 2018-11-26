import org.newdawn.slick.Input;

/**
 * Represents one kind of sprites. It has speed and move direction.
 */
public abstract class MovingObject extends Sprite {
	private boolean moveRight;
	private float speed;
	
    /** 
     * Initialize an object of the class MovingObject and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     */
	public MovingObject(String imageSrc, float x, float y, float speed, boolean moveRight) {
		super(imageSrc, x, y);
		this.moveRight = moveRight;
		this.speed = speed;
	}
	
    /** 
     * Initialize the class MovingObject and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     * @param tags sprite's tags
     */
	public MovingObject(String imageSrc, float x, float y, float speed, boolean moveRight, String[] tags) {
		super(imageSrc, x, y, tags);
		this.moveRight = moveRight;
		this.speed = speed;
	}
	
	/**
	 * Accesses the direction of movement.
	 * @return	the direction of movement.
	 */
	public boolean getMoveRight() {
		return moveRight;
	}
	
	/**
	 * Accesses the sprite's speed.
	 * @return	the sprite's speed.
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Sets the direction of movement.
	 * @param moveRight the direction of movement.
	 */
	public void setMoveRight(boolean moveRight) {
		this.moveRight = moveRight;
	}
	
	private final float getInitialX() {
		return moveRight ? -getImageWidth() / 2
						 : App.SCREEN_WIDTH + getImageWidth() / 2;
	}
	
	@Override
	public void update(Input input, int delta) {
		// according to the direction of movement, move the MovingObject
		move(speed * delta * (moveRight ? 1 : -1), 0);
		
		// check if the vehicle has moved off the screen
		if (getX() > App.SCREEN_WIDTH + getImageWidth() / 2 || getX() < -getImageWidth()/ 2
		 || getY() > App.SCREEN_HEIGHT + getImageHeight() / 2 || getY() < -getImageHeight() / 2) {
			setX(getInitialX());
		}
	}
}