
import DBAccess.DBCountry;
import Model.Country;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TableColumn idCol;
    public TableColumn nameCol;
    public TableView dataTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showMe(ActionEvent actionEvent) {
        ObservableList<Country> countryList = DBCountry.getAllCountries();
        for(Country C : countryList) {
            System.out.println("Country Id: " + C.getId() + " Name: " + C.getName());
        }
    }
}
