/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.ItemBean;
import iwaiter.entity.AvailableItemEntity;
import iwaiter.entity.OrderEntity;
import iwaiter.entity.OrderItemEntity;
import iwaiter.model.OrderBean;
import iwaiter.model.TableBean;
import iwaiter.entity.TableEntity;
import iwaiter.model.WaiterBean;
import iwaiter.entity.WaiterEntity;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JTextPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.1
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
    private Button cmdFinalizeOrder;
    @FXML
    private Button cmdPrintOrder;
    
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
    private Button cmdSaveChanges;
    @FXML
    private TextField txtOrderItemName;
    @FXML
    private TextField txtOrderItemPrice;
    
    /**
     * Controller fields
     */
    WaiterBean curWaiterBean; // pointer for currently selected waiter
    OrderBean curOrderBean; // -||- order
    ItemBean curItemBean; // -||- order item
    
    private boolean mutexNoEvent = false; // flag for fxml objects to not interact
    
    /**
     *Persistence
     */
    PersistenceManager pm = new PersistenceManager();
    
    WaiterEntity curWaiterEntity; // pointer for currently selected waiter
    OrderEntity curOrderEntity; // -||- order
    OrderItemEntity curItemEntity; // -||- order item
    
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
        
        // order lists
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
        
        // order item lists
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
        lstWaiter_unload();
        loadMainDatabaseTables();
    }
    
    /**
     * Loads the waiters and the tables from the database tables.
     */
    private void loadMainDatabaseTables() {
        // (re-)initialize models
        waiters = FXCollections.observableArrayList();
        availableItems = FXCollections.observableArrayList();
        tables = FXCollections.observableArrayList();
        
        // load entities and their respective beans
        // -- waiters
        for(Object e : pm.allEntries(WaiterEntity.class))
            waiters.add(new WaiterBean(((WaiterEntity)e).getName()));
        // -- available items
        for(Object e : pm.allEntries(AvailableItemEntity.class)) {
            AvailableItemEntity item = (AvailableItemEntity) e;
            availableItems.add(new ItemBean(item.getId(), item.getName(), item.getPrice()));
        }
        // -- tables
        for(Object e : pm.allEntries(TableEntity.class))
            tables.add(new TableBean(((TableEntity)e).getTableNumber()));
        
        // apply waiters and tables to view
        lstWaiter.setItems(FXCollections.observableList(waiters));
        lstTable.setItems(FXCollections.observableList(tables));
    }
    
    /**
     * Event for importing qualitative data.
     * (List of waiters, list of tables and list or available order items.)
     * @param t 
     */
    @FXML
    private void cmdImport_click(Event t) {
        try {
            // get file
            FileChooser fileChooser = new FileChooser();
            ExtensionFilter extFilter = new FileChooser.ExtensionFilter("xml-Dateien (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(null);
            
            // process file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList nList;
            // import waiters
            nList = doc.getElementsByTagName("waiters").item(0).getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nNode).getAttribute("name");
                    if (pm.findWaiter(name) == null) {
                        WaiterEntity waiter = new WaiterEntity();
                        waiter.setName(name);
                        pm.create(waiter);
                    }
                }
            }
            // import tables
            nList = doc.getElementsByTagName("tables").item(0).getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    int tablenumber = Integer.parseInt(((Element) nNode).getAttribute("tablenumber"));
                    if (pm.findTable(tablenumber) == null) {
                        TableEntity table = new TableEntity();
                        table.setTableNumber(tablenumber);
                        pm.create(table);
                    }
                }
            }
            // import order items
            nList = doc.getElementsByTagName("items").item(0).getChildNodes();
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    String name = ((Element) nNode).getAttribute("name");
                    int price = Integer.parseInt(((Element) nNode).getAttribute("price"));
                    if (pm.findAvailableItem(name) == null) {
                        AvailableItemEntity item = new AvailableItemEntity();
                        item.setName(name);
                        item.setPrice(price);
                        pm.create(item);
                    }
                }
            }
            
            // (re-)set sub-views for quantitative data
            tblOrder_unload();
            
            // load qualitative data in sub-views
            loadMainDatabaseTables();
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Event for selecting a waiter from the waiter list.
     * @param t 
     */
    @FXML
    private void lstWaiter_select(Event t) {
        loadWaiter();
    }
    
    /**
     * Loads properties and dependencies of the currenty selected waiter for fxml fields.
     */
    public void loadWaiter() {
        if (lstWaiter.getSelectionModel().getSelectedItem() == null)
            return;
        curWaiterBean = (WaiterBean) lstWaiter.getSelectionModel().getSelectedItem();
        curWaiterBean.getOrders().clear();
        
        curWaiterEntity = pm.findWaiter(curWaiterBean.getName());
        for(OrderEntity e : curWaiterEntity.getOrders()) {
            TableBean table = null;
            for (TableBean b : tables)
                if (e.getTable().getTableNumber() == b.getTableNumber()) {
                    table = b;
                    break;
                }
            OrderBean order = new OrderBean(curWaiterBean, e.getOrderNumber(), table, e.getSumOfMoney(), e.isFinalized());
            curWaiterBean.getOrders().add(order);
        }
        
        lblWaiter.setText(curWaiterBean.getName());
        tblOrder.setItems(FXCollections.observableList(curWaiterBean.getOrders()));
        tblOrder.setDisable(false);
        cmdNewOrder.setDisable(false);
        
        tblOrderItem_unload();
    }
    
    /**
     * Reset references connected to the waiter list.
     */
    private void lstWaiter_unload() {
        curWaiterBean = null;
        
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
        curOrderBean = (OrderBean) tblOrder.getSelectionModel().getSelectedItem();
        curOrderBean.getOrderItems().clear();
        
        curOrderEntity = pm.findOrder(curOrderBean.getOrderNumber());
        for(OrderItemEntity e : curOrderEntity.getOrderItems())
            curOrderBean.getOrderItems().add(new ItemBean(e.getId(), e.getName(), e.getPrice()));
        
        boolean fin = curOrderBean.isFinalized();
        cmdDelOrder.setDisable(false);
        cmdFinalizeOrder.setDisable(fin);
        cmdPrintOrder.setDisable(!fin);
        txtOrderNumber.setText(String.valueOf(curOrderBean.getOrderNumber()));
        //txtOrderNumber.setDisable(fin);
        mutexNoEvent = true;
        lstTable.setValue(curOrderBean.getTable());
        mutexNoEvent = false;
        lstTable.setDisable(fin);
        tblOrderItem.setItems(FXCollections.observableList(curOrderBean.getOrderItems()));
        //lstTable.getSelectionModel().clearSelection();
        tblOrderItem.setDisable(false);
        cmdNewOrderItem.setDisable(fin);
        cmdSaveChanges.setDisable(true);
        
        txtOrderItemName_unload();
    }
    /**
     * Resets references connected to the order list.
     */
    private void tblOrder_unload() {
        curOrderBean = null;
        
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
    private void cmdNewOrder_Click(Event t) throws IOException {
        if (curWaiterBean == null)
            return;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iwaiter/view/NewOrderWindow.fxml"));
        Pane pane = (Pane) loader.load();
        NewOrderWindowController controller = loader.getController();
        controller.initData(tables);
        controller.setCorrespondent(this);
        
        Stage stage = new Stage();
        stage.setTitle("Neue Bestellung erstellen");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(pane));
        stage.show();
    }
    
    /**
     * Adds a new order to the order list. 
     * @param table
     */
    public void addOrder(TableBean table) {
        if (curWaiterBean == null)
            return;
        
        OrderBean newOrder = new OrderBean(curWaiterBean, 0, table);
        tblOrder.getItems().add(newOrder);
        
        OrderEntity e = new OrderEntity();
        pm.create(e);
        e.setTable(pm.findTable(table.getTableNumber()));
        e.setWaiter(curWaiterEntity);
        pm.update(e);
        curWaiterEntity.getOrders().add(e);
        pm.update(curWaiterEntity);
        
        newOrder.setOrderNumber(e.getOrderNumber()); // get database generated value
        tblOrder.getSelectionModel().select(newOrder);
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
        
        curWaiterEntity.getOrders().remove(curOrderEntity);
        pm.update(curWaiterEntity);
        
        tblOrder.getSelectionModel().clearSelection();
        //if (tblOrder.getSelectionModel().getSelectedItem() == null)
            tblOrderItem_unload();
    }
    
    /**
     * Event for editing the order number of the currently selected order.
     * @param t 
     */
    @FXML
    private void txtOrderNumber_change(Event t) {
        if (curOrderBean == null || txtOrderNumber.getText().isEmpty())
            return;
        try {
            curOrderBean.setOrderNumber(Integer.parseInt(txtOrderNumber.getText()));
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
        if (mutexNoEvent || curOrderBean == null || 
                lstTable.getSelectionModel().getSelectedItem() == null)
            return;
        
        TableBean table = (TableBean) lstTable.getSelectionModel().getSelectedItem();
        curOrderBean.setTable(table);
        refreshColumn(colOrderTableNumber);
        
        curOrderEntity.setTable(pm.findTable(table.getTableNumber()));
        pm.update(curOrderEntity);
    }
    
    /**
     * Event for setting the currently selecter order to finalized.
     * @param t 
     */
    @FXML
    private void cmdFinalizeOrder_click(Event t) {
        if (curOrderBean == null)
            return;
        
        if (!curOrderBean.isFinalized()) {
            curOrderEntity.setFinalized();
            pm.update(curOrderEntity);
            
            curOrderBean.setFinalized();
        }
        loadOrder();
        //cmdFinalizeOrder.setDisable(true);
        refreshColumn(colOrderFinalized);
    }
    
    /**
     * Event for printing the currently selected order.
     * @param t 
     */
    @FXML
    private void cmdPrintOrder_click(Event t) throws PrinterException {
        
        // build text
        String text = "iWaiter Bestellung" + 
                "\n\nKeller: " + curWaiterBean.getName() + 
                "\nBestellnummer: " + curOrderBean.getOrderNumber() + 
                "\nTisch: " + curOrderBean.getTable().getTableNumber() + 
                "\n";
        DecimalFormat df = new DecimalFormat("###,##0.00 €");
        for (ItemBean item : curOrderBean.getOrderItems())
            text += "\n" + item.getName() + " " + df.format((double)item.getPrice()/100);
        text += "\n\nSumme " + df.format((double)curOrderBean.getSumOfMoney()/100);
        
        // print text
        JTextPane textPane = new JTextPane();
        textPane.setText(text);
        textPane.print();
        
    }
    
    /**
     * Event for adding a new order item to the order item list.
     * @param t 
     */
    @FXML
    private void cmdNewOrderItem_Click(Event t) throws IOException {
        if (curOrderBean == null)
            return;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iwaiter/view/NewItemWindow.fxml"));
        Pane pane = (Pane) loader.load();
        NewItemWindowController controller = loader.getController();
        controller.initData(availableItems);
        controller.setCorrespondent(this);
        
        Stage stage = new Stage();
        stage.setTitle("Bestelleinheit hinzufügen");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(pane));
        stage.show();
    }
    
    /**
     * Adds a new order item to the order item list.
     * @param item 
     */
    public void addOrderItem(ItemBean item) {
        if (curOrderBean == null)
            return;
        
        ItemBean newItem = new ItemBean(item);
        tblOrderItem.getItems().add(newItem);
        curOrderBean.calculateSum();
        
        OrderItemEntity e = new OrderItemEntity();
        e.setName(newItem.getName());
        e.setPrice(newItem.getPrice());
        pm.create(e);
        curOrderEntity.getOrderItems().add(e);
        curOrderEntity.setSumOfMoney(curOrderBean.getSumOfMoney());
        pm.update(curOrderEntity);
        
        newItem.setId(e.getId()); // get database generated value
        tblOrderItem.getSelectionModel().select(newItem);
        loadOrderItem();
        refreshColumn(colOrderSum);
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
        curOrderBean.calculateSum();
        
        curOrderEntity.getOrderItems().remove(curItemEntity);
        curOrderEntity.setSumOfMoney(curOrderBean.getSumOfMoney());
        pm.update(curOrderEntity);
        
        tblOrderItem.getSelectionModel().clearSelection();
        //if (tblOrderItem.getSelectionModel().getSelectedItem() == null)
            txtOrderItemName_unload();
        refreshColumn(colOrderSum);
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
        if (curOrderBean == null ||
                tblOrderItem.getSelectionModel().getSelectedItem() == null)
            return;
        curItemBean = (ItemBean) tblOrderItem.getSelectionModel().getSelectedItem();
        
        curItemEntity = pm.findOrderItem(curItemBean.getId());
        
        boolean fin = curOrderBean.isFinalized();
        cmdDelOrderItem.setDisable(fin);
        txtOrderItemName.setText(curItemBean.getName());
        txtOrderItemName.setDisable(fin);
        txtOrderItemPrice.setText(String.valueOf(curItemBean.getPrice()));
        txtOrderItemPrice.setDisable(fin);
    }
    
    /**
     * Resets references connected to the order list.
     */
    private void tblOrderItem_unload() {
        curItemBean = null;
        
        cmdDelOrder.setDisable(true);
        cmdFinalizeOrder.setDisable(true);
        cmdPrintOrder.setDisable(true);
        txtOrderNumber.setDisable(true);
        lstTable.setDisable(true);
        tblOrderItem.setItems(null);
        tblOrderItem.setDisable(true);
        cmdNewOrderItem.setDisable(true);
        
        cmdSaveChanges.setDisable(true);
        txtOrderItemName_unload();
    }
    
    /**
     * Event for editing the name of the currently selected order item.
     * @param t 
     */
    @FXML
    private void txtOrderItemName_change(Event t) {
        if (curItemBean == null || txtOrderItemName.getText().isEmpty())
            return;
        curItemBean.setName(txtOrderItemName.getText());
        cmdSaveChanges.setDisable(false);
        //refreshColumn(colItemName);
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
        if (curItemBean == null || txtOrderItemPrice.getText().isEmpty())
            return;
        try {
            curItemBean.setPrice(Integer.parseInt(txtOrderItemPrice.getText()));
        } catch (NumberFormatException e) {
            
        }
        cmdSaveChanges.setDisable(false);
        //refreshColumn(colItemPrice);
        //refreshColumn(colOrderSum);
    }
    
    /**
     * Event for saving the changes made on an order item onto the database.
     * @param t 
     */
    @FXML
    private void cmdSaveChanges_click(Event t) {
        // it is assumed here that curItemBean is up-to-date and needs to be persisted into curItemEntity
        curOrderBean.calculateSum();
        
        curItemEntity.setName(curItemBean.getName());
        curItemEntity.setPrice(curItemBean.getPrice());
        pm.update(curItemEntity);
        
        curOrderEntity = pm.findOrder(curOrderBean.getOrderNumber());
        curOrderEntity.setSumOfMoney(curOrderBean.getSumOfMoney());
        pm.update(curOrderEntity);
        
        refreshColumn(colItemPrice);
        refreshColumn(colOrderSum);
        
        cmdSaveChanges.setDisable(true);
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
