package com.crumbits.Info;

import java.util.Date;

/**
 *
 * @author Miquel Ferriol
 */
public class CommentInfo extends Info{

    private String comment;
    private Date date;
    private long epochDate;
    private String userId;
    private String crumbId;
    private String userName;

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
    public String getUserId() {
        return userId;
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
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @param crumbId
     */
    public void setCrumbId(String crumbId) {
        this.crumbId = crumbId;
    }
    
    
}
