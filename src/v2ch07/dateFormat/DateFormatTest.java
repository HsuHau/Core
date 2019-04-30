package v2ch07.dateFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * This program demonstrates formatting dates under various locales.
 */
public class DateFormatTest {
    public static void main(String[] args) {
        EventQueue.invokeLater(() ->{
            JFrame jFrame = new DateTimeFormatterFrame();
            jFrame.setTitle("DateFormatTest");
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setVisible(true);
        });
    }
}

/**
 * This frame contains combo boxes to pick a locale, date, and time formats, text fields to display
 * formatted date and time, buttons to parse the text field contents, and a "lenient" check box.
 */
class DateTimeFormatterFrame extends  JFrame {
    private Locale[] locales;
    private LocalDate currentDate;
    private LocalTime currentTime;
    private ZonedDateTime currentDateTime;
    private DateTimeFormatter currentDateFormat;
    private DateTimeFormatter currentTimeFormat;
    private DateTimeFormatter currentDateTimeFormat;
    private JComboBox<String> localeCombo = new JComboBox<>();
    private JButton dateParseButton = new JButton("Parse");
    private JButton timeParseButton = new JButton("Parse");
    private JButton dateTimeParseButton = new JButton("Parse");
    private JTextField dateText = new JTextField(30);
    private JTextField timeText = new JTextField(30);
    private JTextField dateTimeText = new JTextField(30);
    private EnumCombo<FormatStyle> dateStyleCombo = new EnumCombo<>(FormatStyle.class, "Short", "Medium", "Long", "Full");
    private EnumCombo<FormatStyle> timeStyleCombo = new EnumCombo<>(FormatStyle.class, "Short", "Medium");
    private EnumCombo<FormatStyle> dateTimeStyleCombo = new EnumCombo<>(FormatStyle.class, "Short", "Medium", "Long", "Full");

    public DateTimeFormatterFrame() {
        setLayout(new GridBagLayout());
        add(new JLabel("Locale"), new GBC(0, 0).setAnchor(GBC.EAST));
        add(localeCombo, new GBC(1, 0, 2, 1).setAnchor(GBC.WEST));

        add(new JLabel("Date"), new GBC(0, 1).setAnchor(GBC.EAST));
        add(dateStyleCombo, new GBC(1, 1).setAnchor(GBC.WEST));
        add(dateText, new GBC(2, 1, 2, 1).setFill(GBC.HORIZONTAL));
        add(dateParseButton, new GBC(4, 1).setAnchor(GBC.WEST));

        add(new JLabel("Time"), new GBC(0, 2).setAnchor(GBC.EAST));
        add(timeStyleCombo, new GBC(1, 2).setAnchor(GBC.WEST));
        add(timeText, new GBC(2, 2, 2, 1).setFill(GBC.HORIZONTAL));
        add(timeParseButton, new GBC(4, 2).setAnchor(GBC.WEST));

        add(new JLabel("Date and Time"), new GBC(0, 3).setAnchor(GBC.EAST));
        add(dateTimeStyleCombo, new GBC(1, 3).setAnchor(GBC.WEST));
        add(dateTimeText, new GBC(2, 3, 2, 1).setFill(GBC.HORIZONTAL));
        add(dateTimeParseButton, new GBC(4, 3).setAnchor(GBC.WEST));

        locales = (Locale[]) Locale.getAvailableLocales().clone();
        Arrays.sort(locales, Comparator.comparing(Locale::getDisplayName));
        for (Locale locale : locales) {
            localeCombo.addItem(locale.getDisplayName());
        }
        localeCombo.setSelectedItem(Locale.getDefault().getDisplayName());
        currentDate = LocalDate.now();
        currentTime = LocalTime.now();
        currentDateTime = ZonedDateTime.now();
        updateDisplay();

        ActionListener actionListener = event -> updateDisplay();

        localeCombo.addActionListener(actionListener);
        dateStyleCombo.addActionListener(actionListener);
        timeStyleCombo.addActionListener(actionListener);
        dateTimeStyleCombo.addActionListener(actionListener);

        dateParseButton.addActionListener(event -> {
            String d = dateText.getText().trim();
            try {
                currentDate = LocalDate.parse(d, currentDateFormat);
                updateDisplay();
            } catch (Exception e) {
                dateText.setText(e.getMessage());
            }
        });

        timeParseButton.addActionListener(event -> {
            String t = timeText.getText().trim();
            try {
                currentTime = LocalTime.parse(t, currentTimeFormat);
                updateDisplay();
            } catch (Exception e) {
                timeText.setText(e.getMessage());
            }
        });

        dateTimeParseButton.addActionListener(event -> {
            String dt = dateTimeText.getText().trim();
            try {
                currentDateTime = ZonedDateTime.parse(dt, currentDateTimeFormat);
                updateDisplay();
            } catch (Exception e) {
                dateTimeText.setText(e.getMessage());
            }
        });

        pack();
    }

    /**
     * Updates the display and formats the date according to the user settings.
     */
    public void updateDisplay() {
        Locale currentLocale = locales[localeCombo.getSelectedIndex()];

        FormatStyle dateStyle = dateStyleCombo.getValue();
        currentDateFormat = DateTimeFormatter.ofLocalizedDate(dateStyle).withLocale(currentLocale);
        dateText.setText(currentDateFormat.format(currentDate));

        FormatStyle timeStyle = timeStyleCombo.getValue();
        currentTimeFormat = DateTimeFormatter.ofLocalizedTime(timeStyle).withLocale(currentLocale);
        timeText.setText(currentTimeFormat.format(currentTime));

        FormatStyle dateTimeStyle = dateTimeStyleCombo.getValue();
        currentDateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle).withLocale(currentLocale);
        dateTimeText.setText(currentDateTimeFormat.format(currentDateTime));
    }
}

