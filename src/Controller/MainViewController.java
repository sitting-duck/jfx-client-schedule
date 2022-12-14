package Controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import Model.Appointment;
import Model.Customer;
import Utils.UXUtil;
import javafx.collections.FXCollections;
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
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
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
     * When the month radio button is selected, the appointments table will display all appointments within the current selected month.
     * When the month radio button is selected, a Date calendar widget will become visible allowing the user to pick a date,
     * and the month of that date will be the selected month.
     */
    @FXML
    private RadioButton monthRadioButton;

    /**
     * When the week radio button is selected, the appointments table will display all appointments within the current
     * selected week.
     * When the week radio button is selected, a Date calendar widget will become visible allowing the user to pick a date,
     * and the week of that date will be the selected month.
     */
    @FXML
    private RadioButton weekRadioButton;

    /**
     * When the "All Time" radio button is selected, all appointments will show in the appointments table.
     */
    @FXML
    private RadioButton allTimeRadioButton;

    /**
     * The appointment date picker becomes visible when the "Month" or "Week" radio button above the Appointments table
     * are selected. When the "All Time" radio button is selected (also above the Appointments table) the date picker
     * widget disappears since there is no need to select a date range if all appointments are going to be shown anyway.
     *
     * When it is visible, it allows the user to select a date. The user will select a single day. If the Month radio
     * button is selected, then the Appointments table will refresh with all the appointments within the month of the date
     * selected in this calendar widget. Likewise, the same applies for the "Week" radio button. The appointments table
     * will refresh on selection of a date to show all the appointments within the same week of the selected date in the
     * calendar widget.
     */
    @FXML
    private DatePicker appointmentDatePicker;

    /**
     * Leave this here for now until you see if the instructor will allow you to use the calendar widget for month selection.
     */
    @FXML
    private ComboBox monthComboBox;

    /**
     * Leave this here for now until you see if the instructor will allow you to use the calendar widget for month selection.
     */
    @FXML
    private ComboBox weekComboBox;

    /**
     *
     * @param url
     * @param resourceBundle
     *
     * Description: this function is called every time the MainView itself is loaded. The main view is defined in the
     * main.fxml file. In this function we initialize the Customers table and the appointments table by getting all
     * Customers and all Appointments from the Database. The default statue of the table is to show all items, and then
     * the user can enter a search string into the text field above each table to narrow the results if they desire.
     */
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
            System.out.println("UXUtil.isMonth(searchString): " + UXUtil.isMonth(searchString));

            if(UXUtil.isMonth(searchString)) {
                this.searchAppointmentByMonth(searchString);
            }
            try {
                this.searchAppointment(searchString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                //throw new RuntimeException(e);
                System.out.println("Exception: No appointments for customer name: " + searchString);
            }
        });

        //Set toggle group so that only one of All Time, Month, or Week radio buttons can be selected
        ToggleGroup group = new ToggleGroup();
        monthRadioButton.setToggleGroup(group);
        weekRadioButton.setToggleGroup(group);
        allTimeRadioButton.setToggleGroup(group);

        allTimeRadioButton.setSelected(true);
        appointmentDatePicker.setVisible(false);
        appointmentDatePicker.setValue(LocalDate.now());
    }

    /**
     * When the customer reports button is clicked a new view will appear that allows the user to select a customer and
     * see in a text format a report that shows all the appointments in the database for that customer.
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomerReportsButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/customer-report.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called when the user clicks the "Reports" button underneath the appointments table
     * @param actionEvent
     * @throws IOException
     */
    public void onAppointmentReportsButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/appt-reports.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointment Manager");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Never called, leave this here for now.
     * @param actionEvent
     * @throws IOException
     */
    public void onMonthSelected(ActionEvent actionEvent) throws IOException {
        String month = monthComboBox.getValue().toString();
        System.out.println("Month: " + month);

        if(month == "All") {
            appointmentsSearchField.setText("");

        } else {
            appointmentsSearchField.setText(month);
        }
    }

    /**
     * Called when a user selects a date from the Calendar widget.
     * @param actionEvent
     * @throws IOException
     */
    public void onDateSelected(ActionEvent actionEvent) throws IOException {

        LocalDate ld =  appointmentDatePicker.getValue();
        if(weekRadioButton.isSelected()) {
            int weekOfYear = ld.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            ZoneId defaultZoneId = ZoneId.systemDefault();
            java.util.Date date = Date.from(ld.atStartOfDay(defaultZoneId).toInstant());
            calendar.setTime(date);

            System.out.println("weekOfYear: " + Integer.toString(weekOfYear));
            searchAppointmentByWeek(ld);
        } else if(monthRadioButton.isSelected()) {
            searchAppointmentByMonth(ld.getMonth().toString());
        }
    }

    /**
     * Called when the user clicks the radio button labelled "All Time" above the Appointments table. The point of this
     * radio button is to allow the user to filter the Appointments table by time. This particular radio button is in a
     * set of 3 radio buttons, "All Time", "Month" and "Week". When "All Time" is selected the Appointments table will
     * refresh and display all the Appointments available in the database regardless of date. If "Month" is selected, the
     * table will only show Appointments within a chosen month, and likewise if the "Week" radio button is selected, a
     * chosen weeks worth of Appointments will be shown in the Appointments table.
     * @param actionEvent
     * @throws Exception
     */
    public void onAllTimeClicked(ActionEvent actionEvent) throws Exception {
        appointmentDatePicker.hide();
        appointmentDatePicker.setVisible(false);
        onSearchAppointment(actionEvent);
    }

    /**
     * Called when the user clicks the radio button labelled "Week" above the Appointments table. The point of this
     * radio button is to allow the user to filter the Appointments table by time. This particular radio button is in a
     * set of 3 radio buttons, "All Time", "Month" and "Week". When "All Time" is selected the Appointments table will
     * refresh and display all the Appointments available in the database regardless of date. If "Month" is selected, the
     * table will only show Appointments within a chosen month, and likewise if the "Week" radio button is selected, a
     * chosen weeks worth of Appointments will be shown in the Appointments table.
     * @param actionEvent
     * @throws Exception
     */
    public void onWeekClicked(ActionEvent actionEvent) throws IOException {
        appointmentDatePicker.setVisible(true);
        appointmentDatePicker.show();
        onDateSelected(actionEvent);
    }

    /**
     * Called when the user clicks the radio button labelled "Month" above the Appointments table. The point of this
     * radio button is to allow the user to filter the Appointments table by time. This particular radio button is in a
     * set of 3 radio buttons, "All Time", "Month" and "Week". When "All Time" is selected the Appointments table will
     * refresh and display all the Appointments available in the database regardless of date. If "Month" is selected, the
     * table will only show Appointments within a chosen month, and likewise if the "Week" radio button is selected, a
     * chosen weeks worth of Appointments will be shown in the Appointments table.
     * @param actionEvent
     * @throws Exception
     */
    public void onMonthClicked(ActionEvent actionEvent) throws IOException {
        appointmentDatePicker.setVisible(true);
        appointmentDatePicker.show();
        onDateSelected(actionEvent);
    }

    /**
     * This function is called when the user enters a search string into the text field above the Customers table.
     * Every time a character is entered into that text field the Customers table will refresh displaying the set of
     * Customers that either have a matching Customer Name or Customer id.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * This function is called after the user clicks the "Add" button underneath the Customers table and open a view
     * where the user can add all necessary information to create their new Customer to add to the database.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/add-customer.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called after the user clicks the "Modify" button underneath the Customers table. If a Customer is selected it will
     * open a new view where the user can edit the selected Customer. If no Customer is selected the app will display a
     * warning dialog indicating that the user needs to select a Customer to modify.
     * @param actionEvent
     * @throws IOException
     */
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

    /**
     * Called after the user clicks the Delete button underneath the Customers table. If there is no Customer selected
     * the app will display a warning. If a Customer is selected the app will delete all the Appointments for that Customer
     * in the database and then delete the Customer object itself in the MySQL database and then display a dialog box
     * informing the user as to what has been deleted.
     * @param actionEvent
     * @throws IOException
     * @throws SQLException
     */
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Customer deleted successfully");
            alert.setHeaderText("Customer deleted successfully");
            alert.setContentText("Customer deleted successfully");
            alert.showAndWait();
        }
    }

    /**
     * This function is called when the user enters a search string into the text field above the Appointments table.
     * Every time a character is entered the Appointments table will update with the set of Appointments with names
     * containing the search string or with a matching ID number.
     * @param actionEvent
     * @throws Exception
     */
    public void onSearchAppointment(ActionEvent actionEvent) throws Exception {
        String queryText = this.appointmentsSearchField.getText();
        System.out.println("getAppointmentSearchResultsHandler: " + queryText);
        this.searchAppointment(queryText);
    }
    public ObservableList<Appointment> searchAppointment(String queryText) throws Exception {
        System.out.println("searchAppointment(): " + queryText);

        if(queryText.isEmpty()) {
            this.appointmentTable.setItems(DBAppointment.getAllAppointments());
            System.out.println("Search appointment query text was empty, exiting.");
            return DBAppointment.getAllAppointments();
        }

        ObservableList<Appointment> appointments = null;
        try {
            int idNum = Integer.parseInt(queryText);
            appointments = DBAppointment.lookupAppointmentByTitle(idNum);
        } catch(NumberFormatException exception) {
            System.out.println("Non Fatal Error: " + queryText + " cannot be converted to Integer.");

            // Search for appointment by customer name
            if(DBCustomer.customerExists(queryText)) {
                int customerId = DBCustomer.getCustomerByName(queryText).getId();
                appointments = DBAppointment.lookupAppointmentsForCustomer(customerId);
            } else { // if no customer by this name we search by appointment title
                appointments = DBAppointment.lookupAppointmentByTitle(queryText);
            }
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
        }

        return appointments;
    }

    /**
     * Called when the user selects a date from the Calendar widget and the "Month" radio button is selected. Causes the
     * Appointments table to refresh and show the set of Appointments within the specified month.
     * @param queryText
     * @return
     */
    public ObservableList<Appointment> searchAppointmentByMonth(String queryText) {
        System.out.println("searchAppointmentByMonth(): " + queryText);
        queryText = queryText.toUpperCase();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        if(queryText.isEmpty()) {
            this.appointmentTable.setItems(DBAppointment.getAllAppointments());
            System.out.println("Search appointment query text was empty, exiting.");
            return appointments;
        }

        // Search for appointment by month
        for(Appointment appointment : DBAppointment.getAllAppointments()) {
            String startMonth = appointment.getStart().toLocalDateTime().getMonth().toString().toUpperCase();
            String endMonth = appointment.getEnd().toLocalDateTime().getMonth().toString().toUpperCase();
            System.out.println("name: " + appointment.getTitle() + " startMonth: " + startMonth + " endMonth: " + endMonth + " queryText: " + queryText);
            if(startMonth.equals(queryText)) {
                appointments.add(appointment);
            } else if(endMonth.equals(queryText)) {
                appointments.add(appointment);
            }
        }

        this.appointmentTable.setItems(appointments);
        if(appointments.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no appointments for " + queryText);
            alert.setHeaderText("Error: no appointments for " + queryText);
            alert.setContentText("Error: no appointments for " + queryText);
            alert.showAndWait();
            //return;
        }
        return appointments;
    }

    public ObservableList<Appointment> searchAppointmentByWeek(LocalDate ld) {
        System.out.println("searchAppointmentByWeek(): ");
        //Now, get the date for the beginning of the week
        LocalDate startWeek = ld;
        while(startWeek.getDayOfWeek() != DayOfWeek.SUNDAY) {
            startWeek = startWeek.minusDays(1);
        }
        LocalDateTime startWeekTime = startWeek.atStartOfDay();

        //Now, get the date for the end of the week
        LocalDate endWeek = ld;
        while(endWeek.getDayOfWeek() != DayOfWeek.SATURDAY) {
            endWeek = endWeek.plusDays(1);
        }
        LocalDateTime endWeekTime = endWeek.atTime(23, 59);

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Search for appointment by week
        for(Appointment appointment : DBAppointment.getAllAppointments()) {
            LocalDateTime startAppt = appointment.getStart().toLocalDateTime();
            LocalDateTime endAppt = appointment.getEnd().toLocalDateTime();

            if(startAppt.isAfter(startWeekTime) && endAppt.isBefore(endWeekTime)) {
                appointments.add(appointment);
            }
        }

        this.appointmentTable.setItems(appointments);
        if(appointments.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no appointments for " + ld.toString());
            alert.setHeaderText("Error: no appointments for " + ld.toString());
            alert.setContentText("Error: no appointments for " + ld.toString());
            alert.showAndWait();
            //return;
        }
        return appointments;
    }

    /**
     * Called after the user clicks the "Add" button underneath the Appointments table. It opens a new view where the
     * user can enter all the information needed to create a new Appointment.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/add-appointment.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 400, 700);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called when the user clicks the "Modify" button under the Appointments table.
     * @param actionEvent
     * @throws IOException
     */
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
    /**
     * On pressing the Delete button under the appointments table the user will see a confirmation asking if they are sure they wish to delete
     * the selected appointment in the appointment table. If there is no appointment selected then the user will see a warning saying that
     * there is no appointment selected.
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws IOException, SQLException {
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

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment deleted successfully");
            alert.setHeaderText("Appointment deleted successfully");
            alert.setContentText("Appointment with id: " + appointment.getId() + " and type: " + appointment.getType() + " deleted successfully");
            alert.showAndWait();
        }
    }

    /**
     * Initializes the Customer table with all Customers from database.
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

        //final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        TableColumn startCol = new TableColumn("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        startCol.setCellFactory(col -> new TableCell<Appointment, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    ZonedDateTime zdt = item.toLocalDateTime().atZone(ZoneId.systemDefault());
                    setText(String.format(zdt.format(formatter)));
                }
            }
        });

        TableColumn endCol = new TableColumn("End");
        endCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        endCol.setCellFactory(col -> new TableCell<Appointment, Timestamp>() {
            @Override
            protected void updateItem(Timestamp item, boolean empty) {
                super.updateItem(item, empty);
                if(empty) {
                    setText(null);
                } else {
                    ZonedDateTime zdt = item.toLocalDateTime().atZone(ZoneId.systemDefault());
                    setText(String.format(zdt.format(formatter)));
                }
            }
        });

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
