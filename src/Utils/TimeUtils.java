package Utils;

import DBAccess.DBAppointment;
import Model.Appointment;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeUtils {

    /**
     *
     * @param time: A Timestamp containing a date and time.
     * @param month: String for what month to test the timestamp is in. For example: "January", "February" etc. This
     *             value is case-insensitive so "january" and "JANUARY" are all valid String values.
     * @return boolean: returns a boolean indicating if the Timestamp: time passed in was within the month passed in as
     * a String.
     * For example: If we had a timestamp that evaluated to this String: '2022-11-11 11:15:00' That Timestamp is in the
     * 11th month, which is November. String values for month "November", "NOVEMBER", "november" etc. would all return
     * true.
     */
    public static boolean inMonth(Timestamp time, String month) {
        ZonedDateTime zdt = time.toLocalDateTime().atZone(ZoneId.systemDefault());
        String monthFromTime = zdt.getMonth().toString();
        return monthFromTime.equals(month.toUpperCase());
    }

    /**
     * Returns a timestamp from several components that are added together to make the final Timestamp.
     * @param day - A timestamp that is exactly midnight of a date selected by the user from a calendar.
     * @param hour - int specifying the number of hours from midnight. 8 is 8am and 13 is 1pm and so on.
     * @param minute - int specifying the number of minutes from the beginning of the hour.
     * @param amPm - String indicating AM or PM. Case Insensitive.
     * @return
     */
    public static Timestamp buildTimeStamp(Timestamp day, int hour, int minute, String amPm) {
        LocalDateTime time = day.toLocalDateTime();
        if(amPm.toUpperCase().equals("AM")) {
            if(hour != 12) {
                time = time.plusHours(hour);
            }
        } else if(amPm.toUpperCase().equals("PM")) {
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
        //ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
        //ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));

        //Timestamp utcTimestamp = Timestamp.valueOf(ldtZoned.toLocalDateTime());
        Timestamp ldt_ts = Timestamp.valueOf(ldt);
        return ldt_ts;
    }

    /**
     * Takes hours from midnight in military time and return int indicating the hour in civilian time.
     * @param hour - int specifying how many hours from midnight in military time.
     * @return - int specifying the hour in civilian time. Note. Does not specify AM or PM, just returns the int.
     * For example, an input of 13 will return an output of 1. An input of 1 will return an output of 1. An input of
     * 22 will return 10, for 10pm, and so on.
     */
    public static int militaryToCivilianHour(int hour) {
        if(hour > 0 && hour <= 12) {
            return hour;
        } else {
            return hour - 12;
        }
    }

    /**
     * Take an integer value for the hours from midnight in military time, and returns the string "AM" or PM.
     * 13 will return the value "PM". The value 1 will return the value "AM" and so on.
     * @param hour - an integer value for the hours from midnight in military time.
     * @return - the String "AM" or "PM" depending on if the hour passed in is more than 12 hours from midnight.
     */
    public static String getAMPMFromHour(int hour) {
        if(hour > 0 && hour <= 12) {
            return "AM";
        } else {
            return "PM";
        }
    }

    /**
     * Takes two Timestamps for the start and end time of an Appointment, and returns a boolean indicating if that
     * Timestamp is withing the office hours of 8am to 10pm in the Eastern Time zone.
     * @param start - A Timestamp for the start time of an appointment in UTC
     * @param end - A Timestamp for the end time of an appointment in UTC
     * @return - a boolean indicating if the Timestamp is within the office hours of 8am to 10pm in the Eastern time zone.
     */
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

    /**
     *
     * @return
     */
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

    public static String isWithin15Minute() {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime now = Instant.now().atZone(zoneId);
        ZonedDateTime nowPlus15Min = now.plusMinutes(15);

        for( Appointment appointment : DBAppointment.getAllAppointments()) {
            ZonedDateTime apptTime = appointment.getStart().toLocalDateTime().atZone(zoneId);
            boolean afterNow = apptTime.isAfter(now);
            boolean before15MinFromNow = apptTime.isBefore(nowPlus15Min);
            boolean exactly15MinFromNow = apptTime.isEqual(nowPlus15Min);
            if(afterNow && (before15MinFromNow || exactly15MinFromNow)) {
                String dateString = appointment.getStart().toLocalDateTime().toLocalDate().atStartOfDay().toString();
                String timeString = appointment.getStart().toLocalDateTime().toLocalTime().toString();
                return "Upcoming Appointment Id: " + appointment.getId() + " title: " + appointment.getTitle() + " date: " + dateString + " time: " + timeString;
            }
        }
        return "There are no appointments within the next 15 minutes.";
    }

}
