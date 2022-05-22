package WheelOfFortune.Game;

import javax.swing.*;
import java.awt.*;

/**
 *JButton constructor class that allows the use of multiple buttons that share the same properties.
 *@author Harry Bridgen
 *@version 1.0
 */
public class GameButton extends JButton {

    /**Constructs the GameButton.
     * @param text The text to display on the button (String)
     */
    public GameButton(String text) {
        this.setText(text);
        this.setFont(new Font("", Font.BOLD, 20));
        this.setBackground(new Color(255, 255, 255));
        this.setPreferredSize(new Dimension(200, 90));
    }
}