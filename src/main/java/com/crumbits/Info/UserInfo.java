package com.crumbits.Info;

/**
 *
 * @author Miquel Ferriol
 */
public class UserInfo extends Info{

    private String userName;
    private String mail;
    private String accesToken;
    private String userId;
    private int nrePlaces;
    private int nreThemes;
    private int nreCrumbs;
    private int nreCrumbsThanked;
    private int nreThanks;
    private long tokenExpirationTime;

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
    
    /**
     *
     * @return
     */
    public int getNrePlaces() {
        return nrePlaces;
    }

    /**
     *
     * @param nrePlaces
     */
    public void setNrePlaces(int nrePlaces) {
        this.nrePlaces = nrePlaces;
    }

    /**
     *
     * @return
     */
    public int getNreThemes() {
        return nreThemes;
    }

    /**
     *
     * @param nreThemes
     */
    public void setNreThemes(int nreThemes) {
        this.nreThemes = nreThemes;
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
    public int getNreCrumbsThanked() {
        return nreCrumbsThanked;
    }

    /**
     *
     * @param nreCrumbsThanked
     */
    public void setNreCrumbsThanked(int nreCrumbsThanked) {
        this.nreCrumbsThanked = nreCrumbsThanked;
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
    public String getAccesToken() {
        return accesToken;
    }

    /**
     *
     * @param accesToken
     */
    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }

    
    /**
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return
     */
    public String getMail() {
        return mail;
    }

    /**
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
    

}
