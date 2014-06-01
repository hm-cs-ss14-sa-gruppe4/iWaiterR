/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.TableBean;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
public class NewOrderWindowController extends IWaiterController implements Initializable {

    @FXML
    private AnchorPane content;
    
    @FXML
    private TableView tblTable;
    @FXML
    private TableColumn colTableNumber;
    
    private MainWindowController mainWindowController;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // order item list
        colTableNumber.setCellValueFactory(new PropertyValueFactory<TableBean, Integer>(TableBean.PROP_TABLE_NUMBER));
        
    }
    
    /**
     * Initializes data for the view. 
     * @param tables
     */
    public void initData(ObservableList<TableBean> tables) {
        tblTable.setItems(tables);
    }
    
    /**
     * Event for choosing an order item to add to the current order.
     * @param t 
     * @throws java.io.IOException 
     */
    public void tblItem_click(Event t) throws IOException {
        if (tblTable.getSelectionModel().getSelectedItem() == null)
            return;
        
        MainWindowController controller = (MainWindowController) this.getCorrespondent();
        controller.addOrder((TableBean) tblTable.getSelectionModel().getSelectedItem());
        ((Stage) tblTable.getScene().getWindow()).close();
    }
    
}
