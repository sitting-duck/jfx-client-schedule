package Controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import Model.Appointment;
import Model.Customer;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    /**
     * Users enters a string or id number into this text input field and when they press enter the customers table will refresh
     * displaying the set of customers containing either a matching id number or a substring of the search string entered.
     */
    @FXML
    private TextField customersSearchField;

    /**
     * Holds the set of all customers when there is no search query in the customers search text field, and shows the customers that
     * match the search query when there is one. Users can select a customer from this table to Modify or Delete. They can also
     * use the add button to add a customer to this table.
     */
    @FXML
    private TableView customerTable;

    /**
     * If the user has a customer selected in the customers table then on hitting this Delete button they will see a confirmation
     * asking them if they really want to delete the customer. They can click "Ok" to delete the customer or "Cancel" to exit early
     * without deleting. If no customer is selected the user will see a Warning that says no customer was selected for deletion.
     */
    @FXML
    private Button deleteCustomerBtn;

    /**
     * Users enters a string or id number into this text input field and when they press enter the appointments table will refresh
     * displaying the set of appointments containing either a matching id number or a substring of the search string entered.
     */
    @FXML
    private TextField appointmentsSearchField;

    /**
     * Holds the set of all appointments when there is no search query in the appointments search text field, and shows the appointments that
     * match the search query when there is one. Users can select a appointment from this table to Modify or Delete. They can also
     * use the add button to add a appointment to this table.
     */
    @FXML
    private TableView appointmentTable;

    /**
     * On press the user will see a confirmation asking if they are sure they wish to delete the selected appointment in the
     * appointment table. If there is no appointment selected then the user will see a warning saying that there is no appointment selected.
     */
    @FXML
    private Button deleteAppointmentBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initCustomerTable();
        } catch (IOException e) {
            System.out.println("Error initializing Customer table");
            throw new RuntimeException(e);
        }

        try {
            initAppointmentTable();
        } catch (IOException e) {
            System.out.println("Error initializing Appointment table");
            throw new RuntimeException(e);
        }

        // when the user clicks a Customer in the customer table, the search field for appointments at the top of the
        // on the right side column will auto-populate with the name of the customer that has been clicked. The
        // search field has a listener on it defined below such that when a customer name is entered, all appointments
        // with a matching customer name will show in the table.
        appointmentsSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchString = appointmentsSearchField.getText();
            System.out.println("Appointment search field text changed: " + searchString);
            try {
                this.searchAppointment(searchString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void onSearchCustomer(ActionEvent actionEvent) throws IOException {
        String queryText = this.customersSearchField.getText();
        System.out.println("getCustomerSearchResultsHandler: " + queryText);

        if(queryText.isEmpty()) {
            this.customerTable.setItems(DBCustomer.getAllCustomers());
            return;
        }

        ObservableList<Customer> customers;
        try {
            int idNum = Integer.parseInt(queryText);
            customers = DBCustomer.lookupCustomer(idNum);
        } catch(NumberFormatException exception) {
            System.out.println("Non Fatal Error: " + queryText + " cannot be converted to Integer.");
            customers = DBCustomer.lookupCustomer(queryText);
        }

        if(customers.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no results for " + queryText);
            alert.setHeaderText("Error: no results for " + queryText);
            alert.setContentText("Error: no results for " + queryText);
            alert.showAndWait();
            return;
        }
        this.customerTable.setItems(customers);
        this.customersSearchField.setText("");
    }

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/add-customer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {
        Customer customer = (Customer)customerTable.getSelectionModel().getSelectedItem();

        if(customer == null) {
            System.out.println("Error: no customer selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customer selected");
            alert.setHeaderText("No customer selected");
            alert.setContentText("No customer selected. Please select a customer to modify.");
            alert.showAndWait();
        } else {
            ModifyCustomerController.setCustomer(customer);
            Parent root = FXMLLoader.load(getClass().getResource("/View/modify-customer.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 400, 600);
            stage.setTitle("Modify Customer");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("onDeleteCustomer()");
        Customer customer = (Customer)customerTable.getSelectionModel().getSelectedItem();

        if(customer == null) {
            System.out.println("Error: no customer selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No customer selected");
            alert.setHeaderText("No customer selected");
            alert.setContentText("No customer selected. Please select a customer to delete.");
            alert.showAndWait();
        } else {
            DBCustomer.deleteCustomer(customer.getId());
            customerTable.setItems(DBCustomer.getAllCustomers());
            appointmentsSearchField.setText("");
        }
    }

    public void onSearchAppointment(ActionEvent actionEvent) throws Exception {
        String queryText = this.appointmentsSearchField.getText();
        System.out.println("getAppointmentSearchResultsHandler: " + queryText);
        this.searchAppointment(queryText);
    }
    public void searchAppointment(String queryText) throws Exception {
        System.out.println("searchAppointment(): " + queryText);

        if(queryText.isEmpty()) {
            this.appointmentTable.setItems(DBAppointment.getAllAppointments());
            System.out.println("Search appointment query text was empty, exiting.");
            return;
        }

        ObservableList<Appointment> appointments;
        try {
            int idNum = Integer.parseInt(queryText);
            appointments = DBAppointment.lookupAppointmentByTitle(idNum);
        } catch(NumberFormatException exception) {
            System.out.println("Non Fatal Error: " + queryText + " cannot be converted to Integer.");

            // Search for appointment by customer name
            int customerId = DBCustomer.getCustomerByName(queryText).getId();
            appointments = DBAppointment.lookupAppointmentsForCustomer(customerId);

            System.out.println("Got " + appointments.size() + " matching appointments for customer with customer name: " + queryText);
        }

        this.appointmentTable.setItems(appointments);
        //this.appointmentsSearchField.setText("");

        if(appointments.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no appointments for " + queryText);
            alert.setHeaderText("Error: no appointments for " + queryText);
            alert.setContentText("Error: no appointments for " + queryText);
            alert.showAndWait();
            //return;
        }
    }

    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/add-appointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 400, 700);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {
        Appointment appointment = (Appointment)appointmentTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            System.out.println("Error: no appointment selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No appointment selected");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("No appointment selected. Please select a appointment to modify.");
            alert.showAndWait();
        } else {
            ModifyAppointmentController.setAppointment(appointment);
            Parent root = FXMLLoader.load(getClass().getResource("/View/modify-appointment.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 400, 700);
            stage.setTitle("Modify Appointment");
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onDeleteAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        System.out.println("onDeleteAppointment()");
        Appointment appointment = (Appointment)appointmentTable.getSelectionModel().getSelectedItem();

        if(appointment == null) {
            System.out.println("Error: no appointment selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No appointment selected");
            alert.setHeaderText("No appointment selected");
            alert.setContentText("No appointment selected. Please select a appointment to delete.");
            alert.showAndWait();
        } else {
            DBAppointment.deleteAppointment(appointment.getId());
            appointmentTable.setItems(DBAppointment.getAllAppointments());
        }
    }

    /**
     * Initializes the Customer table with all Customers from database
     * @throws IOException
     */
    public void initCustomerTable() throws IOException {
        System.out.println("initCustomerTable()");

        this.customerTable.setEditable(true);

        TableColumn idDCol = new TableColumn("ID");
        idDCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));

        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));

        TableColumn addressCol = new TableColumn("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));

        TableColumn postalCol = new TableColumn("Postal");
        postalCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postal"));

        TableColumn phoneCol = new TableColumn("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));

        TableColumn divisionIdCol = new TableColumn("Division ID");
        divisionIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));

        customerTable.getColumns().addAll(idDCol, nameCol, addressCol, postalCol, phoneCol, divisionIdCol);

        customerTable.setItems(DBCustomer.getAllCustomers());

        this.customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
           if(newSelection != null) {
               System.out.println("new select: " + newSelection.toString());
               System.out.print("class: " + newSelection.getClass());
           }
           Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
           if(customer != null) {
               appointmentsSearchField.setText(customer.getName());
           }

        });
    }

    /**
     * Initializes the Appointment table with all Appointments from database
     * @throws IOException
     */
    public void initAppointmentTable() throws IOException {
        System.out.println("initAppointmentTable()");
        this.appointmentTable.setEditable(true);

        TableColumn idCol = new TableColumn("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));

        TableColumn titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));

        TableColumn descriptionCol = new TableColumn("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));

        TableColumn locationCol = new TableColumn("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));

        TableColumn typeCol = new TableColumn("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));

        TableColumn startCol = new TableColumn("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("start"));

        TableColumn endCol = new TableColumn("End");
        endCol.setCellValueFactory(new PropertyValueFactory<Appointment, Date>("end"));

        TableColumn customerIdCol = new TableColumn("Customer ID");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));

        TableColumn userIdCol = new TableColumn("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));

        TableColumn contactIdCol = new TableColumn("Contact ID");
        contactIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("contactId"));

        appointmentTable.getColumns().addAll(idCol, titleCol, descriptionCol, locationCol, typeCol, startCol, endCol, customerIdCol, userIdCol, contactIdCol);

        appointmentTable.setItems(DBAppointment.getAllAppointments());
    }

}
