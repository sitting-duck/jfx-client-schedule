package Utils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.sql.Timestamp;
import java.time.*;

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

    public static boolean withinOfficeHours(Timestamp start, Timestamp end) {
        ZoneId TIMEZONE_ET = ZoneId.of("America/New_York");
        Instant startAsInstant = start.toInstant();
        LocalDate startAsLocalDate = startAsInstant.atZone(TIMEZONE_ET).toLocalDate();
        Instant startMidnightAsInstantSameDay = startAsLocalDate.atStartOfDay(TIMEZONE_ET).toInstant();
        Instant start8amAsInstantSameDay = startMidnightAsInstantSameDay.plus(Duration.ofHours(8)); // 8 hours from midnight is 8am.

        Instant endAsInstant = end.toInstant();
        LocalDate endAsLocalDate = endAsInstant.atZone(TIMEZONE_ET).toLocalDate();
        Instant endMidnightAsInstantSameDay = endAsLocalDate.atStartOfDay(TIMEZONE_ET).toInstant();
        Instant end10pmAsInstantSameDay = endMidnightAsInstantSameDay.plus(Duration.ofHours(22)); // 12 + 10 hours from midnight is 10pm.


        ZonedDateTime start_zdt_ET = startAsInstant.atZone(TIMEZONE_ET);
        ZonedDateTime end_zdt_ET = endAsInstant.atZone(TIMEZONE_ET);
        ZonedDateTime at8AMEasternTime = start8amAsInstantSameDay.atZone(TIMEZONE_ET);
        ZonedDateTime before10PMEasternTime = end10pmAsInstantSameDay.atZone(TIMEZONE_ET);

        // now check if appointment is during office hours 8am to 10pm EST including weekends
        boolean after8am_ET = startAsInstant.isAfter(start8amAsInstantSameDay);
        boolean before10pm_ET = endAsInstant.isBefore(end10pmAsInstantSameDay);

        return after8am_ET && before10pm_ET;
    }

//    public LocalDate getFirstDayOfWeek(int yearNumber, int monthNumber, int weekNumber) {
//        // create date string for first day of month
//        String dateString = Integer.toString(yearNumber)+"-"+Integer.toString(monthNumber)+"-01";
//        LocalDate firstDayOfMonth = LocalDate.parse(dateString);
//    }
}
