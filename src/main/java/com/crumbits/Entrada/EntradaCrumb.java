/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Entrada;

import com.google.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Miquel Ferriol
 */
public class EntradaCrumb {
    private String creatorId;
    private String userId;
    private String crumbId;
    private String description;
    private Date date;
    private ArrayList<String> themes;
    private ArrayList<String> places;
    private String place;
    private ArrayList<String> fileId;
    private ArrayList<String> base64;
    private String tokenId;
    private float botLat;
    private float botLng;
    private float topLat;
    private float topLng;
    private ArrayList<String> themesId;
    private String placeId;
    private int page;
    private int pageSize;
    private String googlePlaceId;
    private float lat;
    private float lng;
    private String initDate;
    private String endDate;
    private LatLng userPosition;
    private int radious;
    private String type;
    private String socialNetwork;
    private String comments;

    public ArrayList<String> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<String> places) {
        this.places = places;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LatLng getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(LatLng userPosition) {
        this.userPosition = userPosition;
    }

    public int getRadious() {
        return radious;
    }

    public void setRadious(int radious) {
        this.radious = radious;
    }

    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    

    

    /**
     *
     * @return
     */
    public ArrayList<String> getBase64() {
        return base64;
    }

    /**
     *
     * @param base64
     */
    public void setBase64(ArrayList<String> base64) {
        this.base64 = base64;
    }
    
    /**
     *
     * @return
     */
    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    /**
     *
     * @param googlePlaceId
     */
    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
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
    public ArrayList<String> getThemesId() {
        return themesId;
    }

    /**
     *
     * @param themesId
     */
    public void setThemesId(ArrayList<String> themesId) {
        this.themesId = themesId;
    }

    /**
     *
     * @return
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     *
     * @param placeId
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public float getBotLat() {
        return botLat;
    }

    /**
     *
     * @param botLat
     */
    public void setBotLat(float botLat) {
        this.botLat = botLat;
    }

    /**
     *
     * @return
     */
    public float getBotLng() {
        return botLng;
    }

    /**
     *
     * @param botLng
     */
    public void setBotLng(float botLng) {
        this.botLng = botLng;
    }

    /**
     *
     * @return
     */
    public float getTopLat() {
        return topLat;
    }

    /**
     *
     * @param topLat
     */
    public void setTopLat(float topLat) {
        this.topLat = topLat;
    }

    /**
     *
     * @return
     */
    public float getTopLng() {
        return topLng;
    }

    /**
     *
     * @param topLng
     */
    public void setTopLng(float topLng) {
        this.topLng = topLng;
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
    public String getCreatorId() {
        return creatorId;
    }

    /**
     *
     * @param creatorId
     */
    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getThemes() {
        return themes;
    }

    /**
     *
     * @param themes
     */
    public void setThemes(ArrayList<String> themes) {
        this.themes = themes;
    }

    /**
     *
     * @return
     */
    public String getPlace() {
        return place;
    }

    /**
     *
     * @param place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getFileId() {
        return fileId;
    }

    /**
     *
     * @param fileId
     */
    public void setFileId(ArrayList<String> fileId) {
        this.fileId = fileId;
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
    
    
}
