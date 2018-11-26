import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * Fundamental class for the game.
 * Represents everything in the game, including the background and all sprites.
 */
public class World {
	/**
	 * tile size, in pixels
	 */
	public static final int TILE_SIZE = 48;
	/**
	 * the initial x position of the player
	 */
	public static final int INITIAL_X_PLAYER = 512;
	/**
	 * the initial y position of the player
	 */
	public static final int INITIAL_Y_PLAYER = 720;
	/**
	 * the y position of holes
	 */
	public static final float HOLE_Y = 48;
	/**
	 * the x position range of every hole
	 */
	public static final float[][] HOLE_X_RANGE = new float[][] {{72, 168}, {264, 360}, 
		{456, 552}, {648, 744}, {840, 936}};
	
	private static boolean[] holesStatus;
	
	private static final String[] LEVEL_PATH = { "assets/levels/0.lvl", "assets/levels/1.lvl"};
	private static final int INITIAL_LEVEL = 1;
	private static int level = INITIAL_LEVEL;
	
	private static final String DELIMITER = ",";
	
	// correspond to lvl.csv content
	private static final String WATER = "water";
	private static final String GRASS = "grass";
	private static final String TREE = "tree";
	private static final String BUS = "bus";
	private static final String LOG = "log";
	private static final String LONGLOG = "longLog";
	private static final String BULLDOZER = "bulldozer";
	private static final String RACECAR = "racecar";
	private static final String TURTLE = "turtle";
	private static final String BIKE = "bike";
	
	private static final int INITIAL_NUM_LIVES = 3;
	private static int numLives = INITIAL_NUM_LIVES;
		
	private static ArrayList<Sprite> sprites;
	/** 
     * Initialize an object of class World and set its configuration.
     */
	public World() {
		holesStatus = new boolean[HOLE_X_RANGE.length];
		sprites = new ArrayList<>();
		// read level file
		readLevelFile(level);
		// create player
		sprites.add(new Player(INITIAL_X_PLAYER, INITIAL_Y_PLAYER));
		// create extraLife
		sprites.add(new ExtraLife());
	}
	
	/** 
     * Check the status of the selected hole
     * @param holeID the selected hole's ID
     * @return the status of the selected hole
     */
	public static boolean getHoleStatus(int holeID) {
		return holesStatus[holeID];
	}
	
	/** 
     * Access the number of lives
     * @return the number of lives.
     */
	public static int getNumLives() {
		return numLives;
	}
	
	/**
	 * Sets the number of lives.
	 * @param numLives the target number of lives.
	 */
	public static void setNumLives(int numLives) {
		World.numLives = numLives;
	}
	
	/** 
     * Access the sprites
     * @return the sprites.
     */
	public static ArrayList<Sprite> getSprites() {
		return sprites;
	}
	
	/** 
     * Player arrive a hole.
     * @param holeID the selected hole's index
     */
	public static void arriveHole(int holeID) {
		holesStatus[holeID] = true;
	}
	
	/**
	 * Update all sprites and detect whether it upgrades.
	 * @param input A wrapped for all keyboard, mouse and controller input.
	 * @param delta Time passed since last frame (milliseconds).
	 */
	public void update(Input input, int delta) {
		// update all sprites
		for (Sprite sprite : sprites) {
			sprite.update(input, delta);
		}	
		
		// loop over all pairs of sprites and test for intersection
		for (Sprite sprite1: sprites) {
			for (Sprite sprite2: sprites) {
				if (sprite1 != sprite2
						&& sprite1.collides(sprite2)) {
					sprite1.onCollision(sprite2);
				}
			}
		}
		
		// detect whether meet upgrade requirements
		upgrade();
	}
	
	/**
	 * Render all sprites.
	 * @param g The Slick graphics object, used for drawing.
	 */
	public void render(Graphics g) {
		for (Sprite sprite : sprites) {
			sprite.render();
		}
	}
	
	private void readLevelFile(int level) {
		try (BufferedReader br = new BufferedReader(new FileReader(LEVEL_PATH[level - 1]))) {
			String text;
			while((text = br.readLine()) != null) {
				String[] spriteInfo = text.split(DELIMITER);
				sprites.add(addSprite(spriteInfo));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void upgrade() {
		// if all holes have been arrived, entry next level if it exists.
		int i = 0;
		for(boolean temp : holesStatus) {
			if (temp == true) {
				i++;
			}
		}
		if (i != holesStatus.length) {
			return;
		} else {
			level++;
			if (level > LEVEL_PATH.length) {
				System.exit(0);
			} else {
				new World();
			}
		}
	}
	
	private Sprite addSprite(String[] spriteInfo) {
		// create corresponding sprites
		switch(spriteInfo[0]) {
		case WATER: 
			return (Sprite)Tile.createWaterTile(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]));
		case GRASS:
			return (Sprite)Tile.createGrassTile(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]));
		case TREE:
			return (Sprite)Tile.createTreeTile(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]));
		case BUS:
			return (Sprite)new Bus(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]), 
					Boolean.parseBoolean(spriteInfo[3]));
		case RACECAR:
			return (Sprite)new Racecar(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		case BIKE:
			return (Sprite)new Bike(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		case BULLDOZER:
			return (Sprite)new Bulldozer(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		case LOG:
			return (Sprite)Log.createLog(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		case LONGLOG:
			return (Sprite)Log.createLongLog(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		case TURTLE:
			return (Sprite)new Turtle(Float.parseFloat(spriteInfo[1]), Float.parseFloat(spriteInfo[2]),
					Boolean.parseBoolean(spriteInfo[3]));
		default:
			return null;
		}
	}
}