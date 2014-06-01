/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
public class TableBean implements Serializable {
    
    private int tableNumber;
    public static final String PROP_TABLE_NUMBER = "tableNumber";

    /**
     * ctor
     * @param tableNumber 
     */
    public TableBean(int tableNumber) {
        this.setTableNumber(tableNumber);
    }
    
    /**
     * Getter for the table number.
     *
     * @return the value of tableNumber
     */
    public int getTableNumber() {
        return tableNumber;
    }

    /**
     * Setter for the table number.
     *
     * @param tableNumber new value of tableNumber
     */
    public final void setTableNumber(int tableNumber) {
        int oldTableNumber = this.tableNumber;
        this.tableNumber = tableNumber;
        propertyChangeSupport.firePropertyChange(PROP_TABLE_NUMBER, oldTableNumber, tableNumber);
    }

    private transient final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * 
     * @return table number
     */
    @Override
    public String toString() {
        return String.valueOf(tableNumber);
    }

    /**
     * 
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.tableNumber;
        return hash;
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
        if (getClass() != obj.getClass())
            return false;
        final TableBean other = (TableBean) obj;
        return this.tableNumber == other.tableNumber;
    }

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
