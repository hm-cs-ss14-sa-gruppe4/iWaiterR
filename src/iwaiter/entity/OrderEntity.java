/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Roman Baschmakov, Viktor Magdych
 * @version 1.0
 */
@Entity
public class OrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderNumber;
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "table_id")
    private TableEntity table_;
    private int sumOfMoney = 0;
    private boolean finalized = false;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> orderItems = new ArrayList<>();
    @ManyToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "waiter_id")
    private WaiterEntity waiter;

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TableEntity getTable() {
        return table_;
    }

    public void setTable(TableEntity table) {
        this.table_ = table;
    }

    public int getSumOfMoney() {
        return sumOfMoney;
    }

    public void setSumOfMoney(int sumOfMoney) {
        this.sumOfMoney = sumOfMoney;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized() {
        this.finalized = true;
    }
    
    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public WaiterEntity getWaiter() {
        return waiter;
    }

    public void setWaiter(WaiterEntity waiter) {
        this.waiter = waiter;
    }

    @Override
    public String toString() {
        return "OrderEntity{" + "orderNumber=" + orderNumber + ", table_=" + table_ + ", sumOfMoney=" + sumOfMoney + ", finalized=" + finalized + ", orderItems:" + orderItems.size() + ", waiter=" + waiter + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.orderNumber ^ (this.orderNumber >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OrderEntity other = (OrderEntity) obj;
        if (this.orderNumber != other.orderNumber)
            return false;
        return true;
    }
    
    
    
    
}
