package iwaiter.model;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
public class OrderBean implements Serializable {
    
    private int orderNumber;
    public static final String PROP_ORDER_NUMBER= "orderNumber";
    
    private TableBean table;
    public static final String PROP_TABLE = "table";
    
    private int sumOfMoney = 0;
    public static final String PROP_SUM_OF_MONEY = "sumOfMoney";
    
    private boolean calculated = false; // to not calculate the sum reduntantly
    
    private boolean finalized = false;
    public static final String PROP_FINALIZED = "finilized";
    
    private ArrayList<ItemBean> orderItems = new ArrayList<>();
    public static final String PROP_ORDER_ITEMS = "orderItems";
    
    private WaiterBean waiter;
    public static final String PROP_WAITER = "waiter";
    
    private PropertyChangeSupport propertySupport;
    
    /**
     * ctor
     * @param waiter 
     */
    public OrderBean(WaiterBean waiter) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(12345);
        this.setTable(new TableBean(42));
    }
    
    /**
     * 
     * @param waiter
     * @param orderNumber
     * @param tableNumber 
     */
    public OrderBean(WaiterBean waiter, int orderNumber, TableBean tableNumber) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(orderNumber);
        this.setTable(tableNumber);
    }
    
    /**
     * 
     * @param waiter
     * @param orderNumber
     * @param tableNumber
     * @param finalized 
     */
    public OrderBean(WaiterBean waiter, int orderNumber, TableBean tableNumber, boolean finalized) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(orderNumber);
        this.setTable(tableNumber);
        this.setFinalize(finalized);
    }
    
    /**
     * 
     * @return orderNumber
     */
    public int getOrderNumber() {
        return this.orderNumber;
    }
    
    /**
     * 
     * @param value 
     */
    public final void setOrderNumber(int value) {
        int oldValue = this.orderNumber;
        this.orderNumber = value;
        this.propertySupport.firePropertyChange(PROP_ORDER_NUMBER, oldValue, this.orderNumber);
    }
    
    /**
     * 
     * @return 
     */
    public TableBean getTable() {
        return this.table;
    }
    
    /**
     * 
     * @param value 
     */
    public final void setTable(TableBean value) {
        TableBean oldValue = this.table;
        this.table = value;
        this.propertySupport.firePropertyChange(PROP_TABLE, oldValue, this.table);
    }
    
    /**
     * 
     * @return sumOfMoney
     */
    public int getSumOfMoney() {
        if (!calculated)
            this.calculateSum();
        return this.sumOfMoney;
    }
    
    /**
     * 
     * @param value 
     */
    private void calculateSum() {
        int oldValue = this.sumOfMoney;
        this.sumOfMoney = 0;
        for (ItemBean i : orderItems)
            this.sumOfMoney += i.getPrice();
        this.propertySupport.firePropertyChange(PROP_SUM_OF_MONEY, oldValue, this.sumOfMoney);
    }
    
    /**
     * 
     * @return finalized
     */
    public Boolean isFinalized() {
        return this.finalized;
    }
    
    /**
     * 
     */
    public void setFinalize() {
        this.setFinalize(true);
    }
    
    /**
     * 
     * @param value 
     */
    public final void setFinalize(boolean value) {
        boolean oldValue = this.finalized;
        this.finalized = value;
        this.propertySupport.firePropertyChange(PROP_FINALIZED, oldValue, this.finalized);
    }
    
    /**
     * 
     * @return orderItems
     */
    public ArrayList<ItemBean> getOrderItems() {
        return this.orderItems;
    }
    
    /**
     * 
     * @param item 
     */
    public void addOrderItem(ItemBean item) {
        this.orderItems.add(item);
        /** @todo: propertySupport.firePropertyChange */
        this.calculated = false;
    }
    
    /**
     * 
     * @param item
     * @throws iwaiter.model.OrderBean.RemoveOrderItemException 
     */
    public void removeOrderItem(ItemBean item) throws RemoveOrderItemException {
        if (!orderItems.remove(item))
            throw new RemoveOrderItemException();
        /** @todo: propertySupport.firePropertyChange */
        this.calculated = false;
    }
    
    /**
     * 
     * @param oldItem
     * @param newItem 
     */
    public void setOrderItem(ItemBean oldItem, ItemBean newItem) {
        for(ItemBean i : this.orderItems)
            if (i.equals(oldItem))
                i = newItem;
        /** @todo: propertySupport.firePropertyChange */
        this.calculated = false;
    }
    
    /**
     * 
     * @return waiter
     */
    public WaiterBean getWaiter() {
        return this.waiter;
    }
    
    /**
     * 
     * @param value 
     */
    public final void setWaiter(WaiterBean value) {
        WaiterBean oldValue = this.waiter;
        this.waiter = value;
        this.propertySupport.firePropertyChange(PROP_WAITER, oldValue, this.waiter);
    }
    
    @Override
    public String toString() {
        return "OrderBean{" + 
                "orderNumber=" + this.orderNumber + 
                ", tableNumber=" + this.table + 
                ", sumOfMoney=" + this.sumOfMoney + 
                ", finalized=" + this.finalized + 
                ", orderItems=" + this.orderItems.size() + 
                ", waiter=" + this.waiter.getName() + 
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final OrderBean other = (OrderBean) obj;
        return this.orderNumber == other.orderNumber 
                && this.table == other.table
                && this.sumOfMoney == other.sumOfMoney
                && this.finalized == other.finalized
                && Objects.equals(this.orderItems, other.orderItems)
                && Objects.equals(this.waiter, other.waiter);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.orderNumber;
        hash = 67 * hash + Objects.hashCode(this.table);
        hash = 67 * hash + this.sumOfMoney;
        hash = 67 * hash + (this.finalized ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.orderItems);
        hash = 67 * hash + Objects.hashCode(this.waiter);
        return hash;
    }
    
    /**
     * 
     * @param listener 
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * 
     * @param listener 
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * 
     */
    public static class RemoveOrderItemException extends Exception {

        public RemoveOrderItemException() {
        }

        public RemoveOrderItemException(String message) {
            super(message);
        }

        public RemoveOrderItemException(String message, Throwable cause) {
            super(message, cause);
        }

        public RemoveOrderItemException(Throwable cause) {
            super(cause);
        }

        public RemoveOrderItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
    
}
