/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Entrada;

import java.util.ArrayList;

/**
 *
 * @author Miquel Ferriol
 */
public class EntradaSocial {
    private float lat;
    private float lng;
    private String searchId;
    private String tokenId;
    private int page;
    private int pageSize;
    private String themeId;
    private String userId;
    private String themeName;
    private ArrayList<String> themesNames;

    /**
     *
     * @return
     */
    public ArrayList<String> getThemesNames() {
        return themesNames;
    }

    /**
     *
     * @param themesNames
     */
    public void setThemesNames(ArrayList<String> themesNames) {
        this.themesNames = themesNames;
    }

    /**
     *
     * @return
     */
    public String getThemeName() {
        return themeName;
    }

    /**
     *
     * @param themeName
     */
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    /**
     *
     * @return
     */
    public float getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     */
    public void setLat(float lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     */
    public float getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     */
    public void setLng(float lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     */
    public String getSearchId() {
        return searchId;
    }

    /**
     *
     * @param searchId
     */
    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    /**
     *
     * @return
     */
    public String getTokenId() {
        return tokenId;
    }

    /**
     *
     * @param tokenId
     */
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
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
    public String getThemeId() {
        return themeId;
    }

    /**
     *
     * @param themeId
     */
    public void setThemeId(String themeId) {
        this.themeId = themeId;
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
    
    
}
