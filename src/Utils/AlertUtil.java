package Utils;

import javafx.scene.control.Alert;

public class AlertUtil {

    private static String officeHoursTitle = "Error: office hours";
    private static String officeHoursHeaderText = "Error: office hours are 8am-10pm EST";
    private static String officeHoursContentText = "Error: offices hours are 8am-10pm ESTError: offices 8am-10pm EST";

    private static String appointmentOverlapTitle = "Error: Appointment Overlap";
    private static String appointmentOverlapHeader = "Error: This appointment overlaps with another";
    private static String appointmentOverlapContent = "Error: This appointment overlaps with another";

    private static String customerSelectedTitle = "No customer selected";
    private static String customerSelectedHeader = "No customer selected";
    private static String customerSelectedContentModify = "No customer selected. Please select a customer to modify.";
    private static String customerSelectedContentDelete = "No customer selected. Please select a customer to delete.";

    private static String noAppointmentSelectedTitle = "No appointment selected";
    private static String noAppointmentSelectedHeader = "No appointment selected";
    private static String noAppointmentSelectedContentModify = "No appointment selected. Please select an appointment to modify.";
    private static String noAppointmentSelectedContentDelete = "No appointment selected. Please select an appointment to delete.";


    public static void warning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void officeHoursWarning() {
        warning(officeHoursTitle, officeHoursHeaderText, officeHoursContentText);
    }

    public static void appointmentOverlapWarning() {
        warning(appointmentOverlapTitle, appointmentOverlapHeader, appointmentOverlapContent);
    }

    public static void customerSelectWarningModify() {
        warning(customerSelectedTitle, customerSelectedHeader, customerSelectedContentModify);
    }

    public static void customerSelectWarningDelete() {
        warning(customerSelectedTitle, customerSelectedHeader, customerSelectedContentDelete);
    }

    public static void appointmentSelectWarningModify() {
        warning(noAppointmentSelectedTitle, noAppointmentSelectedHeader, noAppointmentSelectedContentModify);
    }

    public static void appointmentSelectWarningDelete() {
        warning(noAppointmentSelectedTitle, noAppointmentSelectedHeader, noAppointmentSelectedContentDelete);
    }
}
