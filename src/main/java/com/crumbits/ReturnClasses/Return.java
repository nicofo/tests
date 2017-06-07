/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.ReturnClasses;

import com.crumbits.Error.ErrorStatus;

/**
 *
 * @author Miquel Ferriol
 */
public class Return {
    ErrorStatus status;
    Object ret;

    /**
     *
     * @return
     */
    public ErrorStatus getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(ErrorStatus status) {
        this.status = status;
    }

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
    
    
}
