import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ApeGame {
	public static final String TITLE = "Monkey Business";
	public static final int KEY_INPUT_SPEED = 20;
	private static double ROCK_VELOCITY = 100;
	
	private int myWidth;
	private int myHeight;
	private Group myRoot;
	private Scene myScene;
	private PlayerMonkey myMonkey;
	private BossApe myBoss;
	private ImageView myBossImage;
	private ImageView myMonkeyImage;
	private ImageView myBackground;
	private Label myHealth;
	private ArrayList<Circle> myRocks = new ArrayList<Circle>();
	private ArrayList<Circle> myEnemyRocks = new ArrayList<Circle>();
	private Label myResult;

	public String getTitle() {
		return TITLE;
	}
	
	public Scene init(int width, int height) {
		myRoot = new Group();
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
        
        myBoss = new BossApe(width,height);
        myBossImage = myBoss.getImageView("ape.jpg");
        myRoot.getChildren().add(myBossImage);
        
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
			messageToPrint = "YOU WON THE FINAL LEVEL!";
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
				if ( myBossImage.getBoundsInParent().intersects(rock.getBoundsInParent())) {
					 myBoss.updateHealth(rock.getRadius());
					if ( myBoss.getHealth() <= 0)
						myRoot.getChildren().remove(myBossImage);
					myRoot.getChildren().remove(rock);
					rockIter.remove();
					if (myBoss.getHealth() <= 0)
						endGame();
				}
			}
		}
		if ( myBoss.getHealth() >= 0) {
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
							endGame();
					}
				}
			}
		}
	}
	
	private void endGame(){
		myRoot.getChildren().remove(myMonkeyImage);
		myRoot.getChildren().remove(myBossImage);
		if ( myMonkey.getHealth() <= 0)
			myResult = makeVictoryLabel(false);	
		else
			myResult = makeVictoryLabel(true);
		myRoot.getChildren().add(myResult);
		java.util.Timer end = new java.util.Timer();
		end.schedule(new TimerTask() {
		    public void run () { 
		    	Platform.exit();
		    	System.exit(0);
		    }
		}, 5000); 
	}
	
	public void apeStep(double elapsedTime) {
		bossMonkeyMovement();
	}
	
	private void bossMonkeyMovement() {
		int option = (int) (1000*Math.random());
		if ( option >= 0 && option < 200) {
			if ( myBossImage.getX() < myWidth-myBoss.getWidth() )
				myBossImage.setX(myBossImage.getX() + KEY_INPUT_SPEED*2);
		}
		if ( option >= 200 && option < 400) {
			if ( myBossImage.getX() > myWidth/2+myBoss.getWidth())
				myBossImage.setX(myBossImage.getX() - KEY_INPUT_SPEED*2);
		}
		if ( option >= 400 && option < 600) {
			if ( myBossImage.getY() > 0 )
				myBossImage.setY(myBossImage.getY() - KEY_INPUT_SPEED*2);
		}
        if ( option >= 600 && option < 800) {
        	if ( myBossImage.getY() < myHeight-2*myBoss.getHeight() )
        		myBossImage.setY(myBossImage.getY() + KEY_INPUT_SPEED*2);
        }
        if ( option >= 800 && option < 1000) {
        	Circle rock = new Circle(myBossImage.getX(), myBossImage.getY() 
        			+ myBossImage.getFitHeight(), 8*Math.random() + 5);
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
