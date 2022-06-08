import DBAccess.DBCountries;
import Database.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryState) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login-form.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        System.out.println("Hello world!");
        launch(args);
    }
}