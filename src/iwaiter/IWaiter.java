package iwaiter;

import iwaiter.controller.MainWindowController;
import iwaiter.model.ItemBean;
import iwaiter.model.WaiterBean;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class IWaiter extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // beans & load example data
        ArrayList<WaiterBean> waiters = new ArrayList<>();
        ArrayList<ItemBean> availableItems = new ArrayList<>();
        for (String s : "Thomas|Johnson|Peter|Alfred".split("|"))
            waiters.add(new WaiterBean(s));
        
        availableItems.add(new ItemBean("Pina Colada",500));
        availableItems.add(new ItemBean("Bloody Mary",550));
        availableItems.add(new ItemBean("Swimming Pool",600));
        availableItems.add(new ItemBean("Zombie",650));
        
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
