import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerMonkey {
	
	private static final double INITIAL_HEALTH = 100;
	private static final double HEIGHT = 70;
	private static final double WIDTH = 90;
	
	private double myHealth;
	private double myXPos;
	private double myYPos;
	
	public PlayerMonkey() {
		myHealth = INITIAL_HEALTH;
		myXPos = 400;
		myYPos = 400;
	}
	
	public PlayerMonkey(double width, double height) {
		myHealth = INITIAL_HEALTH;
		myXPos = width*4/3;
		myYPos = height/2;
	}
	
	public ImageView getImageView(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		ImageView monkey = new ImageView(image);
		myXPos -= monkey.getBoundsInLocal().getWidth();
		monkey.setX(myXPos);
        monkey.setY(myYPos);
        monkey.setFitHeight(HEIGHT);
        monkey.setFitWidth(WIDTH);
        return monkey;
	}
	
	public void updateHealth(double damage) {
		myHealth -= damage;
	}
	
	public void setLosingHealth() {
		myHealth = 0;
	}
	
	public double getHealth() {
		return myHealth;
	}
	
	public double getHeight() {
		return HEIGHT;
	}
	
	public double getWidth() {
		return WIDTH;
	}

}
