package iwaiter.model;

import java.beans.*;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
public class ItemBean implements Serializable {
    
    private long id;
    
    private String name;
    public static final String PROP_NAME = "name";
    
    private int price;
    public static final String PROP_PRICE = "price";
    
    private PropertyChangeSupport propertySupport; // why does NetBeans want it to be final?
    
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
    public ItemBean(long id, String name, int price) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setId(id);
        this.setName(name);
        this.setPrice(price);
    }
    
    /**
     * ctor
     * @param item 
     */
    public ItemBean(ItemBean item) {
        this.propertySupport = new PropertyChangeSupport(this);
        this.setId(item.getId());
        this.setName(item.getName());
        this.setPrice(item.getPrice());
    }

    /**
     * Getter for the name.
     * @return 
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the id.
     * @param id 
     */
    public void setId(long id) {
        this.id = id;
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
     * Getter for the price.
     * @return price
     */
    public int getPrice() {
        return this.price;
    }
    
    /**
     * Setter for the price.
     * @param value 
     */
    public final void setPrice(int value) {
        long oldValue = this.price;
        this.price = value;
        this.propertySupport.firePropertyChange(PROP_PRICE, oldValue, this.price);
    }
    
    /**
     * 
     * @return ItemBean{name=[name], price=[price]}
     */
    @Override
    public String toString() {
        return "ItemBean{" + 
                "name=" + this.name + 
                ", price=" + this.price + 
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
        final ItemBean other = (ItemBean) obj;
        return this.name.equals(other.getName())
                && this.price == other.price;
    }
    
    /**
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.price;
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
    
}
