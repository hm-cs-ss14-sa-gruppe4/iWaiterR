package iwaiter.model;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Roman Baschmakov
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
     * 
     * @return name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 
     * @param value 
     */
    public final void setName(String value) {
        String oldValue = this.name;
        this.name = value;
        this.propertySupport.firePropertyChange(PROP_NAME, oldValue, this.name);
    }
    
    /**
     * 
     * @return orders
     */
    public final ArrayList<OrderBean> getOrders() {
        return this.orders;
    }
    
    /**
     * 
     * @param order 
     */
    public void addOrder(OrderBean order) {
        this.orders.add(order);
    }
    
    /**
     * 
     * @param order
     * @throws iwaiter.model.WaiterBean.RemoveOrderException 
     */
    public void removeOrder(OrderBean order) throws RemoveOrderException {
        if (!orders.remove(order))
            throw new RemoveOrderException();
    }
    
    /**
     * 
     * @param oldOrder
     * @param newOrder 
     */
    public void setOrder(OrderBean oldOrder, OrderBean newOrder) {
        for(OrderBean i : this.orders)
            if (i.equals(oldOrder))
                i = newOrder;
    }
    
    @Override
    public String toString() {
        return "WaiterBean{" + 
                "name=" + this.name + 
                ", orders=" + this.orders.size() + 
                '}';
    }
    
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
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.orders);
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
    public static class RemoveOrderException extends Exception {

        public RemoveOrderException() {
        }

        public RemoveOrderException(String message) {
            super(message);
        }

        public RemoveOrderException(String message, Throwable cause) {
            super(message, cause);
        }

        public RemoveOrderException(Throwable cause) {
            super(cause);
        }

        public RemoveOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }

    }
    
}
