/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.ItemBean;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class NewItemWindowController extends IWaiterController implements Initializable {

    @FXML
    private AnchorPane content;
    
    @FXML
    private TableView tblItem;
    @FXML
    private TableColumn colItemName;
    @FXML
    private TableColumn colItemPrice;
    
    private MainWindowController mainWindowController;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // order item list
        colItemName.setCellValueFactory(new PropertyValueFactory<ItemBean, String>(ItemBean.PROP_NAME));
        colItemPrice.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemBean,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemBean, String> p) {
                DecimalFormat df = new DecimalFormat("###,##0.00 €");
                // is it possible to call the property directly without wrapping it? i'd like to keep the binding OrderBean.PROP_SUM_OF_MONEY
                return new ReadOnlyObjectWrapper(df.format((double)p.getValue().getPrice()/100));
            }
        });
        
    }
    
    /**
     * Initializes data for the view.
     * @param items 
     */
    public void initData(ObservableList<ItemBean> items) {
        tblItem.setItems(items);
    }
    
    /**
     * Event for choosing an order item to add to the current order.
     * @param t 
     * @throws java.io.IOException 
     */
    public void tblItem_click(Event t) throws IOException {
        if (tblItem.getSelectionModel().getSelectedItem() == null)
            return;
        
        MainWindowController controller = (MainWindowController) this.getCorrespondent();
        controller.addOrderItem((ItemBean) tblItem.getSelectionModel().getSelectedItem());
        ((Stage) tblItem.getScene().getWindow()).close();
    }
    
    
}
