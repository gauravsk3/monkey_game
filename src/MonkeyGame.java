import java.awt.List;
import java.util.ArrayList;

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

public class MonkeyGame {
	public static final String TITLE = "Monkey Business";
	public static final int KEY_INPUT_SPEED = 20;
	private static double DEFAULT_BANANA_VELOCITY = 10;
	private static int NUM_ENEMIES = 4;
	private static double ENEMY_BANANA_VELOCITY = 3;
	
	private int myWidth;
	private int myHeight;
	private Group myRoot;
	private Scene myScene;
	private PlayerMonkey myMonkey;
	private EnemyChimp[] myEnemies = new EnemyChimp[NUM_ENEMIES];
	private ImageView[] myEnemiesImage = new ImageView[NUM_ENEMIES];
	private ImageView myMonkeyImage;
	private ImageView myBackground;
	private Label myHealth;
	private ArrayList<Banana> myBananas = new ArrayList<Banana>();
	private ArrayList<Banana> myEnemyBananas = new ArrayList<Banana>();

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
		myMonkeyImage = (myMonkey).getImageView();
        myRoot.getChildren().add(myMonkeyImage);
        
        for ( int i = 0; i < NUM_ENEMIES; i++) {
        	myEnemies[i] = new EnemyChimp(width,height);
        	myEnemiesImage[i] = myEnemies[i].getImageView();
        	myRoot.getChildren().add(myEnemiesImage[i]);
        }
        
        myHealth = new Label("Current Health: " + myMonkey.getHealth() + " HP");
        myHealth.setTextFill(Color.RED);
        myHealth.setMaxSize(3000, 3000);
        myHealth.setFont(Font.font("Verdana",FontWeight.BOLD, 20));
        myHealth.setTranslateY(25);
        myHealth.setTranslateX(150);
        myRoot.getChildren().add(myHealth);
        
        myScene.setOnKeyPressed(e -> playerMonkeyKeys(e.getCode()));
		
		return myScene;
	}
	
	public void step(double elapsedTime) {
		//moveReleasedBananas();
	}
	
	public void enemyStep(double elapsedTime) {
		for ( int i = 0; i < NUM_ENEMIES; i++) {
			enemyMonkeyMovement(i);
		}
	}
	
	private void enemyMonkeyMovement(int i) {
		int option = (int) (1000*Math.random());
		if ( option >= 0 && option < 249) {
			if ( myEnemiesImage[i].getX() < myWidth-myEnemies[i].getWidth() )
				myEnemiesImage[i].setX(myEnemiesImage[i].getX() + KEY_INPUT_SPEED);
		}
		if ( option >= 249 && option < 498) {
			if ( myEnemiesImage[i].getX() > myWidth/2+myEnemies[i].getWidth())
				myEnemiesImage[i].setX(myEnemiesImage[i].getX() - KEY_INPUT_SPEED);
		}
		if ( option >= 498 && option < 747) {
			if ( myEnemiesImage[i].getY() > 0 )
				myEnemiesImage[i].setY(myEnemiesImage[i].getY() - KEY_INPUT_SPEED);
		}
        if ( option >= 747 && option < 996) {
        	if ( myEnemiesImage[i].getY() < myHeight-2*myEnemies[i].getHeight() )
        		myEnemiesImage[i].setY(myEnemiesImage[i].getY() + KEY_INPUT_SPEED);
        }
        if ( option >= 996 && option < 1000) {
			Banana b = new Banana(myEnemiesImage[i].getX()-myEnemies[i].getWidth(), 
					myEnemiesImage[i].getY()-myEnemies[i].getHeight(), ENEMY_BANANA_VELOCITY);
        	myEnemyBananas.add(b);
        	myRoot.getChildren().add(b.getImageView());
        }
	}
	
	private void moveReleasedBananas() {
		for (Banana b: myBananas) {
			myRoot.getChildren().remove(b);
			b.getImageView().setImage(null);
			if (b.getXPos() < myWidth) {
				b.setNewXPos();
				b.getImageView().setX(b.getXPos());
				myRoot.getChildren().add(b.getImageView());
			}
			else
				myBananas.remove(b);
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
            	Banana b = new Banana(myMonkeyImage.getX(), myMonkeyImage.getY());
            	myBananas.add(b);
            	myRoot.getChildren().add(b.getImageView());
            	break;
            default:
                // do nothing
        }
    }

}
