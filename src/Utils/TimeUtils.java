package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeUtils {

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

    public static int militaryToCivilianHour(int hour) {
        if(hour > 0 && hour <= 12) {
            return hour;
        } else {
            return hour - 12;
        }
    }

    public static String getAMPMFromHour(int hour) {
        if(hour > 0 && hour <= 12) {
            return "AM";
        } else {
            return "PM";
        }
    }

//    public LocalDate getFirstDayOfWeek(int yearNumber, int monthNumber, int weekNumber) {
//        // create date string for first day of month
//        String dateString = Integer.toString(yearNumber)+"-"+Integer.toString(monthNumber)+"-01";
//        LocalDate firstDayOfMonth = LocalDate.parse(dateString);
//    }
}
