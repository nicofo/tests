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
public class EntradaComment {
    private String userId;
    private String crumbId;
    private String comment;
    private String tokenId;
    private int page;
    private int pageSize;
    private boolean ascending;

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
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
    public String getCrumbId() {
        return crumbId;
    }

    /**
     *
     * @param crumbId
     */
    public void setCrumbId(String crumbId) {
        this.crumbId = crumbId;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

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
    
    
}
