package WheelOfFortune.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


/**
 * Wheel class for wheel of fortune game. Most of this class is mathematical functions.
 * This class extends JComponent to override the paintComponent of JComponent with custom painting.
 *
 * @author Harry Bridgen
 * @version 1.0
 */
public class Wheel extends JComponent {
	double rotateByRadians;
	int sectorLanded;
	int radius;
	int panelBorder;
	int imageX;
	int imageY = 3;
	int centreX;
	int centreY;
	int xPower2;
	int yPower2;
	int rPower2;
	BufferedImage wheelImage;

	/**
	 * Constructor loads the image of the wheel and sets the radius of the wheel.
	 */
	public Wheel() {
		wheelImage = loadImage();
		radius = wheelImage.getWidth() / 2;
	}

	/**
	 * Calculates the image X coordinate (top left corner of the wheel image) and
	 * the centre x and y coordinates of the wheel.
	 * <p>
	 * The imageX value takes into consideration the natural borders that JPanels have.
	 * This is why we have int JPanelBorder {@code ((guiWidth - contentPanelWidth) / 2)}.
	 * <p>The two centre values of the wheel are calculated by taking the imageX and
	 * image Y and adding the radius of the circle.</p>
	 *
	 * @param guiWidth          width of the JFrame
	 * @param contentPanelWidth Width of the JPanel containing the wheel
	 * @see <a href="https://i.imgur.com/7GqimzD.png">Wheel variables and JPanel calculations</a>
	 */
	public void setPosition(int guiWidth, int contentPanelWidth) {
		panelBorder = (guiWidth - contentPanelWidth) / 2;
		imageX = (guiWidth / 2) - radius - panelBorder;

		centreX = imageX + radius;
		centreY = imageY + radius;
	}

	/**
	 * Detects if the wheel has been clicked.
	 * <p>Function is used inside an action listener and takes in the x and y values of the click
	 * as parameters.</p>
	 * <p>Uses the equation of a circle, the centre coordinates of the wheel and the radius of the wheel, to detect
	 * if the clicked x and y coordinates are within the wheel.</p>
	 * <p>Returns true if the user clicks within the wheel.</p>
	 *
	 * @param clickX X position of the users click
	 * @param clickY Y position of the users click
	 * @return True/false (Boolean)
	 */
	public boolean detectClicked(int clickX, int clickY) {
		xPower2 = (clickX - centreX) * (clickX - centreX);
		yPower2 = (clickY - centreY) * (clickY - centreY);
		rPower2 = radius * radius;
		return xPower2 + yPower2 <= rPower2;
	}

	/**
	 * <p>Calculates the rotation of a new wheel image within the spin animation.
	 *  Also, calculates which sector landed at 90 degrees.</p>
	 * <p>Converts the y value of a mathematical function into radians.</p>
	 * <p>Then take radians mod PI * 2.
	 * This is to make sure the {@link Wheel#rotateByRadians} value doesn't increase
	 * infinitely by resetting back to 0 when the wheel makes a full revolution.</p>
	 * <p>The {@link Wheel#sectorLanded} is calculated by taking the floor value of radians rotated / PI/4.
	 * This is because the wheel is divided up into PI/4 sized sectors.</p>
	 *
	 * @param y Y value of a mathematical function
	 */
	public void rotate(double y) {
		rotateByRadians += Math.toRadians(y);
		rotateByRadians = rotateByRadians % (Math.PI * 2);
		sectorLanded = (int) Math.floor(rotateByRadians / (Math.PI / 4));
	}

	/**<p>Overrides the JComponent paintComponent.</p>
	 * Transforms the wheel image by rotating it by the class variable {@link Wheel#rotateByRadians}.
	 * Calling the function {@link Wheel#rotate(double)} before painting the wheel in
	 * a loop gives the illusion that the wheel is spinning.
	 * @param g graphics
	 */
	@Override
	public void paintComponent(Graphics g) {
		AffineTransform transform = AffineTransform.getTranslateInstance(imageX, imageY);
		transform.rotate(rotateByRadians, radius, radius);
		Graphics2D graphics2D = (Graphics2D) g;
		graphics2D.drawImage(wheelImage, transform, null);
	}

	/**Loads the image of the wheel and sets it as a class variable.
	 * @return Image of the wheel used in the game
	 */
	BufferedImage loadImage() {
		wheelImage = null;
		try {
			wheelImage = ImageIO.read(Objects.requireNonNull(GUI.class.getResource("assets/wheel.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wheelImage;
	}
}


