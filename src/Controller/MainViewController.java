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

    public void onDeleteCustomer(ActionEvent actionEvent) throws IOException {

    }

    public void onSearchAppointment(ActionEvent actionEvent) throws IOException {

    }

    public void onAddAppointment(ActionEvent actionEvent) throws IOException {

    }

    public void onModifyAppointment(ActionEvent actionEvent) throws IOException {

    }

    public void onDeleteAppointment(ActionEvent actionEvent) throws IOException {

    }

    public void onExitBtnClicked(ActionEvent actionEvent) throws IOException {

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
