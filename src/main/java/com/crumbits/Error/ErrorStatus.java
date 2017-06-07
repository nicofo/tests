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
public class ErrorStatus {
    private String status = "error";
    private ErrorStruct error = new ErrorStruct();

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

    /**
     *
     * @return
     */
    public ErrorStruct getError() {
        return error;
    }

    /**
     *
     * @param error
     */
    public void setError(ErrorStruct error) {
        this.error = error;
    }
    
    
}
