import java.util.ArrayList;
import org.newdawn.slick.Input;
import utilities.BoundingBox;

/**
 * Realizes a special sprite, player. It can be controlled to move by keyboard.
 * It does different responses when it contacts with other sprites with different tags.
 */
public class Player extends Sprite {
	private static final String PLAYER_PATH = "assets/frog.png";
	private static final float LIFE_ICON_X = 24;
	private static final float LIFE_ICON_Y = 744;
	private static final float LIFE_ICON_INTERVAL_X = 32;
	
	// extra bounds to detect potential solid sprites
	private BoundingBox upBound;
	private BoundingBox downBound;
	private BoundingBox leftBound;
	private BoundingBox rightBound;
	
	// save potentially solid sprites
	private Sprite upSolidSprite = null;
	private Sprite downSolidSprite = null;
	private Sprite leftSolidSprite = null;
	private Sprite rightSolidSprite = null;
	
	// save rideable object when a player on it
	private RideableObject onRideableObject = null;
	
	// save solid object which will push the player
	private MovingObject solidObject = null;
	
	// flags to decide player's movement
	private boolean moveUp;
	private boolean moveDown;
	private boolean moveLeft;
	private boolean moveRight;
	
	private ArrayList<Sprite> playerInHole;
	private ArrayList<Sprite> lives = new ArrayList<> ();
	
    /** 
     * Initialize an object of class Player and set its configuration.
     * @param x x position.
     * @param y y position.
     */
	public Player(float x, float y) {
		super(PLAYER_PATH, x, y);
		
		// according number of lives, add corresponding lives remaining logo
		for(int i = 0; i < World.getNumLives(); i++) {
			lives.add(Tile.createLifeTile(LIFE_ICON_X + i * LIFE_ICON_INTERVAL_X, LIFE_ICON_Y));
		}
		
		playerInHole = new ArrayList<Sprite> ();
		
		// initialize all extra bounds
		upBound = new BoundingBox(x, y - getImageHeight(), getImageWidth(), getImageHeight());
		downBound = new BoundingBox(x, y + getImageHeight(), getImageWidth(), getImageHeight());
		leftBound = new BoundingBox(x - getImageWidth(), y, getImageWidth(), getImageHeight());
		rightBound = new BoundingBox(x + getImageWidth(), y, getImageWidth(), getImageHeight());
	}
	
	private void checkMoveable() {
		// check movement direction of left, right, up, down whether is available.
		if (leftSolidSprite == null || !leftBound.intersects(leftSolidSprite.getBounds())) {
			moveLeft = true;
			leftSolidSprite = null;
		} else {
			moveLeft = false;
		}
		if (rightSolidSprite == null || !rightBound.intersects(rightSolidSprite.getBounds())) {
			moveRight = true;
			rightSolidSprite = null;
		} else {
			moveRight = false;
		}
		if (downSolidSprite == null || !downBound.intersects(downSolidSprite.getBounds())) {
			moveDown = true;
			downSolidSprite = null;
		} else {
			moveDown = false;
		}
		if (upSolidSprite == null||!upBound.intersects(upSolidSprite.getBounds())) {
			moveUp = true;
			upSolidSprite = null;
		} else {
			moveUp = false;
		}
	}
	
