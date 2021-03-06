import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EnemyChimp extends PlayerMonkey {
	
	private static final double INITIAL_HEALTH = 50;
	private static final double HEIGHT = 50;
	private static final double WIDTH = 40;
	
	private double myHealth;
	private double myXPos;
	private double myYPos;
	
	public EnemyChimp(double width, double height) {
		myHealth = INITIAL_HEALTH;
		myXPos = (Math.random()*width/2)+width/2;
		myYPos = Math.random()*height/2;
	}
	
	public ImageView getImageView(String imageName) {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(imageName));
		ImageView chimp = new ImageView(image);
		chimp.setX(myXPos);
        chimp.setY(myYPos);
        chimp.setFitHeight(HEIGHT);
        chimp.setFitWidth(WIDTH);
        return chimp;
	}
	
	public double getHeight() {
		return HEIGHT;
	}
	
	public double getWidth() {
		return WIDTH;
	}
	
	public void updateHealth(double damage) {
		myHealth -= damage;
	}
	
	public double getHealth() {
		return myHealth;
	}
}
