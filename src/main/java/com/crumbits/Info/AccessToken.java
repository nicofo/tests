/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Info;


/**
 *
 * @author Ricard
 */
public class AccessToken {
    private String accessToken;
    private long tokenExpirationTime;

    /**
     *
     */
    public AccessToken() {
    }

    /**
     *
     * @return
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     *
     * @return
     */
    public long getTokenExpirationTime() {
        return tokenExpirationTime;
    }

    /**
     *
     * @param tokenExpirationTime
     */
    public void setTokenExpirationTime(long tokenExpirationTime) {
        this.tokenExpirationTime = tokenExpirationTime;
    }
    
    
    
    
}
