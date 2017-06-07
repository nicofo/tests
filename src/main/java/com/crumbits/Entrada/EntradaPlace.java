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
public class EntradaPlace {
    private String place;
    private float lat;
    private float lng;
    private String searchId;
    private String tokenId;
    private int page;
    private int pageSize;
    private String placeId;
    private String userId;
    private ArrayList<String> placesNames;
    private ArrayList<Float> placesLat;
    private ArrayList<Float> placesLng;
    private ArrayList<String> googlePlaceIds;
    private String googlePlaceId;

    public ArrayList<String> getGooglePlaceIds() {
        return googlePlaceIds;
    }

    public void setGooglePlaceIds(ArrayList<String> googlePlaceIds) {
        this.googlePlaceIds = googlePlaceIds;
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
    public ArrayList<Float> getPlacesLat() {
        return placesLat;
    }

    /**
     *
     * @param placesLat
     */
    public void setPlacesLat(ArrayList<Float> placesLat) {
        this.placesLat = placesLat;
    }

    /**
     *
     * @return
     */
    public ArrayList<Float> getPlacesLng() {
        return placesLng;
    }

    /**
     *
     * @param placesLng
     */
    public void setPlacesLng(ArrayList<Float> placesLng) {
        this.placesLng = placesLng;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getPlacesNames() {
        return placesNames;
    }

    /**
     *
     * @param placesNames
     */
    public void setPlacesNames(ArrayList<String> placesNames) {
        this.placesNames = placesNames;
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
