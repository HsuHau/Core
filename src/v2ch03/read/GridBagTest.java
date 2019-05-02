package v2ch03.read;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * This program shows how to use an XML file to describe a gridbag layout.
 */
public class GridBagTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFileChooser chooser = new JFileChooser(".");
            chooser.showOpenDialog(null);
            File file = chooser.getSelectedFile();
            JFrame frame = new FontFrame(file);
            frame.setTitle("GridBagTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * This frame contains a font selection dialog that is described by an XML file.
 */
class FontFrame extends JFrame {
    private GridBagPane gridBagPane;
    private JComboBox<String> face;
    private JComboBox<String> size;
    private JCheckBox bold;
    private JCheckBox italic;

    //@SuppressWarnings("unchecked")
    public FontFrame(File file) {
        gridBagPane = new GridBagPane(file);
        add(gridBagPane);

        face = (JComboBox<String>) gridBagPane.get("face");
        size = (JComboBox<String>) gridBagPane.get("size");
        bold = (JCheckBox) gridBagPane.get("bold");
        italic = (JCheckBox) gridBagPane.get("italic");

        face.setModel(new DefaultComboBoxModel<String>(new String[] {
                "Serif", "SansSerif", "Monospaced", "Dialog", "DialogInput"
        }));

        size.setModel(new DefaultComboBoxModel<String>(new String[] {
                "8", "10", "12", "15", "18", "24", "36", "48"
        }));

        ActionListener listener = event -> setSample();

        face.addActionListener(listener);
        size.addActionListener(listener);
        bold.addActionListener(listener);
        italic.addActionListener(listener);

        setSample();
        pack();
    }

    /**
     * This method sets the text sample to the selected font.
     */
    public void setSample() {
        String fontFace = face.getItemAt(face.getSelectedIndex());
        int fontSize = Integer.parseInt(size.getItemAt(size.getSelectedIndex()));
        JTextArea sample = (JTextArea) gridBagPane.get("sample");
        int fontStyle = (bold.isSelected() ? Font.BOLD : 0) + (italic.isSelected() ? Font.ITALIC : 0);
        sample.setFont(new Font(fontFace, fontStyle, fontSize));
        sample.repaint();
    }
}
