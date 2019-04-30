package v2ch06.formatting;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

public class Formatting {
    public static void main(String[] args) {
        ZonedDateTime apollo11launch = ZonedDateTime.of(1969, 7, 16, 9, 32, 0, 0,
                ZoneId.of("America/New_York"));

        String formatted = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(apollo11launch);
        // 1969-07016T09:32:00-04:00
        System.out.println(formatted);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        formatted = dateTimeFormatter.format(apollo11launch);
        // July 16, 1969 9:32:00 AM EDT
        System.out.println(formatted);
        formatted = dateTimeFormatter.withLocale(Locale.FRENCH).format(apollo11launch);
        // 16 juillet 1969 09:32:00 EDT
        System.out.println(formatted);

        dateTimeFormatter = DateTimeFormatter.ofPattern("E yyyy-MM-dd HH:mm");
        formatted = dateTimeFormatter.format(apollo11launch);
        System.out.println(formatted);

        LocalDate churchBirthday = LocalDate.parse("1903-06-14");
        System.out.println("churchBirthday: " + churchBirthday);
        apollo11launch = ZonedDateTime.parse("1969-07-16 03:32:00-0400", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssxx"));
        System.out.println("apollo11launch:" + apollo11launch);

        for (DayOfWeek week : DayOfWeek.values()) {
            System.out.println(week.getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " ");
        }
    }
}
