/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.ItemBean;
import iwaiter.model.OrderBean;
import iwaiter.model.TableBean;
import iwaiter.model.WaiterBean;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class MainWindowController extends IWaiterController implements Initializable {
    
    /**
     * Model
     */
    ObservableList<WaiterBean> waiters;
    ObservableList<ItemBean> availableItems;
    ObservableList<TableBean> tables;
    
    /**
     * View
     */
    
    @FXML
    private AnchorPane content;
    
    @FXML
    private Button cmdImport;
    
    @FXML
    private ListView lstWaiter;
    
    @FXML
    private Label lblWaiter;
    
    @FXML
    private TableView tblOrder; // list of orders (of a waiter)
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
    private Button cmdFinalizeOrder;
    
    @FXML
    private TextField txtOrderNumber;
    @FXML
    private ComboBox lstTable;
    
    @FXML
    private TableView tblOrderItem; // list of order items (of an order)
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
    
    /**
     * Controller fields
     */
    WaiterBean curWaiter; // pointer for currently selected waiter
    OrderBean curOrder; // -||- order
    ItemBean curItem; // -||- order item
    
    private boolean mutexNoEvent = false; // flag for fxml objects to not interact
    
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
                        // is it possible to call the property directly? i'd like to have the binding WaiterBean.PROP_NAME
                        if (!empty)
                            this.setText(cache.getName()); // this is the important line
                    }
                    
                };
            }
            
        });
        
        // order list
        colOrderNumber.setCellValueFactory(new PropertyValueFactory<OrderBean, String>(OrderBean.PROP_ORDER_NUMBER));
        colOrderTableNumber.setCellValueFactory(new PropertyValueFactory<OrderBean, String>(OrderBean.PROP_TABLE));
        colOrderSum.setCellValueFactory(new Callback<CellDataFeatures<OrderBean,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<OrderBean, String> p) {
                DecimalFormat df = new DecimalFormat("###,##0.00 €");
                // is it possible to call the property directly without wrapping it? i'd like to keep the binding OrderBean.PROP_SUM_OF_MONEY
                return new ReadOnlyObjectWrapper(df.format((double)p.getValue().getSumOfMoney()/100));
            }
        });
        colOrderFinalized.setCellValueFactory(new Callback<CellDataFeatures<OrderBean,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<OrderBean, String> p) {
                // is it possible to call the property directly without wrapping it? i'd like to keep the binding OrderBean.PROP_FINALIZED
                return new ReadOnlyObjectWrapper(p.getValue().isFinalized() ? "ja" : "nein");
            }
        });
        
        // order item list
        colItemName.setCellValueFactory(new PropertyValueFactory<ItemBean, String>(ItemBean.PROP_NAME));
        colItemPrice.setCellValueFactory(new Callback<CellDataFeatures<ItemBean,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<ItemBean, String> p) {
                DecimalFormat df = new DecimalFormat("###,##0.00 €");
                // is it possible to call the property directly without wrapping it? i'd like to keep the binding OrderBean.PROP_SUM_OF_MONEY
                return new ReadOnlyObjectWrapper(df.format((double)p.getValue().getPrice()/100));
            }
        });
        
        // default view settings
        //tblOrder.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //tblOrderItem.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        lstWaiter_unload();
    }
    
    /**
     * Event for importing qualitative data.
     * (List of waiters, list of tables and list or available order items.)
     * @param t 
     */
    @FXML
    private void cmdImport_click(Event t) {
        
        // (re-)initialize models
        waiters = FXCollections.observableArrayList();
        availableItems = FXCollections.observableArrayList();
        tables = FXCollections.observableArrayList();
        
        // randomly generated example data
        for (int i = 1; i <= (int)(Math.random()*8)+5; i++)
            tables.add(new TableBean(i));
        
        System.out.println("tables: " + tables);
        
        availableItems.add(new ItemBean("Pina Colada",500));
        availableItems.add(new ItemBean("Bloody Mary",550));
        availableItems.add(new ItemBean("Swimming Pool",600));
        availableItems.add(new ItemBean("Zombie",650));
        
        System.out.println("availableItems: " + availableItems);
        
        for (String s : "Thomas|Johnson|Peter|Alfred".split("\\|")) {
            WaiterBean waiter = new WaiterBean(s);
            waiters.add(waiter);
            for (int i = 0; i < Math.pow(Math.random() + 1, 4); i++) { // random amount of orders
                OrderBean order = new OrderBean(waiter, 
                        (int)(Math.random()*10000), // random order number
                        tables.get((int)(Math.random()*tables.size())), // random table
                        Math.random() < 0.5); // random finalized status
                waiter.addOrder(order);
                for (int j = 0; j < Math.pow(Math.random() + 1, 4); j++) // random amount of random order items
                    order.addOrderItem(new ItemBean(availableItems.get((int)(Math.random()*availableItems.size()))));
            }
        }
        
        System.out.println("waiters: " + waiters);
        
        // (re-)set sub-views for quantitative data
        tblOrder_unload();
        
        // load qualitative data in sub-views
        lstWaiter.setItems(FXCollections.observableList(waiters));
        lstTable.setItems(FXCollections.observableList(tables));
    }
    
    /**
     * Event for selecting a waiter from the waiter list.
     * @param t 
     */
    @FXML
    private void lstWaiter_select(Event t) {
        if (lstWaiter.getSelectionModel().getSelectedItem() == null)
            return;
        curWaiter = (WaiterBean) lstWaiter.getSelectionModel().getSelectedItem();
        
        lblWaiter.setText(curWaiter.getName());
        tblOrder.setItems(FXCollections.observableList(curWaiter.getOrders()));
        tblOrder.setDisable(false);
        cmdNewOrder.setDisable(false);
        
        tblOrderItem_unload();
    }
    
    /**
     * Reset references connected to the waiter list.
     */
    private void lstWaiter_unload() {
        curWaiter = null;
        
        lstWaiter.setItems(null);
        
        tblOrder_unload();
    }
    
    /**
     * Event for selecting an order from the order list.
     * @param t 
     */
    @FXML
    private void tblOrder_select(Event t) {
        loadOrder();
    }
    
    /**
     * Loads properties and dependencies of the currenty selected order for fxml fields.
     */
    public void loadOrder() {
        if (tblOrder.getSelectionModel().getSelectedItem() == null)
            return;
        curOrder = (OrderBean) tblOrder.getSelectionModel().getSelectedItem();
        
        cmdDelOrder.setDisable(false);
        cmdFinalizeOrder.setDisable(curOrder.isFinalized());
        txtOrderNumber.setText(String.valueOf(curOrder.getOrderNumber()));
        txtOrderNumber.setDisable(false);
        mutexNoEvent = true;
        lstTable.setValue(curOrder.getTable());
        mutexNoEvent = false;
        lstTable.setDisable(false);
        tblOrderItem.setItems(FXCollections.observableList(curOrder.getOrderItems()));
        //lstTable.getSelectionModel().clearSelection();
        tblOrderItem.setDisable(false);
        cmdNewOrderItem.setDisable(false);
        
        txtOrderItemName_unload();
    }
    /**
     * Resets references connected to the order list.
     */
    private void tblOrder_unload() {
        curOrder = null;
        
        lblWaiter.setText("(kein Kellner gewählt)");
        tblOrder.setItems(null);
        tblOrder.setDisable(true);
        cmdNewOrder.setDisable(true);
        
        tblOrderItem_unload();
    }
    
    /**
     * Event for adding a new order to the order list.
     * @param t 
     */
    @FXML
    private void cmdNewOrder_Click(Event t) {
        if (curWaiter == null)
            return;
        OrderBean order = new OrderBean(curWaiter);
        tblOrder.getItems().add(order);
        tblOrder.getSelectionModel().select(order);
        loadOrder();
    }
    
    /**
     * Event for deleting the currently selected order from the order list.
     * @param t
     * @throws iwaiter.model.WaiterBean.RemoveOrderException 
     */
    @FXML
    private void cmdDelOrder_click(Event t) throws WaiterBean.RemoveOrderException {
        if (tblOrder.getSelectionModel().getSelectedItem() == null)
            return;
        tblOrder.getItems().remove(tblOrder.getSelectionModel().getSelectedIndex());
        if (tblOrder.getSelectionModel().getSelectedItem() == null)
            tblOrderItem_unload();
    }
    
    /**
     * Event for editing the order number of the currently selected order.
     * @param t 
     */
    @FXML
    private void txtOrderNumber_change(Event t) {
        if (curOrder == null || txtOrderNumber.getText().isEmpty())
            return;
        try {
            curOrder.setOrderNumber(Integer.parseInt(txtOrderNumber.getText()));
        } catch (NumberFormatException e) {
            
        }
        refreshColumn(colOrderNumber);
    }
    
    /**
     * Event for changing the table of the currently selected order.
     * @param t 
     */
    @FXML
    private void lstTable_change(Event t) {
        if (mutexNoEvent || curOrder == null || 
                lstTable.getSelectionModel().getSelectedItem() == null)
            return;
        curOrder.setTable((TableBean) lstTable.getSelectionModel().getSelectedItem());
        refreshColumn(colOrderTableNumber);
    }
    
    /**
     * Event for setting the currently selecter order to finalized.
     * @param t 
     */
    @FXML
    private void cmdFinalizeOrder_click(Event t) {
        if (curOrder == null)
            return;
        if (!curOrder.isFinalized())
            curOrder.setFinalize();
        cmdFinalizeOrder.setDisable(true);
        refreshColumn(colOrderFinalized);
    }
    
    /**
     * Event for adding a new order item to the order item list.
     * @param t 
     */
    @FXML
    private void cmdNewOrderItem_Click(Event t) throws IOException {
        if (curOrder == null)
            return;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iwaiter/view/NewItemWindow.fxml"));
        Pane pane = (Pane) loader.load();
        NewItemWindowController controller = loader.getController();
        controller.initData(availableItems);
        controller.setCorrespondent(this);
        //controller.setParentStage(this.getParentStage());
        //controller.setPreviousScene(this.getParentStage().getScene());
        
        Stage stage = new Stage();
        stage.setTitle("Bestelleinheit hinzufügen");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(pane));
        stage.show();
        
        //tblOrderItem.getItems().add(new ItemBean());
    }
    
    /**
     * Adds a new order item to the order item list.
     * @param item 
     */
    public void addOrderItem(ItemBean item) {
        if (curOrder == null)
            return;
        tblOrderItem.getItems().add(new ItemBean(item));
        tblOrderItem.getSelectionModel().select(item);
        loadOrderItem();
    }
    
    /**
     * Event for deleting the currently selected order item from the order item list.
     * @param t
     * @throws iwaiter.model.OrderBean.RemoveOrderItemException 
     */
    @FXML
    private void cmdDelOrderItem_click(Event t) throws OrderBean.RemoveOrderItemException {
        if (tblOrderItem.getSelectionModel().getSelectedItem() == null)
            return;
        tblOrderItem.getItems().remove(tblOrderItem.getSelectionModel().getSelectedIndex());
        if (tblOrderItem.getSelectionModel().getSelectedItem() == null)
            txtOrderItemName_unload();
    }
    
    /**
     * Event for selecting an order item from the order item list.
     * @param t 
     */
    @FXML
    private void tblOrderItem_select(Event t) {
        loadOrderItem();
    }
    
    /**
     * Loads properties of the currently selected order item for fxml fields.
     */
    public void loadOrderItem() {
        if (curOrder == null ||
                tblOrderItem.getSelectionModel().getSelectedItem() == null)
            return;
        curItem = (ItemBean) tblOrderItem.getSelectionModel().getSelectedItem();
        
        cmdDelOrderItem.setDisable(false);
        txtOrderItemName.setText(curItem.getName());
        txtOrderItemName.setDisable(false);
        txtOrderItemPrice.setText(String.valueOf(curItem.getPrice()));
        txtOrderItemPrice.setDisable(false);
    }
    
    /**
     * Resets references connected to the order list.
     */
    private void tblOrderItem_unload() {
        curItem = null;
        
        cmdDelOrder.setDisable(true);
        cmdFinalizeOrder.setDisable(true);
        txtOrderNumber.setDisable(true);
        lstTable.setDisable(true);
        tblOrderItem.setItems(null);
        tblOrderItem.setDisable(true);
        cmdNewOrderItem.setDisable(true);
        
        txtOrderItemName_unload();
    }
    
    /**
     * Event for editing the name of the currently selected order item.
     * @param t 
     */
    @FXML
    private void txtOrderItemName_change(Event t) {
        if (curItem == null || txtOrderItemName.getText().isEmpty())
            return;
        curItem.setName(txtOrderItemName.getText());
        refreshColumn(colItemName);
    }
    
    /**
     * Resets references connected to the order item list.
     */
    private void txtOrderItemName_unload() {
        cmdDelOrderItem.setDisable(true);
        txtOrderItemName.setDisable(true);
        txtOrderItemPrice.setDisable(true);
    }
    
    /**
     * Event for editing the price of the currently selected order item.
     * @param t 
     */
    @FXML
    private void txtOrderItemPrice_change(Event t) {
        if (curItem == null || txtOrderItemPrice.getText().isEmpty())
            return;
        try {
            curItem.setPrice(Integer.parseInt(txtOrderItemPrice.getText()));
        } catch (NumberFormatException e) {
            
        }
        refreshColumn(colItemPrice);
        refreshColumn(colOrderSum);
    }
    
    /**
     * Refreshes table columns for changes to see.
     * @param tc 
     */
    private void refreshColumn(TableColumn tc) {
        tc.setVisible(false); // resolving table view column refresh issue
        tc.setVisible(true);
    }
    
}
