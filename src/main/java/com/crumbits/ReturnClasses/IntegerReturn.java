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
public class IntegerReturn {
    private Object notifications;

    /**
     *
     * @param notifications
     */
    public IntegerReturn(Object notifications) {
        this.notifications = notifications;
    }

    /**
     *
     * @return
     */
    public Object getNotifications() {
        return notifications;
    }

    /**
     *
     * @param notifications
     */
    public void setNotifications(Object notifications) {
        this.notifications = notifications;
    }
}
