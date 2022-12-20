package Utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * A class for conveniently navigating between views.
 */
public class SceneLoader {

    /**
     * Loads an fxml view
     * @param actionEvent - loads the fxml file
     * @param fxmlFilePath - the path to the fxml file to be loaded
     * @param width - the width of the view
     * @param height - the height of the view
     * @param title - the title bar string for the view
     * @throws IOException - throws exception if fxml file cannot be found
     */
    private static void goToView(ActionEvent actionEvent, String fxmlFilePath, int width, int height, String title) throws IOException {
        Parent root = FXMLLoader.load(SceneLoader.class.getResource(fxmlFilePath));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, width, height);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the view for the login page
     * @param primaryStage - set to the login page
     * @throws Exception - throws exception if /View/login.fxml cannot be found
     */
    public static void goToLoginView(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(SceneLoader.class.getResource("/View/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();
    }

    /**
     * This function is called many times throughout the application whenever the user wants to navigate back to the
     * main application view.
     *
     * In the main view on the left column is the table of all Customers from the database and below
     * that table is a set of buttons for manipulating Customers from that table. In the right column is the table of all
     * Appointments from the database and below that table is a set of buttons for manipulating Appointments from that
     * table.
     *
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToMainView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/main.fxml", 1400, 400, "Customer Appointment Manager");
    }

    /**
     * This function is called after the user clicks the "Add" button underneath the Customers table and open a view
     * where the user can add all necessary information to create their new Customer to add to the database.
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToAddCustomerView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/add-customer.fxml", 400, 600, "Add Customer");
    }

    /**
     * Called after the user clicks the "Modify" button underneath the Customers table. If a Customer is selected it will
     * open a new view where the user can edit the selected Customer. If no Customer is selected the app will display a
     * warning dialog indicating that the user needs to select a Customer to modify.
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToModifyCustomerView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/modify-customer.fxml", 400, 600, "Modify Customer");
    }

    /**
     * Called after the user clicks the "Add" button underneath the Appointments table. It opens a new view where the
     * user can enter all the information needed to create a new Appointment.
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToAddAppointmentView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/add-appointment.fxml", 400, 700, "Add Appointment");
    }

    /**
     * Called when the user clicks the "Modify" button under the Appointments table.
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToModifyAppointmentView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/modify-appointment.fxml", 400, 700, "Modify Appointment");
    }

    /**
     * When the customer reports button is clicked a new view will appear that allows the user to select a customer and
     * see in a text format a report that shows all the appointments in the database for that customer.
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToCustomerReportsView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/customer-report.fxml", 600, 400, "Customer Reports");
    }

    /**
     * Called when the user clicks the "Reports" button underneath the appointments table
     * @param actionEvent - passed to FXML Loader
     * @throws IOException - throws if fxml file not found
     */
    public static void goToAppointmentReportsView(ActionEvent actionEvent) throws IOException {
        goToView(actionEvent, "/View/appt-reports.fxml", 600, 400, "Appointment Reports");
    }

}
