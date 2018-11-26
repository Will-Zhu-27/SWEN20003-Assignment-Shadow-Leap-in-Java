import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a special sprite, turtle. Every 9 seconds, it disappears for 2 seconds.
 */
public class Turtle extends RideableObject {
	private static final String TURTLE_PATH = "assets/turtles.png";
	private static final float TURTLE_SPEED = 0.085f;
	private static final long DISAPEAR_FREQUENCY_MILLISECOND = 7000;
	private static final long DISAPEAR_MILLISECOND = 2000;
	
	private boolean visible;
	private Timer underwaterTimer;
	
	/** 
     * Initialize an object of class Turtle and set its configuration.
     * @param x x position.
     * @param y y position.
     * @param moveRight represents the direction of movement.
     */
	public Turtle(float x, float y, boolean moveRight) {
		super(TURTLE_PATH, x, y, TURTLE_SPEED, moveRight);
		visible = true;
		underwaterTimer = new Timer();
		// start the loop
		underwaterTimer(DISAPEAR_FREQUENCY_MILLISECOND, DISAPEAR_MILLISECOND);
	}
	
	private void underwaterTimer(long disapearFrequency, long disapearTime) {
		underwaterTimer.schedule(new TimerTask () {
			public void run() {
				// turtle appears
				visible = true;
				addTag(Sprite.RIDE);
				deleteTag(Sprite.HAZARD);
				// set a timer to make turtle underwater
				timer(DISAPEAR_FREQUENCY_MILLISECOND);
			}
		}, 0, disapearFrequency + disapearTime);
	}
	
	private void timer(long period) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				// turtle disappears
				visible = false;
				deleteTag(Sprite.RIDE);
				addTag(Sprite.HAZARD);
			}
		}, period);
	}
	
	@Override
	public void render() {
		if (visible == false) {
			return;
		} else {
			super.render();
		}
	}
}