	/**
	 * respond the keyboard input
	 * @param input A wrapped for all keyboard, mouse and controller input.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	@Override
	public void update(Input input, int delta) {
		int dx = 0, dy = 0;
		checkMoveable();
		
		// respond the input of keyboard
		if (input.isKeyPressed(Input.KEY_LEFT)) {
			if (moveLeft) {
				dx -= getImageWidth();
			}
		}
		if (input.isKeyPressed(Input.KEY_RIGHT)) {
			if (moveRight) {
				dx += getImageWidth();
			}
		}
		if (input.isKeyPressed(Input.KEY_DOWN)) {
			if (moveDown) {
				dy += getImageHeight();
			}
		} 
		if (input.isKeyPressed(Input.KEY_UP)) {
				if(moveUp) {
					dy -= getImageHeight();
				}
		}
		
		checkRideObject (delta);
		checkSolidObject (delta);
		// make sure the frog stays on screen
		if (!onScreenX(getX() + dx)) {
			dx = 0;
		}
		if (!onScreenY(getY() + dy)) {
			dy = 0;
		}
		
		move(dx, dy);
		
		arriveHole();
		updateBound();
	}
	
	private void checkRideObject (int delta) {
		/* if player is on a rideable object, the player will move with player
		 *  but the frog won't go off the screen with the rideableObject
		 */
		if(onRideableObject != null) {
			if(!super.collides(onRideableObject)) {
				onRideableObject = null;
			} else {
				move(onRideableObject.getSpeed() * delta * (onRideableObject.getMoveRight() ? 1 : -1), 0);
				if (!onScreenX(getX())) {
					setX(onRideableObject.getMoveRight() ? App.SCREEN_WIDTH - getImageWidth() / 2 : getImageWidth() / 2);
				}
			}
		}
	}
	
	private void checkSolidObject (int delta) {
		// SolidObject will push player until player leave the screen and lose a life
		if (solidObject == null || !super.collides(solidObject)) {
			solidObject = null;
		} else {
			move(solidObject.getSpeed() * delta * (solidObject.getMoveRight() ? 1 : -1), 0);
			if (!onScreenX(getX())) {
				loseOneLife();
			}
		}
	}
	private void updateBound() {
		// update all extra bounds' x and y position
		upBound.setX(getX());
		upBound.setY(getY() - getImageHeight());
		downBound.setX(getX());
		downBound.setY(getY() + getImageHeight());
		leftBound.setX(getX() - getImageWidth());
		leftBound.setY(getY());
		rightBound.setX(getX() + getImageWidth());
		rightBound.setY(getY());
	}
	
	private void arriveHole() {
		// detect the player arrive holes in y position
		if(getY() != World.HOLE_Y) {
			return;
		} else {
			// if the hole player arrived has already a player icon, lose a life or create a player icon in it 
			for(int i = 0; i < World.HOLE_X_RANGE.length; i++) {
				if (World.getHoleStatus(i) == false && getX() < World.HOLE_X_RANGE[i][1] 
						&& getX() > World.HOLE_X_RANGE[i][0]) {
					World.arriveHole(i);
					setX(App.SCREEN_WIDTH / 2);
					setY(App.SCREEN_HEIGHT - World.TILE_SIZE);
					playerInHole.add(new Tile(PLAYER_PATH, (World.HOLE_X_RANGE[i][0] + World.HOLE_X_RANGE[i][1]) / 2,
							World.HOLE_Y, new String[] { Sprite.HAZARD }));
					return;
				}
			}
			loseOneLife();
		}
	}
	
	/**
	 * judge whether it and its extra bounds contact other sprite 
	 * @param other the selected sprite to detect whether it contacts player.
	 * @return a value of boolean represents whether they contact each other.
	 */
	@Override
	public boolean collides(Sprite other) {
		if (other.hasTag(Sprite.SOLID)) {
			if (upBound.intersects(other.getBounds())) {
				upSolidSprite = other;
			}
			if (downBound.intersects(other.getBounds())) {
				downSolidSprite = other;
			}
			if (leftBound.intersects(other.getBounds())) {
				leftSolidSprite = other;
			}
			if (rightBound.intersects(other.getBounds())) {
				rightSolidSprite = other;
			}
		}
		return super.collides(other);
	}
	
	/**
	 * According to contacted sprite's tags, do corresponding response
	 * @param other the sprite is contacted.
	 */
	@Override
	public void onCollision(Sprite other) {
		// contacted rideable sprite, save this sprite to onRideableObject
		if (other.hasTag(Sprite.RIDE)) {
			onRideableObject = (RideableObject)other;
		}
		// contacted a sprite with tag hazard and not in a rideable sprite, lose a life
		if (other.hasTag(Sprite.HAZARD)) {
			if (onRideableObject == null || !onRideableObject.hasTag(Sprite.RIDE)) {
				loseOneLife();
			}	
		}
		// contacted an extraLife and it is visible, add a life
		if (other.hasTag(Sprite.EXTRA_LIFE)) {
			if (((ExtraLife)other).getVisible() == true) {
				((ExtraLife)other).setVisible(false);
				addOneLife();
			}
		}
		/* contacted a solid sprite, save this sprite to solidObject
		 */
		if (other.hasTag(Sprite.SOLID)) {
			solidObject = (MovingObject)other;
		}
	}
	
	private void addOneLife() {
		World.setNumLives(World.getNumLives() + 1);
		lives.add(Tile.createLifeTile(LIFE_ICON_X + lives.size() * LIFE_ICON_INTERVAL_X, LIFE_ICON_Y));
	}
		
	private void loseOneLife() {
		// lose one life and if the number of lives equals 0, game over.
		World.setNumLives(World.getNumLives() - 1);
		if(World.getNumLives() == 0) {
			System.exit(0);
		}
		
		// remove lives remaining
		lives.remove(lives.size() - 1);
		lives.trimToSize();
		
		// reset player position
		setX(World.INITIAL_X_PLAYER);
		setY(World.INITIAL_Y_PLAYER);
	}

	/**
	 * Render the player, lives remaining, players in the holes
	 */
	@Override
	public void render() {
		super.render();
		
		// render lives
		for (Sprite temp : lives) {
			temp.render();
		}
		
		// render players in the holes
		for (Sprite temp : playerInHole) {
			temp.render();
		}
	}
	
}
