/**
 * Represents a sprite which doesn't move.
 */
public class Tile extends Sprite {
	private static final String GRASS_PATH = "assets/grass.png";
	private static final String WATER_PATH = "assets/water.png";
	private static final String TREE_PATH = "assets/tree.png";
	private static final String LIFE_PATH = "assets/lives.png";
	
	 /** 
     * Create a Tile object using grass image.
     * @param x x position.
     * @param y y position.
     * @return a new object of Tile using grass image.
     */
	public static Tile createGrassTile(float x, float y) {
		return new Tile(GRASS_PATH, x, y);
	}
	
	 /** 
     * Create a Tile object using water image.
     * @param x x position.
     * @param y y position.
     * @return a new object of Tile using water image.
     */
	public static Tile createWaterTile(float x, float y) {
		return new Tile(WATER_PATH, x, y, new String[] { Sprite.HAZARD });
	}
	
	/** 
     * Create a Tile object using tree image.
     * @param x x position.
     * @param y y position.
     * @return a new object of Tile using tree image.
     */
	public static Tile createTreeTile(float x, float y) {
		return new Tile(TREE_PATH, x, y, new String[] { Sprite.SOLID });
	}
	
	/** 
     * Create a Tile object using lives image.
     * @param x x position.
     * @param y y position.
     * @return a new object of Tile using lives image.
     */
	public static Tile createLifeTile(float x, float y) {
		return new Tile(LIFE_PATH, x, y);
	}
	
	/** 
     * Initialize an object of class Tile and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     */
	public Tile(String imageSrc, float x, float y) {		
		super(imageSrc, x, y);
	}
	
	/** 
     * Initialize an object of class Tile and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param tags target's tags.
     */
	public Tile(String imageSrc, float x, float y, String[] tags) {		
		super(imageSrc, x, y, tags);
	}
}