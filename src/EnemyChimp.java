import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EnemyChimp {
	
	private static final double INITIAL_HEALTH = 20;
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
	
	public ImageView getImageView() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("chimpanzee.jpg"));
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
}
