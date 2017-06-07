/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Entrada;

/**
 *
 * @author Miquel Ferriol
 */
public class EntradaActivity {
    private String userId;
    private String notificationId;
    private int page;
    private int pageSize;
    private String text;
    private String type;
    private String id;
    private String tokenId;

    /**
     *
     * @return
     */
    public String getToken() {
        return tokenId;
    }

    /**
     *
     * @param token
     */
    public void setToken(String token) {
        this.tokenId = token;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     *
     * @param notificationId
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     */
    public int getPage() {
        return page;
    }

    /**
     *
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     *
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
}
