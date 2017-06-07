package com.crumbits;

import java.util.ArrayList;
import java.util.Date;

import com.crumbits.DB.ActivityQuery;
import com.crumbits.Info.NotificationInfo;
import com.crumbits.ReturnClasses.PaginationRet;
import com.crumbits.Utilities.Utilities;
import com.crumbits.pushNotification.pushNotification;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import java.io.IOException;
import java.net.ProtocolException;

/**
 *
 * @author Miquel Ferriol
 */
public class Activity {

    ActivityQuery DB = new ActivityQuery();
    private String userKey;

    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Activity(String token) throws IllegalAccessException{
        Utilities u = new Utilities();
        try{
            if(!u.verifyToken(token)) throw new IllegalAccessException();
            else{
                userKey = u.decodeToken(token);
            }
        }
        catch(Exception e){
            throw new IllegalAccessException();
        }
    }
    
    public Activity(){
    }
    
    /**
     *
     * @param text
     * @param date
     * @param userKey
     * @param type
     * @param Id
     * @throws EntityNotFoundException
     */
    public void createNotificationCrumb(String tittle, String text, Date date, String type, String crumbId) throws EntityNotFoundException, ProtocolException, IOException{
        Entity crumb = DB.getById(crumbId);
        Key uKey = (Key) crumb.getProperty("created");
        DB.insertNotificationCrumb(text, date, type, crumbId, KeyFactory.keyToString(uKey));
        pushNotification pn = new pushNotification();
        Entity user = DB.getById(uKey);
        ArrayList<String> playerIds = (ArrayList<String>) user.getProperty("playerIds");
        if(playerIds != null) pn.sendPushNotification(playerIds, tittle, tittle, text, text);
    }
    
    /**
     *
     * @param text
     * @param date
     * @param type
     * @param Id
     * @throws EntityNotFoundException
     */
    public void createNotificationFollow(String tittle, String text, Date date, String type, String Id) throws EntityNotFoundException, ProtocolException, IOException{
        
        pushNotification pn = new pushNotification();
        Entity themePlace = DB.getById(Id);
        ArrayList<Key> usersFollowing = (ArrayList<Key>)themePlace.getProperty("followed");
        ArrayList<String> totalPlayerIds = new ArrayList<>();
        if(usersFollowing != null){
            for(int i = 0; i < usersFollowing.size(); ++i){
                Entity user = DB.getById(usersFollowing.get(i));
                DB.insertNotificationFollow(text, date, type, Id);
                ArrayList<String> playerIds = (ArrayList<String>)user.getProperty("playerIds");
                if(playerIds != null){
                    totalPlayerIds.addAll((ArrayList<String>)user.getProperty("playerIds"));
                }
            }
        }
        if(totalPlayerIds != null) pn.sendPushNotification(totalPlayerIds, tittle, tittle, text, text);
        
    }

    /**
     *
     * @param userKey
     * @param page
     * @param pageSize
     * @return
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public PaginationRet getUserOwnActivity( int page, int pageSize) throws EntityNotFoundException {
        
        Entity user = DB.getById(userKey);
        ArrayList<Key> keyNot = (ArrayList<Key>) user.getProperty("ownNotification");
        if(keyNot == null) keyNot = new ArrayList();
        Utilities u = new Utilities();
        
        ArrayList<NotificationInfo> not = new ArrayList();
        for(int i = page*pageSize; i < page*pageSize + pageSize && i < keyNot.size()-1; ++i){
            not.add(u.entityToNotification(DB.getById(keyNot.get(keyNot.size()-i-1))));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(not);
        pr.setLastPage(((int) Math.ceil(keyNot.size() / (double)pageSize)) - 1);
        return pr;
    }
    
    /**
     *
     * @param userKey
     * @param page
     * @param pageSize
     * @return
     * @throws EntityNotFoundException
     */
    public PaginationRet getUserFollowingActivity(int page, int pageSize) throws EntityNotFoundException {
        Entity user = DB.getById(userKey);
        ArrayList<Key> keyNot = (ArrayList<Key>) user.getProperty("followNotification");
        if(keyNot == null) keyNot = new ArrayList();
        Utilities u = new Utilities();
        
        ArrayList<NotificationInfo> not = new ArrayList();
        for(int i = page*pageSize; i < page*pageSize + pageSize && i < keyNot.size()-1; ++i){
            not.add(u.entityToNotification(DB.getById(keyNot.get(keyNot.size()-i-1))));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(not);
        pr.setLastPage(((int) Math.ceil(keyNot.size() / (double)pageSize)) - 1);
        return pr;
    }

    /**
     *
     * @param userKey
     * @return
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public int getNumberPendentNotiﬁcation() throws EntityNotFoundException {
        return DB.pendentNotificationQuery(userKey);
    }

    /**
     *
     * @param notificationId
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void markNotificationAsRead(String notificationId) throws EntityNotFoundException {

        DB.markAsRead(notificationId);

    }

    /**
     *
     * @param userKey
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void markAllNotificationsAsRead() throws EntityNotFoundException {
        DB.markAllAsRead(userKey);
    }

}
