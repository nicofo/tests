package com.crumbits.Info;

import com.google.maps.model.*;

/**
 *
 * @author Miquel Ferriol
 */
public class PlaceInfo extends Info{

    private String name;
    private LatLng coordinate;
    private String type;
    private int nreCrumbs;
    private int nreUsersFollowing;
    private boolean isFollowing;
    private FileInfo placeFile;
    private String googleId;

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    
    /**
     *
     * @return
     */
    public FileInfo getPlaceFile() {
        return placeFile;
    }

    /**
     *
     * @param placeFile
     */
    public void setPlaceFile(FileInfo placeFile) {
        this.placeFile = placeFile;
    }

    /**
     *
     * @return
     */
    public int getNreCrumbs() {
        return nreCrumbs;
    }

    /**
     *
     * @param nreCrumbs
     */
    public void setNreCrumbs(int nreCrumbs) {
        this.nreCrumbs = nreCrumbs;
    }

    /**
     *
     * @return
     */
    public int getNreUsersFollowing() {
        return nreUsersFollowing;
    }

    /**
     *
     * @param nreUsersFollowing
     */
    public void setNreUsersFollowing(int nreUsersFollowing) {
        this.nreUsersFollowing = nreUsersFollowing;
    }

    /**
     *
     * @return
     */
    public boolean isIsFollowing() {
        return isFollowing;
    }

    /**
     *
     * @param isFollowing
     */
    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public LatLng getCoordinate() {
        return coordinate;
    }

    /**
     *
     * @param coordinate
     */
    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
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
