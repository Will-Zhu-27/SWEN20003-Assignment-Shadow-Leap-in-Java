import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.Input;

/**
 * Represents a special sprit, extraLife.
 * It will appear on the center of random log or long log object after a random number of seconds between 25 and 35.
 * It first attempts to move right, one tile every 2 seconds.
 * When the player touches it, it will disappear and player's life number adds one.
 * After 14 seconds the extra life object should disappear, then this process will repeat.
 */
public class ExtraLife extends MovingObject {
	private static final String EXTRA_LIFE_PATH = "assets/extralife.png";
	private static final int RANDOM_SECOND_FROM = 25;
	private static final int RANDOM_SECOND_TO = 35;
	private static final int MOVE_FREQUENCY_MILLISECOND = 2000;
	private static final float MOVE_DISTANCE = World.TILE_SIZE;
	private static final int LOOP_PERIOD_MILLISECOND = 14000;
	
	private boolean visible;
	private Log onLog;
	private Timer startTimer;
	private Timer moveTimer;
	private Timer restartTimer;
	// the relative x position on the selected log object
	private float relativeX;
	
    /** 
     * Initialize an object of the class ExtraLife and set its configuration
     */
	public ExtraLife() {
		super(EXTRA_LIFE_PATH, 0, 0, 0, true, new String[] {Sprite.EXTRA_LIFE});
		visible = false;
		relativeX = 0;
		startTimer = new Timer();
		
		// start the loop
		startTimer(getStartSeconds() * 1000, LOOP_PERIOD_MILLISECOND);
	}
	
	private void startTimer(long delay, long period) {
		// set a timer to start the loop
		startTimer.schedule(new TimerTask() {
			public void run() {
				moveTimer = new Timer();
				if (restartTimer != null) {
					restartTimer.cancel();
				}
				restartTimer = new Timer();
				restartTimer(period);
				relativeX = 0;
				getOnLog(World.getSprites());
				visible = true;
				moveTimer(MOVE_FREQUENCY_MILLISECOND);
				startTimer.cancel();
			}
		}, delay);
	}
	
	private void restartTimer(long delay) {
		// set a timer to restart the loop
		restartTimer.schedule(new TimerTask() {
			public void run() {
				visible = false;
				if(moveTimer != null) {
					moveTimer.cancel();
				}
				if(startTimer != null) {
					startTimer.cancel();
				}	
				startTimer = new Timer();
				startTimer(getStartSeconds() * 1000, LOOP_PERIOD_MILLISECOND);
			}
		}, delay);
	}
	
	private int getStartSeconds() {
		return new Random().nextInt(RANDOM_SECOND_TO - RANDOM_SECOND_FROM + 1) + RANDOM_SECOND_FROM;
	}
	
	private void getOnLog(ArrayList<Sprite> sprites) {
		/* select a log or long log object from World.sprites randomly,
		   set coordinate based on selected log object. */
		ArrayList<Sprite> logs = new ArrayList<Sprite> ();
		for (Sprite temp : sprites) {
			if (temp.getClass().getName() == "Log") {
				logs.add(temp);
			}
		}
		onLog = (Log)logs.get(new Random().nextInt(logs.size()));
		setX(onLog.getX());
		setY(onLog.getY());
	}
	
    /** 
     * Set the visible state of this extraLife object
     * @param visible the value of the visible state
     */
	public void setVisible(boolean visible) {
		// when the visible becomes true to false, ExtraLife restarts.
		if(visible == false && this.visible == true) {
			if(restartTimer != null) {
				restartTimer.cancel();
			}
			restartTimer = new Timer();
			restartTimer(0);
		}
		this.visible = visible;
	}
	
	/**
	 * Accesses the visible state of the extraLife object.
	 * @return	the visible state of the extraLife object.
	 */
	public boolean getVisible() {
		return visible;
	}
	
	private void moveTimer(long period) {
		// set a timer on a fixed frequency to move and make sure it always contacts Onlog
		moveTimer.schedule(new TimerTask() {
			public void run() {
				if (Math.abs(relativeX + MOVE_DISTANCE * (getMoveRight() ? 1 : -1)) > onLog.getImageWidth() / 2) {
					setMoveRight(!getMoveRight());
					relativeX += MOVE_DISTANCE * (getMoveRight() ? 1 : -1);
				} else {
					relativeX += MOVE_DISTANCE * (getMoveRight() ? 1 : -1);
				}
			}
		}, period, period);
	}
	
	@Override
	public void update(Input input, int delta) {
		// only update it when visible is true
		if (visible == false) {
		} else {
			setX(onLog.getX() + relativeX);
		}
	}
	
	/**
	 * Render the sprite when it is visible.
	 */
	@Override
	public void render() {
		// only render it when visible is true
		if (visible == false) {
			return;
		} else {
			super.render();
		}
	}
}