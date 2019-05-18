package v1ch02.ImagevViewer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageViewer {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
//            JFrame frame = new JFrame();
            JFrame frame = new ImageViewerFrame();
            frame.setTitle("ImageViewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

class ImageViewerFrame extends JFrame{
//    private JPanel panel;
    private JLabel label;
//    private JMenuBar menuBar;
    private JFileChooser chooser;
//    private JMenu menu;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 400;

//    ImageViewerFrame
    public ImageViewerFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

//        JLabel label = new JLabel();
        label = new JLabel();
        add(label);

//        JFileChooser chooser = new JFileChooser();
        chooser = new JFileChooser();
//        chooser.setCurrentDirectory(".");
        chooser.setCurrentDirectory(new File("."));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

//        JMenu menu = new JMenu();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem openItem = new JMenuItem("Open");
        menu.add(openItem);
        openItem.addActionListener(event -> {
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
//                label.setIcon
//                String name = chooser.getSelectedFile().getName();
                String name = chooser.getSelectedFile().getPath();
                label.setIcon(new ImageIcon(name));
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
//        exitItem.addActionListener(event -> System.exit());
        exitItem.addActionListener(event -> System.exit(0));
    }

}
