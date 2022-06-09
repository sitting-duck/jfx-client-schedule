package Controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class CustomerAppointmentController implements Initializable {

    /**
     * Users enters a string or id number into this text input field and when they press enter the customers table will refresh
     * displaying the set of customers containing either a matching id number or a substring of the search string entered.
     */
    public TextField customersSearchField;

    /**
     * Holds the set of all customers when there is no search query in the customers search text field, and shows the customers that
     * match the search query when there is one. Users can select a customer from this table to Modify or Delete. They can also
     * use the add button to add a customer to this table.
     */
    public TableView customerTable;

    /**
     * If the user has a customer selected in the customers table then on hitting this Delete button they will see a confirmation
     * asking them if they really want to delete the customer. They can click "Ok" to delete the customer or "Cancel" to exit early
     * without deleting. If no customer is selected the user will see a Warning that says no customer was selected for deletion.
     */
    public Button deleteCustomerBtn;

    /**
     * Users enters a string or id number into this text input field and when they press enter the appointments table will refresh
     * displaying the set of appointments containing either a matching id number or a substring of the search string entered.
     */
    public TextField appointmentsSearchField;

    /**
     * Holds the set of all appointments when there is no search query in the appointments search text field, and shows the appointments that
     * match the search query when there is one. Users can select a appointment from this table to Modify or Delete. They can also
     * use the add button to add a appointment to this table.
     */
    public TableView appointmentTable;

    /**
     * On press the user will see a confirmation asking if they are sure they wish to delete the selected appointment in the
     * appointment table. If there is no appointment selected then the user will see a warning saying that there is no appointment selected.
     */
    public Button deleteAppointmentBtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initAppointmentTable();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSearchCustomer(ActionEvent actionEvent) throws IOException {

    }

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {

    }

    public void onModifyCustomer(ActionEvent actionEvent) throws IOException {

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

        TableColumn customerIDCol = new TableColumn("ID");
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn customerNameCol = new TableColumn("Name");
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn inventoryLevelCol = new TableColumn("Inventory Level");
        inventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn priceCostPerUnitCol = new TableColumn("Price/Cost per Unit");
        priceCostPerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        customerTable.getColumns().addAll(customerIDCol, customerNameCol, inventoryLevelCol, priceCostPerUnitCol);

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

        //todo: date start and end
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
