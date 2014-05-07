/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.model.ItemBean;
import iwaiter.model.OrderBean;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author viktormagdych
 */
public class BillController extends IWaiterController implements Initializable {
    @FXML
    private Label waiterName;
    @FXML
    private Label Total;
    @FXML
    private TableView tableBill;
    @FXML
    private TableColumn itemBill;
    @FXML
    private TableColumn priseBill;

    iwaiter.model.OrderBean curOrder;
   Pane pane;
    @FXML
    private Button printButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         itemBill.setCellValueFactory(new PropertyValueFactory<ItemBean, String>(ItemBean.PROP_NAME));
        priseBill.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemBean,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ItemBean, String> p) {
                DecimalFormat df = new DecimalFormat("###,##0.00 €");
                // is it possible to call the property directly without wrapping it? i'd like to keep the binding OrderBean.PROP_SUM_OF_MONEY
                return new ReadOnlyObjectWrapper(df.format((double)p.getValue().getPrice()/100));
            }
        });
    }    

    void initData(iwaiter.model.OrderBean curOrder,Pane pane) {
        this.curOrder=curOrder;
        tableBill.setItems(FXCollections.observableList(curOrder.getOrderItems()));
        Total.setText(""+(curOrder.getSumOfMoney()/100)+"€");
        waiterName.setText(curOrder.getWaiter().getName());
        this.pane=pane;
    }

    @FXML
    private void printing(ActionEvent event) {
        
        PrinterJob job = PrinterJob.createPrinterJob();
	if (job != null) {
		boolean success = job.printPage(pane);
		if (success) {
			System.out.println("printed successfully");
			job.endJob();
		}
	}
    }

}
