import java.awt.List;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.sun.glass.ui.Timer;
import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

public class MonkeyGame {
	public static final String TITLE = "Monkey Business";
	public static final int KEY_INPUT_SPEED = 20;
	private static int NUM_ENEMIES = 5;
	private static double ROCK_VELOCITY = 100;
	public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	
	private Stage myStage;
	private int myWidth;
	private int myHeight;
	private Group myRoot;
	private Scene myScene;
	private PlayerMonkey myMonkey;
	private ArrayList<EnemyChimp> myEnemies = new ArrayList<EnemyChimp>();
	private ArrayList<ImageView> myEnemiesImage = new ArrayList<ImageView>();
	private ImageView myMonkeyImage;
	private ImageView myBackground;
	private Label myHealth;
	private ArrayList<Circle> myRocks = new ArrayList<Circle>();
	private ArrayList<Circle> myEnemyRocks = new ArrayList<Circle>();
	private Label myResult;

	public String getTitle() {
		return TITLE;
	}
	
	public Scene init(int width, int height, Stage s) {
		myRoot = new Group();
		myStage = s;
		myWidth = width;
		myHeight = height;
		myScene = new Scene(myRoot, myWidth, myHeight);
		
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("forestBackground.jpg"));
        myBackground = new ImageView(image);
        myBackground.setX(0);
        myBackground.setY(0);
        myBackground.setFitHeight(height);
        myBackground.setFitWidth(width);
        myRoot.getChildren().add(myBackground);
		
        myMonkey = new PlayerMonkey(width,height);
		myMonkeyImage = myMonkey.getImageView("orangutan.jpg");
        myRoot.getChildren().add(myMonkeyImage);
        
        for ( int i = 0; i < NUM_ENEMIES; i++) {
        	myEnemies.add(new EnemyChimp(width,height));
        	myEnemiesImage.add(myEnemies.get(i).getImageView("chimpanzee.jpg"));
        	myRoot.getChildren().add(myEnemiesImage.get(i));
        }
        
        myHealth = makeHealthLabel();
        myRoot.getChildren().add(myHealth);
        
        myScene.setOnKeyPressed(e -> playerMonkeyKeys(e.getCode()));
		
