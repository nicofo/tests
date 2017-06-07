package com.crumbits.Info;

import java.util.Date;

/**
 *
 * @author Miquel Ferriol
 */
public class NotificationInfo extends Info{
    
    private String text;
    private Date date;
    private long epochDate;
    private String userId;
    private String type;
    private String relatedId;
    private String url;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    public String getText() {
        return text;
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
     * @return
     */
    public String getType() {
        return type;
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
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    
    
    
}
