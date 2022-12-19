import Database.DBConnection;
import Utils.SceneLoader;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Program entry. Loads the login page view and initializes a connection with the database.
 */
public class Main extends Application {

    /**
     * Loads the view for the login page
     * @param primaryStage - set to the login page
     * @throws Exception - throws exception if /View/login.fxml cannot be found
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneLoader.goToLoginView(primaryStage);
    }

    /**
     * Initializes a connection to the database and starts the Java main loop
     * @param args - command line arguments to be passed to the Java main loop
     */
    public static void main(String[] args) {
        DBConnection.openConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}