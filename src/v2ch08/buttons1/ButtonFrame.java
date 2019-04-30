package v2ch08.buttons1;

import javax.swing.*;

/**
 * A frame with a button panel.
 */
public class ButtonFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    private JPanel jPanel;
    private JButton yellowButton;
    private JButton blueButton;
    private JButton redButton;

    public ButtonFrame() {
        setSize(DEFAULT_HEIGHT, DEFAULT_HEIGHT);
        jPanel = new JPanel();
        jPanel.setName("panel");
        add(jPanel);

        yellowButton = new JButton("Yellow");
        yellowButton.setName("yellowButton");
        blueButton = new JButton("Blue");
        blueButton.setName("blueButton");
        redButton = new JButton("Red");
        redButton.setName("redButton");

        jPanel.add(yellowButton);
        jPanel.add(blueButton);
        jPanel.add(redButton);
    }
}
