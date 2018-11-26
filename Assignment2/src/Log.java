/**
 * Realizes two sprites, log and long log.
 */
public class Log extends RideableObject {
	private static final String LOG_PATH = "assets/log.png";
	private static final String LONGLOG_PATH = "assets/longlog.png";
	private static final float LOG_SPEED = 0.1f;
	private static final float LONGLOG_SPEED = 0.07f;
	
    /** 
     * Initialize an object of class Log and set its configuration.
     * @param imageSrc the source path of image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @param moveRight represents the direction of movement.
     */
	public Log(String imageSrc, float x, float y, float speed, boolean moveRight) {
		super(imageSrc, x, y, speed, moveRight);
	}
	
    /** 
     * Create a Log object using log image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @return a new object of Log using log image.
     */
	public static Log createLog(float x, float y, boolean moveRight) {
		return new Log(LOG_PATH, x, y, LOG_SPEED, moveRight);
	}
	
    /** 
     * Create a Log object using long log image.
     * @param x x position.
     * @param y y position.
     * @param speed the speed of movement.
     * @return a new object of Log using long log image.
     */
	public static Log createLongLog(float x, float y, boolean moveRight) {
		return new Log(LONGLOG_PATH, x, y, LONGLOG_SPEED, moveRight);
	}
}