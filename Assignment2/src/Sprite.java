import utilities.BoundingBox;
import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * Fundamental class for the game.
 * Represents a sprite and handles rendering its image as well as updating relevant data, 
 * all the sprites compose the game.
 */
public abstract class Sprite {
	/**
	 *  a property of sprite, player will lose a live when touch this kind of sprites.
	 */
	public final static String HAZARD = "hazard";
	/**
	 *  a property of sprite, player can be on the sprite and move with same speed.
	 */
	public final static String RIDE = "ride";
	/**
	 *  a property of sprite, player cannot entry this kind of sprites.
	 */
	public final static String SOLID = "solid";
	/**
	 *  a property of sprite, player adds a life when touches this kind of sprites.
	 */
	public final static String EXTRA_LIFE = "extraLife";
	
	private BoundingBox bounds;
	private Image image;
	private float x;
	private float y;
	private Sprite collideRideableObject = null;
	// record sprite's special properties
	private ArrayList<String> tags;
	
	/** 
     * Initialize an object of class Sprite and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     */
	public Sprite(String imageSrc, float x, float y) {
		setupSprite(imageSrc, x, y);
	}
	
	/** 
     * Initialize an object of class Sprite and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param tags target's tags.
     */
	public Sprite(String imageSrc, float x, float y, String[] tags) {
		setupSprite(imageSrc, x, y);
		for(String temp : tags) {
			this.tags.add(temp);
		}
	}
	
	/** 
     * Add new tags
     * @param tags new target's tags.
     */
	public void addTag(String tag) {
		if(!tags.contains(tag)) {
			tags.add(tag);
		}
	}
	
	/** 
     * Delete tags
     * @param tags deleted target's tags.
     */
	public void deleteTag(String tag) {
		tags.remove(tag);
		tags.trimToSize();
	}
	
	/** 
     * Set tags
     * @param newTags new target's tags.
     */
	public void setTags(ArrayList<String> newTags) {
		tags = newTags;
	}
	
	private void setupSprite(String imageSrc, float x, float y) {
		try {
			image = new Image(imageSrc);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		this.x = x;
		this.y = y;
		
		bounds = new BoundingBox(image, (int)x, (int)y);
		
		tags = new ArrayList<String> ();		
	}

	/**
	 * Sets the x position of the sprite.
	 * @param x	 the target x position
	 */
	public final void setX(float x) {
		this.x = x; bounds.setX((int)x); 
	}
	
	/**
	 * Sets the y position of the sprite.
	 * @param y	 the target y position
	 */
	public final void setY(float y) {
		this.y = y; bounds.setY((int)y);
	}
	
	/**
	 * Accesses the x position of the sprite.
	 * @return	the x position of the sprite
	 */
	public final float getX() {
		return x; 
	}
	
	/**
	 * Accesses the y position of the sprite.
	 * @return	the y position of the sprite
	 */
	public final float getY() {
		return y; 
	}
	
	/**
	 * Accesses the width of sprite's image.
	 * @return	the width of sprite's image.
	 */
	public int getImageWidth() {
		return image.getWidth();
	}
	
	/**
	 * Accesses the height of sprite's image.
	 * @return	the height of sprite's image.
	 */
	public int getImageHeight() {
		return image.getHeight();
	}
	
	/**
	 * Accesses the bound of sprite.
	 * @return	the bound of sprite.
	 */
	public BoundingBox getBounds() {
		return bounds;
	}
	
	/**
	 * Sets the increment of coordinate of the sprite.
	 * @param dx the target x position's increment.
	 * @param dy the target y position's increment.
	 */
	public final void move(float dx, float dy) {
		setX(x + dx);
		setY(y + dy);
	}
	
	
	private final boolean onScreen(float x, float y) {
		return onScreenX(x) && onScreenY(y);
	}
	
	/**
	 * judge whether x position is on the screen.
	 * @param x the input value of x position.
	 * @return a value of boolean represents whether target is on the screen.
	 */
	public final boolean onScreenX(float x) {
		return x + image.getWidth() / 2.0 <= App.SCREEN_WIDTH && x - image.getWidth() / 2.0 >= 0;
	}
	
	/**
	 * judge whether y position is on the screen.
	 * @param y the input value of y position.
	 * @return a value of boolean represents whether target is on the screen.
	 */
	public final boolean onScreenY(float y) {
		return y + image.getHeight() / 2.0 <= App.SCREEN_HEIGHT && y - image.getHeight() / 2.0 >= 0;
	}
	/**
	 * judge whether it is on the screen.
	 * @return a value of boolean represents whether target is on the screen.
	 */
	public final boolean onScreen() {
		return onScreen(x, y);
	}
	
	/**
	 * judge whether it contacts other sprite.
	 * @param other the selected sprite to detect whether it contacts player.
	 * @return a value of boolean represents whether they contact each other.
	 */
	public boolean collides(Sprite other) {
		if (other == null) {
			return false;
		}
		return bounds.intersects(other.bounds);
	}
	
	/**
	 * According the time, update sprite's information
	 * @param input A wrapped for all keyboard, mouse and controller input.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta) {
		/* when the sprite does not contact other sprite with ride tag, 
		 * the deleted tag hazard should be recovered
		 * the mission of collideRideableObject is over
		 */
		if (collideRideableObject != null && (!bounds.intersects(collideRideableObject.getBounds()) 
				|| !collideRideableObject.hasTag(Sprite.RIDE))) {
			addTag(Sprite.HAZARD);
			collideRideableObject = null;
		}
	}
	
	/**
	 * Delete tag Sprite.HAZARD and save the contacted sprite to collideRideableObject
	 * when it has tag Sprite.HAZARD and contacted sprite with tag Sprite.RIDE.
	 * @param other the sprite is contacted.
	 */
	public void onCollision(Sprite other) {
		/* when the sprite with tag hazard contact other sprite with tag ride, 
		delete hazard tag and save other sprite to collideRideableObjcet to detect contact status */
		if(tags.contains(Sprite.HAZARD) && other.hasTag(Sprite.RIDE)) {
			deleteTag(Sprite.HAZARD);
			collideRideableObject = other;	
		}
	}
	
	/**
	 * Render the sprite
	 */
	public void render() {
		image.drawCentered(x, y);
	}
	
	/**
	 * detect whether the sprite has a tag.
	 * @param tag a tag is detected.
	 * @return a boolean of result that whether sprite has this tag.
	 */
	public boolean hasTag(String tag) {
		for (String test : tags) {
			if (tag.equals(test)) {
				return true;
			}
		}
		return false;
	}
}
