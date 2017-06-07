package com.crumbits.Info;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Miquel Ferriol
 */
public class CrumbInfo extends Info{
    
    private String description;
    private ArrayList<ThemeInfo> Themes;
    private PlaceInfo Place;
    private Date date;
    private ArrayList<FileInfo> crumbFile;
    private int relevance;
    private int nreShares;
    private int nreThanks;
    private int nreReports;
    private int nreComments;
    private int nreViews;
    private long epochDate;
    private boolean isUserShare;
    private boolean isUserThanks;
    private FileInfo thumbnail;
    private boolean isOwner;

    public boolean isIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public FileInfo getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(FileInfo thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     *
     * @return
     */
    public long getEpochDate() {
        return epochDate;
    }

    /**
     *
     * @param epochDate
     */
    public void setEpochDate(long epochDate) {
        this.epochDate = epochDate;
    }

    /**
     *
     * @return
     */
    public boolean isIsUserShare() {
        return isUserShare;
    }

    /**
     *
     * @param isUserShare
     */
    public void setIsUserShare(boolean isUserShare) {
        this.isUserShare = isUserShare;
    }

    /**
     *
     * @return
     */
    public boolean isIsUserThanks() {
        return isUserThanks;
    }

    /**
     *
     * @param isUserThanks
     */
    public void setIsUserThanks(boolean isUserThanks) {
        this.isUserThanks = isUserThanks;
    }
    
    /**
     *
     * @return
     */
    public int getNreViews() {
        return nreViews;
    }

    /**
     *
     * @param nreViews
     */
    public void setNreViews(int nreViews) {
        this.nreViews = nreViews;
    }

    /**
     *
     * @return
     */
    public PlaceInfo getPlace() {
        return Place;
    }

    /**
     *
     * @param Place
     */
    public void setPlace(PlaceInfo Place) {
        this.Place = Place;
    }

    /**
     *
     * @return
     */
    public int getNreComments() {
        return nreComments;
    }

    /**
     *
     * @param nreComments
     */
    public void setNreComments(int nreComments) {
        this.nreComments = nreComments;
    }

    /**
     *
     * @return
     */
    public ArrayList<ThemeInfo> getThemes() {
        return Themes;
    }

    /**
     *
     * @param themes
     */
    public void setThemes(ArrayList<ThemeInfo> themes) {
        Themes = themes;
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
    public ArrayList<FileInfo> getCrumbFile() {
        return crumbFile;
    }

    /**
     *
     * @param crumbFile
     */
    public void setCrumbFile(ArrayList<FileInfo> crumbFile) {
        this.crumbFile = crumbFile;
    }

    /**
     *
     * @return
     */
    /*public LatLng getCoordinate() {
        return coordinate;
    }*/

    /**
     *
     * @param coordinate
     */
    /*public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }*/

    /**
     *
     * @return
     */
    public int getRelevance() {
        return relevance;
    }

    /**
     *
     * @param relevance
     */
    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    /**
     *
     * @return
     */
    public int getNreShares() {
        return nreShares;
    }

    /**
     *
     * @param nreShares
     */
    public void setNreShares(int nreShares) {
        this.nreShares = nreShares;
    }

    /**
     *
     * @return
     */
    public int getNreThanks() {
        return nreThanks;
    }

    /**
     *
     * @param nreThanks
     */
    public void setNreThanks(int nreThanks) {
        this.nreThanks = nreThanks;
    }

    /**
     *
     * @return
     */
    public int getNreReports() {
        return nreReports;
    }

    /**
     *
     * @param nreReports
     */
    public void setNreReports(int nreReports) {
        this.nreReports = nreReports;
    }

    /**
     *
     */
    public void incNreShares() {
        ++nreShares;
    }

    /**
     *
     */
    public void decNreShares() {
        --nreShares;
    }

    /**
     *
     */
    public void incNreThanks() {
        ++nreThanks;
    }

    /**
     *
     */
    public void decNreThanks() {
        --nreThanks;
    }

    /**
     *
     */
    public void incNreReports() {
        ++nreReports;
    }

    /**
     *
     */
    public void decNreReports() {
        --nreReports;
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
    
}
