import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Banana {
	
	public static double DEFAULT_VELOCITY = 10;
	
	private double myHitPoints;
	private double myXPos;
	private double myYPos;
	private double myVel;
	
	public Banana(double xPos, double yPos) {
		myHitPoints = 10*Math.random();
		myXPos = xPos;
		myYPos = yPos;
		myVel = DEFAULT_VELOCITY;
	}
	
	public Banana(double xPos, double yPos, double velocity) {
		myHitPoints = 10*Math.random();
		myXPos = xPos;
		myYPos = yPos;
		myVel = velocity;
	}
	
	public ImageView getImageView(){
		Image image = new Image(getClass().getClassLoader().getResourceAsStream("banana.jpg"));
		ImageView banana = new ImageView(image);
		banana.setX(myXPos+70);
        banana.setY(myYPos+35);
        banana.setFitHeight(20);
        banana.setFitWidth(50);
        return banana;
	}
	
	public void setNewXPos(){
		myXPos += DEFAULT_VELOCITY;
	}
	
	public double getXPos(){
		return myXPos;
	}
	
	public double getHitPoints() {
		return myHitPoints;
	}
	
}
