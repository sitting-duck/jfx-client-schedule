package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtils {

    public static void thing(DatePicker dp, ComboBox hourCB, ComboBox minuteCB, ComboBox amPMCB) {

    }

    public static Timestamp getTime(Timestamp day, int hour, int minute, String amPm) {
        LocalDateTime time = day.toLocalDateTime();
        if(amPm.equals("AM")) {
            if(hour != 12) {
                time = time.plusHours(hour);
            }
        } else if(amPm.equals("PM")) {
            if(hour == 12) {
                time = time.plusHours(12);
            } else {
                time = time.plusHours(12 + hour);
            }
        } else {
            System.out.println("Error: Invalid value for start am/pm combo box");
        }

        time = time.plusMinutes(minute);
        day = Timestamp.valueOf(time);
        return day;
    }
}
