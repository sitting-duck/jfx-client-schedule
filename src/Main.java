import DBAccess.DBCountries;
import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/login.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 200, 200));
        primaryStage.show();
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        //DBConnection.openConnection();
        launch(args);
        //DBConnection.closeConnection();
    }
}