/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iwaiter.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Roman Baschmakov
 * @version 1.0
 */
public abstract class IWaiterController {
    
    private Stage parentStage;
    private Scene previousScene;
    private Object correspondent;

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
    
}
