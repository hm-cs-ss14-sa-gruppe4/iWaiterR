/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import iwaiter.entity.AvailableItemEntity;
import iwaiter.entity.OrderEntity;
import iwaiter.entity.OrderItemEntity;
import iwaiter.entity.TableEntity;
import iwaiter.entity.WaiterEntity;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public abstract class IWaiterController {
    
    private Stage parentStage;
    private Scene previousScene;
    private Object correspondent;
    
    private boolean actionConfirmed;

    /**
     * Set a pointer to the parentStage stage.
     * @param stage 
     */
    public void setParentStage(Stage stage) {
        this.parentStage = stage;
    }
    
    /**
     * Get the pointer to the parentStage stage.
     * @return The parentStage Stage.
     */
    public Stage getParentStage() {
        return this.parentStage;
    }
    
    /**
     * Get the pointer to the previousScene Scene.
     * @return The previousScene Pane.
     */
    public Scene getPreviousScene() {
        return previousScene;
    }
    
    /**
     * Set a pointer to the previousScene Scene.
     * @param scene 
     */
    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    
    /**
     * Get the pointer to the correspondent class to send information to.
     * @return 
     */
    public Object getCorrespondent() {
        return correspondent;
    }
    
    /**
     * Set a pointer to the correspondent class to send information to.
     * @param correspondent 
     */
    public void setCorrespondent(Object correspondent) {
        this.correspondent = correspondent;
    }

    /**
     * Checks whether an action was confirmed to be performed.
     * @return 
     */
    public boolean isActionConfirmed() {
        return actionConfirmed;
    }

    /**
     * Resets the actionConfirmed flag. 
     */
    public void resetActionConfirmed() {
        this.actionConfirmed = false;
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
    *
    * @author Roman Baschmakov, Viktor Magdych
    * @version 1.1
    */
    public class PersistenceManager {
        
        private final EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("iWaiterR_PU");
        
        /**
         * Creates a new entity in the database
         * @param object
         */
        public void create(Object object) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
            em.close();
        }
        
        /**
         * Updates a given entity in the database
         * @param object
         */
        public void update(Object object) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            em.merge(object);
            em.getTransaction().commit();
            em.close();
        }
        
        /**
         * Deletes a given entity in the database
         * @param c
         * @param id
         */
        public void delete(Class c, Long id) {
            EntityManager em = emf.createEntityManager();
            em.getTransaction().begin();
            Object object = em.getReference(c, id);
            em.remove(object);
            em.getTransaction().commit();
            em.clear();
            em.close();
        }
        
        /**
         * Gives you all entities from a table in the database
         * @param c Entity Class
         * @return 
         */
        public List<Object> allEntries(Class c) {
            EntityManager em = emf.createEntityManager();
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery(c);
            Root<Object> rootEntry = cq.from(c);
            CriteriaQuery<Object> all = cq.select(rootEntry);
            TypedQuery<Object> allQuery = em.createQuery(all);
            //try {
                return allQuery.getResultList();
            //} catch (Exception e) {
            //    return new ArrayList<>();
            //} finally {
            //    em.close();
            //}
        }
        
        
        /**
         * Searches for a waiter entity in the database by name
         * @param name
         * @return 
         */
        public WaiterEntity findWaiter(String name) {
            EntityManager em = emf.createEntityManager();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<WaiterEntity> cq = cb.createQuery(WaiterEntity.class);
                Root<WaiterEntity> root = cq.from(WaiterEntity.class);
                cq.where(cb.equal(root.get("name"), name));
                TypedQuery query = em.createQuery(cq);
                List<WaiterEntity> result = query.getResultList();
                return (result.size() > 0 ? result.get(0) : null);
            //} catch (Exception e) {
            //    return null;
            } finally {
                em.close();
            }
        }
        
        /**
         * Searches for a table entity in the database by table number
         * @param tableNumber
         * @return 
         */
        public TableEntity findTable(int tableNumber) {
            EntityManager em = emf.createEntityManager();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<TableEntity> cq = cb.createQuery(TableEntity.class);
                Root<TableEntity> root = cq.from(TableEntity.class);
                cq.where(cb.equal(root.get("tableNumber"), (Integer) tableNumber));
                TypedQuery query = em.createQuery(cq);
                List<TableEntity> result = query.getResultList();
                return (result.size() > 0 ? result.get(0) : null);
            } finally {
                em.close();
            }
        }
        
        /**
         * Searches for an order entity in the database by order number
         * @param orderNumber
         * @return 
         */
        public OrderEntity findOrder(long orderNumber) {
            EntityManager em = emf.createEntityManager();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
                Root<OrderEntity> root = cq.from(OrderEntity.class);
                cq.where(cb.equal(root.get("orderNumber"), orderNumber));
                TypedQuery query = em.createQuery(cq);
                List<OrderEntity> result = query.getResultList();
                return (result.size() > 0 ? result.get(0) : null);
            } finally {
                em.close();
            }
        }
        
        /**
         * Searches for a order item entity in the database by id
         * @param id
         * @return 
         */
        public OrderItemEntity findOrderItem(long id) {
            EntityManager em = emf.createEntityManager();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<OrderItemEntity> cq = cb.createQuery(OrderItemEntity.class);
                Root<OrderItemEntity> root = cq.from(OrderItemEntity.class);
                cq.where(cb.equal(root.get("id"), id));
                TypedQuery query = em.createQuery(cq);
                List<OrderItemEntity> result = query.getResultList();
                return (result.size() > 0 ? result.get(0) : null);
            } finally {
                em.close();
            }
        }
        
        
        /**
         * Searches for a orter item entity in the database by name
         * @param name
         * @return 
         */
        public AvailableItemEntity findAvailableItem(String name) {
            EntityManager em = emf.createEntityManager();
            try {
                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<AvailableItemEntity> cq = cb.createQuery(AvailableItemEntity.class);
                Root<AvailableItemEntity> root = cq.from(AvailableItemEntity.class);
                cq.where(cb.equal(root.get("name"), name));
                TypedQuery query = em.createQuery(cq);
                List<AvailableItemEntity> result = query.getResultList();
                return (result.size() > 0 ? result.get(0) : null);
            } finally {
                em.close();
            }
        }
        
    }
}
