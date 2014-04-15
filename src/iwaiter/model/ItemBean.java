package iwaiter.model;

import java.beans.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public class ItemBean implements Serializable {
    
    private String name;
    public static final String PROP_NAME = "name";
    
    private int price;
    public static final String PROP_PRICE = "price";
    
    private PropertyChangeSupport propertySupport;
    
    /**
     * ctor
     */
    public ItemBean() {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setName("new item");
        this.setPrice(0);
    }
    
    /**
     * ctor
     * @param name
     * @param price 
     */
    public ItemBean(String name, int price) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setName(name);
        this.setPrice(price);
    }
    
    /**
     * ctor
     * @param item 
     */
    public ItemBean(ItemBean item) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setName(item.getName());
        this.setPrice(item.getPrice());
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
     * @return price
     */
    public int getPrice() {
        return this.price;
    }
    
    /**
     * 
     * @param value 
     */
    public final void setPrice(int value) {
        int oldValue = this.price;
        this.price = value;
        this.propertySupport.firePropertyChange(PROP_PRICE, oldValue, this.price);
    }
    
    @Override
    public String toString() {
        return "ItemBean{" + 
                "name=" + this.name + 
                ", price=" + this.price + 
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;
        final ItemBean other = (ItemBean) obj;
        return this.name.equals(other.getName())
                && this.price != other.price;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.price;
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
    
}
