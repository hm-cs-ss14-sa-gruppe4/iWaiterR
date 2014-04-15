/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.ItemBean;
import iwaiter.model.OrderBean;
import iwaiter.model.WaiterBean;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class MainWindowController implements Initializable {
    
    // Model
    ArrayList<WaiterBean> waiters = new ArrayList<>();
    ArrayList<ItemBean> availableItems = new ArrayList<>();
    
    // View
    @FXML
    private ListView lstWaiter;
    @FXML
    private TableView tblOrder;
    @FXML
    private TableColumn colOrderNumber;
    @FXML
    private TableColumn colOrderTableNumber;
    @FXML
    private TableColumn colOrderSum;
    @FXML
    private TableColumn colOrderFinalized;
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
    private TableColumn colItemName;
    @FXML
    private TableColumn colItemPrice;
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
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       // waiter list
        lstWaiter.setCellFactory(new Callback<ListView, ListCell<WaiterBean>>() {
            
            @Override
            public ListCell<WaiterBean> call(ListView p) {
                return new ListCell<WaiterBean>() {
                    
                    @Override
                    public void updateItem(WaiterBean cache, boolean empty) {
                        super.updateItem(cache, empty);
                        if (!empty)
                            this.setText(cache.getName()); // this is the important line
                    }
                    
                };
            }
            
        });
        
        // order list
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<OrderBean, String>(OrderBean.PROP_ORDER_NUMBER));
        colOrderTableNumber.setCellValueFactory(new PropertyValueFactory<OrderBean, String>(OrderBean.PROP_TABLE_NUMBER));
        colOrderSum.setCellValueFactory(new PropertyValueFactory<OrderBean, String>(OrderBean.PROP_SUM_OF_MONEY));
        colOrderFinalized.setCellValueFactory(new PropertyValueFactory<OrderBean, Boolean>(OrderBean.PROP_FINALIZED));
        
        // order item list
        colItemName.setCellValueFactory(new PropertyValueFactory<ItemBean, String>(ItemBean.PROP_NAME));
        colItemPrice.setCellValueFactory(new PropertyValueFactory<ItemBean, String>(ItemBean.PROP_PRICE));
        
        /*txtOrderItemName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                System.out.println(t.toString());
            }
        });*/
        
        loadData();
        
    }
    
    private void loadData () {
        
         // example data
        availableItems.add(new ItemBean("Pina Colada",500));
        availableItems.add(new ItemBean("Bloody Mary",550));
        availableItems.add(new ItemBean("Swimming Pool",600));
        availableItems.add(new ItemBean("Zombie",650));
        
        for (String s : "Thomas|Johnson|Peter|Alfred".split("\\|")) {
            WaiterBean waiter = new WaiterBean(s);
            waiters.add(waiter);
            for (int i = 0; i < Math.pow(Math.random() + 0.9, 6); i++) {
                waiter.addOrder(new OrderBean(waiter, (int)(Math.random()*10000), (int)(Math.random()*20+1), Math.random() < 0.3));
                System.out.println(waiter.getOrders().get(waiter.getOrders().size()-1));
            }
        }
        
        // load data in view
        lstWaiter.setItems(FXCollections.observableList(waiters));
        
    }
    
    @FXML
    private void lstWaiter_clicked(MouseEvent t) {
        WaiterBean waiter = (WaiterBean) lstWaiter.getSelectionModel().getSelectedItem();
        tblOrder.setItems(FXCollections.observableList(waiter.getOrders()));
    }
    
    @FXML
    private void txtOrderItemName_clicked(MouseEvent t) {
        System.out.println("~~~ Test: " + t.toString());
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
    }
    
}