		return myScene;
	}
	
	private Label makeHealthLabel(){
		Label health = new Label("Current Health: " + myMonkey.getHealth() + " HP");
        health.setTextFill(Color.RED);
        health.setMaxSize(3000, 3000);
        health.setFont(Font.font("Verdana",FontWeight.BOLD, 20));
        health.setTranslateY(25);
        health.setTranslateX(150);
        return health;
	}
	
	private Label makeVictoryLabel(boolean didWin) {
		String messageToPrint;
		if ( didWin )
			messageToPrint = "YOU WIN LEVEL 1!";
		else
			messageToPrint = "YOU LOSE. TRY AGAIN!";
		Label result = new Label(messageToPrint);
		result.setTextFill(Color.RED);
		result.setMaxSize(10000,10000);
		result.setFont(Font.font("Verdana",FontWeight.BOLD, 50));
		result.setTranslateY(myHeight/2);
		result.setTranslateX(0);
		return result;
	}
	
	public void step(double elapsedTime) {
		java.util.Iterator<Circle> rockIter = myRocks.iterator();
		while (rockIter.hasNext()) {
			Circle rock = rockIter.next();
			rock.setCenterX(rock.getCenterX() + ROCK_VELOCITY*elapsedTime);
			if ( rock.getCenterX() == myWidth) {
				rockIter.remove();
				myRoot.getChildren().remove(rock);
			}
			else {
				java.util.Iterator<ImageView> enemyImageIter = myEnemiesImage.iterator();
				java.util.Iterator<EnemyChimp> enemyIter = myEnemies.iterator();
				while (enemyImageIter.hasNext()) {
					ImageView enemyImage = enemyImageIter.next();
					EnemyChimp enemy = enemyIter.next();
					if ( enemyImage.getBoundsInParent().intersects(rock.getBoundsInParent())) {
						 enemy.updateHealth(rock.getRadius());
						if ( enemy.getHealth() <= 0) {
							myRoot.getChildren().remove(enemyImage);	
							enemyImageIter.remove();
							enemyIter.remove();
						}
						myRoot.getChildren().remove(rock);
						if (myEnemies.size() <= 0)
							endLevel();
					}
				}
			}
		}
		if ( myEnemies.size() > 0) {
			java.util.Iterator<Circle> enemyRockIter = myEnemyRocks.iterator();
			while(enemyRockIter.hasNext()) {
				Circle rock = enemyRockIter.next();
				rock.setCenterX(rock.getCenterX() - ROCK_VELOCITY*elapsedTime);
				if ( rock.getCenterX() == 0) {
					enemyRockIter.remove();
					myRoot.getChildren().remove(rock);
				}
				else {
					if ( myMonkeyImage.getBoundsInParent().intersects(rock.getBoundsInParent())) {
						myMonkey.updateHealth(rock.getRadius());
						if ( myMonkey.getHealth() <= 0)
							myMonkey.setLosingHealth();
						enemyRockIter.remove();
						myRoot.getChildren().remove(rock);
						myRoot.getChildren().remove(myHealth);
						myHealth = makeHealthLabel();
						myRoot.getChildren().add(myHealth);
						if ( myMonkey.getHealth() <= 0)
							endLevel();
					}
				}
			}
		}
	}
	
	private void endLevel(){
		if ( myMonkey.getHealth() <= 0)
			myResult = makeVictoryLabel(false);	
		else
			myResult = makeVictoryLabel(true);
		myRoot.getChildren().add(myResult);
		
		ApeGame secondLevel = new ApeGame();
		
		Scene secondScene = secondLevel.init(myWidth, myHeight);
        myStage.setScene(secondScene);
        myStage.show();
        
        KeyFrame secondFrame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), 
        		e -> secondLevel.step(SECOND_DELAY));
        Timeline secondAnimation = new Timeline();
        secondAnimation.setCycleCount(Timeline.INDEFINITE);
        secondAnimation.getKeyFrames().add(secondFrame);
        secondAnimation.play();
        
        KeyFrame bossFrame = new KeyFrame(Duration.millis(20*MILLISECOND_DELAY), 
        		e -> secondLevel.apeStep(SECOND_DELAY*400));
        Timeline bossAnimation = new Timeline();
        bossAnimation.setCycleCount(Timeline.INDEFINITE);
        bossAnimation.getKeyFrames().add(bossFrame);
        bossAnimation.play();
	}
	
	public void enemyStep(double elapsedTime) {
		for ( int i = 0; i < myEnemies.size(); i++) {
			enemyMonkeyMovement(i);
		}
	}
	
	private void enemyMonkeyMovement(int i) {
		int option = (int) (1000*Math.random());
		if ( option >= 0 && option < 240) {
			if ( myEnemiesImage.get(i).getX() < myWidth-2*myEnemies.get(i).getWidth() )
				myEnemiesImage.get(i).setX(myEnemiesImage.get(i).getX() + KEY_INPUT_SPEED*2);
		}
		if ( option >= 240 && option < 480) {
			if ( myEnemiesImage.get(i).getX() > myWidth/2+myEnemies.get(i).getWidth())
				myEnemiesImage.get(i).setX(myEnemiesImage.get(i).getX() - KEY_INPUT_SPEED*2);
		}
		if ( option >= 480 && option < 720) {
			if ( myEnemiesImage.get(i).getY()-myEnemies.get(i).getHeight() > 0 )
				myEnemiesImage.get(i).setY(myEnemiesImage.get(i).getY() - KEY_INPUT_SPEED*2);
		}
        if ( option >= 720 && option < 960) {
        	if ( myEnemiesImage.get(i).getY() < myHeight-2*myEnemies.get(i).getHeight() )
        		myEnemiesImage.get(i).setY(myEnemiesImage.get(i).getY() + KEY_INPUT_SPEED*2);
        }
        if ( option >= 960 && option < 1000) {
        	Circle rock = new Circle(myEnemiesImage.get(i).getX(), myEnemiesImage.get(i).getY() 
        			- myEnemiesImage.get(i).getFitHeight()/2, 8*Math.random() + 3);
        	myEnemyRocks.add(rock);
        	myRoot.getChildren().add(rock);
        }
	}
	
	// What to do each time a key is pressed
    private void playerMonkeyKeys (KeyCode code) {
        switch (code) {
            case RIGHT:
            	if ( myMonkeyImage.getX() < myWidth/2-myMonkey.getWidth() )
            		myMonkeyImage.setX(myMonkeyImage.getX() + KEY_INPUT_SPEED);
                break;
            case LEFT:
            	if ( myMonkeyImage.getX() > 0)
            		myMonkeyImage.setX(myMonkeyImage.getX() - KEY_INPUT_SPEED);
                break;
            case UP:
            	if ( myMonkeyImage.getY() > 0 )
            		myMonkeyImage.setY(myMonkeyImage.getY() - KEY_INPUT_SPEED);
                break;
            case DOWN:
            	if ( myMonkeyImage.getY() < myHeight-myMonkey.getHeight() )
            		myMonkeyImage.setY(myMonkeyImage.getY() + KEY_INPUT_SPEED);
                break;
            case SPACE:
            	Circle rock = new Circle(myMonkeyImage.getX()+myMonkeyImage.getFitWidth(), 
            			myMonkeyImage.getY() + myMonkeyImage.getFitHeight()/2, 8*Math.random() + 3);
            	myRocks.add(rock);
            	myRoot.getChildren().add(rock);
            	break;
            default:
                // do nothing
        }
    }

}
