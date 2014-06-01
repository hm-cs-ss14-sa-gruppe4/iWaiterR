package iwaiter.model;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.1
 */
public class OrderBean implements Serializable {
    
    private long orderNumber;
    public static final String PROP_ORDER_NUMBER= "orderNumber";
    
    private TableBean table;
    public static final String PROP_TABLE = "table";
    
    private int sumOfMoney = 0;
    public static final String PROP_SUM_OF_MONEY = "sumOfMoney";
    
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
     * ctor
     * @param waiter
     * @param orderNumber
     * @param table 
     */
    public OrderBean(WaiterBean waiter, long orderNumber, TableBean table) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(orderNumber);
        this.setTable(table);
    }
    
    /**
     * ctor
     * @param waiter
     * @param orderNumber
     * @param table 
     * @param sumOfMoney 
     */
    public OrderBean(WaiterBean waiter, long orderNumber, TableBean table, int sumOfMoney) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(orderNumber);
        this.setTable(table);
        this.sumOfMoney = sumOfMoney;
    }
    
    /**
     * ctor
     * @param waiter
     * @param orderNumber
     * @param tableNumber
     * @param sumOfMoney
     * @param finalized 
     */
    public OrderBean(WaiterBean waiter, long orderNumber, TableBean tableNumber, int sumOfMoney, boolean finalized) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setWaiter(waiter);
        this.setOrderNumber(orderNumber);
        this.setTable(tableNumber);
        this.sumOfMoney = sumOfMoney;
        this.setFinalized(finalized);
    }
    
    /**
     * Getter for the order number.
     * @return orderNumber
     */
    public long getOrderNumber() {
        return this.orderNumber;
    }
    
    /**
     * Setter for the order number.
     * @param value 
     */
    public final void setOrderNumber(long value) {
        long oldValue = this.orderNumber;
        this.orderNumber = value;
        this.propertySupport.firePropertyChange(PROP_ORDER_NUMBER, oldValue, this.orderNumber);
    }
    
    /**
     * Getter for the table.
     * @return 
     */
    public TableBean getTable() {
        return this.table;
    }
    
    /**
     * Setter for the table.
     * @param value 
     */
    public final void setTable(TableBean value) {
        TableBean oldValue = this.table;
        this.table = value;
        this.propertySupport.firePropertyChange(PROP_TABLE, oldValue, this.table);
    }
    
    /**
     * Getter for the sum.
     * @return sumOfMoney
     */
    public int getSumOfMoney() {
        return this.sumOfMoney;
    }
    
    /**
     * Setter for the sum.
     * @param sumOfMoney
     */
    public void setSumOfMoney(int sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }
    
    /**
     * Calculates the current sum. Incremently sums up the prices of all its order items. 
     */
    public void calculateSum() {
        int oldValue = this.sumOfMoney;
        this.sumOfMoney = 0;
        for (ItemBean i : orderItems)
            this.sumOfMoney += i.getPrice();
        this.propertySupport.firePropertyChange(PROP_SUM_OF_MONEY, oldValue, this.sumOfMoney);
    }
    
    /**
     * Getter for the order's "finalized" state.
     * @return finalized
     */
    public Boolean isFinalized() {
        return this.finalized;
    }
    
    /**
     * Setter for the order's state "finalized". Sets false by default.
     */
    public void setFinalized() {
        this.setFinalized(true);
    }
    
    /**
     * Setter for the order's state "finalized". Sets false by default.
     * @param value 
     */
    public final void setFinalized(boolean value) {
        boolean oldValue = this.finalized;
        this.finalized = value;
        this.propertySupport.firePropertyChange(PROP_FINALIZED, oldValue, this.finalized);
    }
    
    /**
     * Getter for the order items.
     * @return orderItems
     */
    public ArrayList<ItemBean> getOrderItems() {
        return this.orderItems;
    }
    
    /**
     * Setter for the order items.
     * @param items
     */
    public void setOrderItems(ArrayList<ItemBean> items) {
        this.orderItems = items;
    }
    
    /**
     * Getter for the waiter.
     * @return waiter
     */
    public WaiterBean getWaiter() {
        return this.waiter;
    }
    
    /**
     * Setter for the waiter.
     * @param value 
     */
    public final void setWaiter(WaiterBean value) {
        WaiterBean oldValue = this.waiter;
        this.waiter = value;
        this.propertySupport.firePropertyChange(PROP_WAITER, oldValue, this.waiter);
    }
    
    /**
     * 
     * @return OrderBean{orderNumber=[], tableNumber=[], sumOfMoney=[], finalized=[], oderItems=[amount], waiter=[name]}
     */
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
    
    /**
     * Tests another object whether it has the same properties.
     * @param obj
     * @return 
     */
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
    
    /**
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.orderNumber);
        hash = 67 * hash + Objects.hashCode(this.table);
        hash = 67 * hash + this.sumOfMoney;
        hash = 67 * hash + (this.finalized ? 1 : 0);
        hash = 67 * hash + Objects.hashCode(this.orderItems);
        hash = 67 * hash + Objects.hashCode(this.waiter);
        return hash;
    }
    
    /**
     * Add PropertyChangeListener.
     * @param listener 
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Remove PropertyChangeListener.
     * @param listener 
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * Class for Exceptions thrown when an order item could not be deleted.
     */
    public static class RemoveOrderItemException extends Exception {
        
        /**
         * ctor
         */
        public RemoveOrderItemException() {
        }

        /**
         * ctor
         * @param message 
         */
        public RemoveOrderItemException(String message) {
            super(message);
        }

        /**
         * ctor
         * @param message
         * @param cause 
         */
        public RemoveOrderItemException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * ctor
         * @param cause 
         */
        public RemoveOrderItemException(Throwable cause) {
            super(cause);
        }

        /**
         * ctor
         * @param message
         * @param cause
         * @param enableSuppression
         * @param writableStackTrace 
         */
        public RemoveOrderItemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
    
}
