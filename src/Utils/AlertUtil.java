package Utils;

import javafx.scene.control.Alert;

/**
 * Class for showing Warning Dialogs to the user
 */
public class AlertUtil {

    /**
     * Title for office hours warning dialog
     */
    private static String officeHoursTitle = "Error: office hours";

    /**
     * Header for office hours warning dialog
     */
    private static String officeHoursHeaderText = "Error: office hours are 8am-10pm EST";

    /**
     * Content for office hours warning dialog
     */
    private static String officeHoursContentText = "Error: offices hours are 8am-10pm ESTError: offices 8am-10pm EST";

    /**
     * Title for appointment overlap warning dialog
     */
    private static String appointmentOverlapTitle = "Error: Appointment Overlap";

    /**
     * Header for appointment overlap warning dialog
     */
    private static String appointmentOverlapHeader = "Error: This appointment overlaps with another";

    /**
     * Content for appointment overlap warning dialog
     */
    private static String appointmentOverlapContent = "Error: This appointment overlaps with another";

    /**
     * Title for no customer selected warning dialog
     */
    private static String customerSelectedTitle = "No customer selected";

    /**
     * Header for no customer selected warning dialog
     */
    private static String customerSelectedHeader = "No customer selected";

    /**
     * Content for no customer selected warning dialog when clicking modify button
     */
    private static String customerSelectedContentModify = "No customer selected. Please select a customer to modify.";

    /**
     * Content for no customer selected warning dialog when clicking delete button
     */
    private static String customerSelectedContentDelete = "No customer selected. Please select a customer to delete.";

    /**
     * Title for no appointment selected warning dialog
     */
    private static String noAppointmentSelectedTitle = "No appointment selected";

    /**
     * Header for no appointment selected warning dialog
     */
    private static String noAppointmentSelectedHeader = "No appointment selected";

    /**
     * Content for no appointment selected warning dialog when clicking modify button
     */
    private static String noAppointmentSelectedContentModify = "No appointment selected. Please select an appointment to modify.";

    /**
     * Content for no appointment selected warning dialog when clicking delete button
     */
    private static String noAppointmentSelectedContentDelete = "No appointment selected. Please select an appointment to delete.";

    /**
     * Creates and displays a warning dialog with strings of your choice. Will remain visible until the user clicks OK
     * @param title - title bar string
     * @param header - header string
     * @param content - content string. This is usually a detailed error message.
     */
    public static void warning(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays when the user selects a date for Appointment creation or modification that is outside office hours
     */
    public static void officeHoursWarning() {
        warning(officeHoursTitle, officeHoursHeaderText, officeHoursContentText);
    }

    /**
     * Displays when the user attempts to create or modify an appointment to exist on a time that overlaps with
     * another already existing appointment in the database.
     */
    public static void appointmentOverlapWarning() {
        warning(appointmentOverlapTitle, appointmentOverlapHeader, appointmentOverlapContent);
    }

    /**
     * Displays when the user has clicked the modify button underneath the customer table, but has not selected
     * a Customer to modify from that table.
     */
    public static void customerSelectWarningModify() {
        warning(customerSelectedTitle, customerSelectedHeader, customerSelectedContentModify);
    }

    /**
     * Displays when the user has clicked the delete button underneath the Customer table, but has not selected a
     * Customer to delete from that table.
     */
    public static void customerSelectWarningDelete() {
        warning(customerSelectedTitle, customerSelectedHeader, customerSelectedContentDelete);
    }

    /**
     * Displays when the user has clicked the Modify button underneath the Appointments table, but has not clicked
     * an appointment to modify from that table.
     */
    public static void appointmentSelectWarningModify() {
        warning(noAppointmentSelectedTitle, noAppointmentSelectedHeader, noAppointmentSelectedContentModify);
    }

    /**
     * Displays when the user has clicked the Delete button underneath the Appointments table, but has not clicked
     * an appointment to delete from that table.
     */
    public static void appointmentSelectWarningDelete() {
        warning(noAppointmentSelectedTitle, noAppointmentSelectedHeader, noAppointmentSelectedContentDelete);
    }
}
