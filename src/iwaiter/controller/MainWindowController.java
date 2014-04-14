/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class MainWindowController implements Initializable {
    @FXML
    private ListView lstWaiter;
    @FXML
    private TableView tblOrder;
    @FXML
    private Button cmdNewOrder;
    @FXML
    private Button cmdDelOrder;
    @FXML
    private Button cmdPrintOrder;
    @FXML
    private ComboBox lstTable;
    @FXML
    private TableView tblOrderItem;
    @FXML
    private Button cmdNewOrderItem;
    @FXML
    private Button cmdDelOrderItem;
    @FXML
    private TextField txtOrderItemName;
    @FXML
    private TextField txtOrderItemPrice;
    @FXML
    private Button cmdFinalizeOrder;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        txtOrderItemName.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            }
            
        });
        
    }
    
    @FXML
    private void clicked(ActionEvent event) {
        System.out.println(event.toString());
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
    }
    
}
