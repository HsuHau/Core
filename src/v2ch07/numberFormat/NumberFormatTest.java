package v2ch07.numberFormat;

import v2ch07.dateFormat.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * This program demonstrates formatting numbers under various locales.
 */
public class NumberFormatTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new NumberFormatFrame();
            frame.setTitle("NumberFormatTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * This frame contains radio buttons to select a number format, a combo box to pick a locale,
 * a text field to display a formatted number, and a button to parse the text field contents.
 */
class NumberFormatFrame extends JFrame {
    private Locale[] locales;
    private double currentNumber;
    private JComboBox<String> localeCombo = new JComboBox<>();
    private JButton parseButton = new JButton("Parse");
    private JTextField numberText = new JTextField(30);
    private JRadioButton numberRadioButton = new JRadioButton("Number");
    private JRadioButton currencyRadioButton = new JRadioButton("Currency");
    private JRadioButton percentRadioButton = new JRadioButton("Percent");
    private ButtonGroup buttonGroup = new ButtonGroup();
    private NumberFormat currentNumberFormat;

    public NumberFormatFrame(){
        setLayout(new GridBagLayout());

        ActionListener actionListener = event -> updateDisplay();

        JPanel jPanel = new JPanel();
        addRadioButton(jPanel, numberRadioButton, buttonGroup, actionListener);
        addRadioButton(jPanel, currencyRadioButton, buttonGroup, actionListener);
        addRadioButton(jPanel, percentRadioButton, buttonGroup, actionListener);

        add(new JLabel("Locale:"), new GBC(0, 0).setAnchor(GBC.EAST));
        add(jPanel, new GBC(1, 1));
        add(parseButton, new GBC(0,2).setInsets(2));
        add(localeCombo, new GBC(1, 0).setAnchor(GBC.EAST));
        add(numberText, new GBC(1, 2).setFill(GBC.HORIZONTAL));
        locales = (Locale[]) NumberFormat.getAvailableLocales().clone();
        Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
        for (Locale locale : locales) {
            localeCombo.addItem(locale.getDisplayName());
        }
        localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
        currentNumber = 123456.78;
        updateDisplay();

        localeCombo.addActionListener(actionListener);

        parseButton.addActionListener(event -> {
            String s = numberText.getText().trim();
            try {
                Number number = currentNumberFormat.parse(s);
                if (number != null) {
                    currentNumber = number.doubleValue();
                    updateDisplay();
                } else {
                    numberText.setText("Parse error: " + s);
                }
            } catch (ParseException e) {
                numberText.setText("Parse error: " + s);
            }
        });
        pack();
    }

    /**
     * Adds a radio button to a container.
     * @param p the container into which to place the button
     * @param b the button
     * @param g the button group
     * @param listener the button listener
     */
    public void addRadioButton(Container p, JRadioButton b, ButtonGroup g, ActionListener listener) {
        b.setSelected(g.getButtonCount() == 0);
        b.addActionListener(listener);
        g.add(b);
        p.add(b);
    }

    /**
     * Updates the display and formats the number according to the user settings.
     */
    public void updateDisplay() {
        Locale currentLocale = locales[localeCombo.getSelectedIndex()];
        currentNumberFormat = null;
        if (numberRadioButton.isSelected()) {
            currentNumberFormat = NumberFormat.getNumberInstance(currentLocale);
        } else if (currencyRadioButton.isSelected()) {
            currentNumberFormat = NumberFormat.getCurrencyInstance(currentLocale);
        } else if (percentRadioButton.isSelected()) {
            currentNumberFormat = NumberFormat.getPercentInstance(currentLocale);
        }
        String formatted = currentNumberFormat.format(currentNumber);
        numberText.setText(formatted);
    }
}