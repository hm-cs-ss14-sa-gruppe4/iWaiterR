package iwaiter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class IWaiter extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // view & controller
        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        
        stage.setTitle("iWaiter");
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
