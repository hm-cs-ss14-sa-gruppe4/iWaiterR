package iwaiter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
public class IWaiter extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainWindow.fxml"));
        Pane pane = (Pane) loader.load(); //init is executed here
        //IWaiterController controller = loader.getController();
        //controller.setParentStage(primaryStage);
        primaryStage.setTitle("iWaiter");
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();
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
