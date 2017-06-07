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
public class ErrorStruct {
    private String errorTitle;
    private String errorMessage;
    private Integer errorCode;

    /**
     *
     * @return
     */
    public String getErrorTitle() {
        return errorTitle;
    }

    /**
     *
     * @param errorTitle
     */
    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    /**
     *
     * @return
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     *
     * @return
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param errorCode
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
    
    
}
