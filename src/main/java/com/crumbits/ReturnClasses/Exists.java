/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.ReturnClasses;

/**
 *
 * @author Miquel Ferriol Galmé
 */
public class Exists {
    private boolean exists;

    /**
     *
     */
    public Exists(){
        
    }
    
    /**
     *
     * @return
     */
    public boolean isExists() {
        return exists;
    }

    /**
     *
     * @param exists
     */
    public void setExists(boolean exists) {
        this.exists = exists;
    }

    /**
     *
     * @param exists
     */
    public Exists(boolean exists) {
        this.exists = exists;
    }
    
    
}
