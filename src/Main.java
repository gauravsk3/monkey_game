
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main program, executes the game
 * 
 * @author gauravkumar
 */
public class Main extends Application {
	private static MonkeyGame myFirstLevel;
	private static ApeGame mySecondLevel;
	public static final int SIZE = 800;
	public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	/**
	 * @Override from Application class
	 */
	public void start(Stage s) {
		myFirstLevel = new MonkeyGame();
		s.setTitle(myFirstLevel.getTitle());
		
		Scene scene = myFirstLevel.init(SIZE, SIZE,s);
        s.setScene(scene);
        s.show();
        
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), 
        		e -> myFirstLevel.step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
        
        KeyFrame enemyFrame = new KeyFrame(Duration.millis(20*MILLISECOND_DELAY), 
        		e -> myFirstLevel.enemyStep(SECOND_DELAY*600));
        Timeline enemyAnimation = new Timeline();
        enemyAnimation.setCycleCount(Timeline.INDEFINITE);
        enemyAnimation.getKeyFrames().add(enemyFrame);
        enemyAnimation.play();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
