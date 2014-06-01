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
public class WaiterBean implements Serializable {
    
    private String name;
    public static final String PROP_NAME = "name";
    
    private ArrayList<OrderBean> orders = new ArrayList<>();
    public static final String PROP_ORDERS = "orders";
    
    private PropertyChangeSupport propertySupport;
    
    /**
     * ctor
     * @param name 
     */
    public WaiterBean(String name) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setName(name);
    }
    
    /**
     * Getter for the name.
     * @return name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter for the name.
     * @param value 
     */
    public final void setName(String value) {
        String oldValue = this.name;
        this.name = value;
        this.propertySupport.firePropertyChange(PROP_NAME, oldValue, this.name);
    }
    
    /**
     * Getter for the orders.
     * @return orders
     */
    public ArrayList<OrderBean> getOrders() {
        return this.orders;
    }
    
    /**
     * Setter for the orders.
     * @param orders 
     */
    public void setOrders(ArrayList<OrderBean> orders) {
        this.orders = orders;
    }
    
    /**
     * 
     * @return WaiterBean{name=[], orders=[amount]}
     */
    @Override
    public String toString() {
        return "WaiterBean{" + 
                "name=" + this.name + 
                ", orders=" + this.orders.size() + 
                '}';
    }
    
    /**
     * Tests another object whether it has the same properties.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final WaiterBean other = (WaiterBean) obj;
        return this.name.equals(other.name) 
                && this.orders.equals(other.orders);
    }
    
    /**
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.orders);
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
     * Class for Exceptions thrown when an order could not be deleted.
     */
    public static class RemoveOrderException extends Exception {

        /**
         * ctor
         */
        public RemoveOrderException() {
        }

        /**
         * ctor
         * @param message 
         */
        public RemoveOrderException(String message) {
            super(message);
        }

        /**
         * ctor
         * @param message
         * @param cause 
         */
        public RemoveOrderException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * ctor
         * @param cause 
         */
        public RemoveOrderException(Throwable cause) {
            super(cause);
        }

        /**
         * ctor
         * @param message
         * @param cause
         * @param enableSuppression
         * @param writableStackTrace 
         */
        public RemoveOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
    
}
