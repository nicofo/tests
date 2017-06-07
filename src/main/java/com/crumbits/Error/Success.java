/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Error;

/**
 *
 * @author Miquel Ferriol
 */
public class Success {
    private Object ret;
    private String status = "success";

    /**
     *
     * @return
     */
    public Object getRet() {
        return ret;
    }

    /**
     *
     * @param ret
     */
    public void setRet(Object ret) {
        this.ret = ret;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
