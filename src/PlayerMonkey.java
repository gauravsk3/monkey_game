import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PlayerMonkey {
	
	private static final double INITIAL_HEALTH = 100;
	private static final double HEIGHT = 70;
	private static final double WIDTH = 90;
	
	private double myHealth;
	private double myXPos;
	private double myYPos;
	
	public PlayerMonkey(double width, double height) {
		myHealth = INITIAL_HEALTH;
		myXPos = width/2;
		myYPos = height/2;
	}
	
	public ImageView getImageView() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("orangutan.jpg"));
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
