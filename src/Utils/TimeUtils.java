package Utils;

import DBAccess.DBAppointment;
import Model.Appointment;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeUtils {

    public static Timestamp buildTimeStamp(Timestamp day, int hour, int minute, String amPm) {
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

        LocalDateTime ldt = day.toLocalDateTime();
        ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));

        Timestamp utcTimestamp = Timestamp.valueOf(utcZoned.toLocalDateTime());
        return utcTimestamp;
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

        boolean after8am_ET = start_zdt_ET.isAfter(at8AMEasternTime) || start_zdt_ET.isEqual(at8AMEasternTime);
        boolean before10pm_ET = end_zdt_ET.isBefore(before10PMEasternTime) || end_zdt_ET.isEqual(before10PMEasternTime);

        return after8am_ET && before10pm_ET;
    }

    public static String getNowLocalTimeString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        return "Local: " + java.time.ZonedDateTime.now().format(formatter);
    }

    public static String getNowEasternTimeString() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        ZoneId TIMEZONE_ET = ZoneId.of("America/New_York");
        ZonedDateTime now_ET = Instant.now().atZone(TIMEZONE_ET);
        return "Eastern: " + now_ET.format(formatter);
    }

    public static boolean isWithin15Minute() {
        ZoneId localTimeZone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(localTimeZone);
        ZonedDateTime nowPlus15Min = now.plusMinutes(15);

        for( Appointment appointment : DBAppointment.getAllAppointments()) {
            ZonedDateTime apptTime = appointment.getStart().toLocalDateTime().atZone(localTimeZone);
            if(apptTime.isAfter(now) && apptTime.isBefore(nowPlus15Min) || apptTime.isEqual(nowPlus15Min)) {
                return true;
            }
        }
        return false;
    }


}
