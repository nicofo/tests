
package com.crumbits.DB;

import static com.crumbits.DB.Queries.asyncDatastore;
import java.util.ArrayList;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Miquel Ferriol
 */
public class ActivityQuery extends Queries{
    
    /**
     *
     * @param text
     * @param date
     * @param userId
     * @param type
     * @param CrumbId
     * @throws EntityNotFoundException
     */
    public void insertNotificationCrumb(String text, Date date, String type, String crumbId, String userId) throws EntityNotFoundException{
        Key userKey = KeyFactory.stringToKey(userId);
        Entity n = new Entity("Notification");
        n.setProperty("text", text);
        Date now = Calendar.getInstance().getTime();
        n.setProperty("date", now);
        n.setProperty("dateTime", now.getTime());
        n.setProperty("userId", userKey);
        n.setProperty("id", KeyFactory.stringToKey(crumbId));
        n.setProperty("type", type);
        Key notifiKey = datastore.put(n);
        
        Entity user =  datastore.get(userKey);
        ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
        if(pNotifi == null) pNotifi = new ArrayList<>();
        pNotifi.add(notifiKey);
        user.setProperty("pendentNotification", pNotifi);
        ArrayList<Key> ownNotifi = (ArrayList<Key>)user.getProperty("ownNotification");
        if(ownNotifi == null) ownNotifi = new ArrayList<>();
        ownNotifi.add(notifiKey);
        user.setProperty("ownNotification", ownNotifi);
        datastore.put(user);
    }
    
    /**
     *
     * @param text
     * @param date
     * @param userId
     * @param type
     * @param CrumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void insertNotificationCrumbAsync(String text, Date date, String userId, String type, String CrumbId) throws EntityNotFoundException, InterruptedException, ExecutionException{
        
        Key userKey = KeyFactory.stringToKey(userId);
        Entity n = new Entity("Notification");
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Future<Entity> feUser =  asyncDatastore.get(userKey);
        
        n.setProperty("text", text);
        n.setProperty("date", date);
        n.setProperty("dateTime", date.getTime());
        n.setProperty("userId", userKey);
        n.setProperty("id", CrumbId);
        n.setProperty("type", type);
        Future<Key> feNotifiKey = asyncDatastore.put(n);
        
        Entity user = feUser.get();
        Key notifiKey = feNotifiKey.get();
        
        ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
        if(pNotifi == null) pNotifi = new ArrayList<>();
        pNotifi.add(notifiKey);
        user.setProperty("pendentNotification", pNotifi);
        ArrayList<Key> ownNotifi = (ArrayList<Key>)user.getProperty("ownNotification");
        if(ownNotifi == null) ownNotifi = new ArrayList<>();
        ownNotifi.add(notifiKey);
        user.setProperty("ownNotification", ownNotifi);
        
        asyncDatastore.put(user);
       
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @param text
     * @param date
     * @param type
     * @param Id
     * @throws EntityNotFoundException
     */
    public void insertNotificationFollow(String text, Date date, String type, String id) throws EntityNotFoundException{
        Entity n = new Entity("Notification");
        n.setProperty("text", text);
        Date now = Calendar.getInstance().getTime();
        n.setProperty("date", now);
        n.setProperty("dateTime", now.getTime());
        n.setProperty("id", KeyFactory.stringToKey(id));
        n.setProperty("type", type);
        
        Entity e = getById(id);
        ArrayList<Key> usersFollowing = (ArrayList<Key>)e.getProperty("followed");
        n.setProperty("userId", usersFollowing);
        Key notifiKey = datastore.put(n);
        if(usersFollowing != null){
            for(int i = 0; i < usersFollowing.size(); ++i){
                Entity user =  datastore.get(usersFollowing.get(i));
                ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
                if(pNotifi == null) pNotifi = new ArrayList<>();
                pNotifi.add(notifiKey);
                user.setProperty("pendentNotification", pNotifi);
                ArrayList<Key> ownNotifi = (ArrayList<Key>)user.getProperty("followNotification");
                if(ownNotifi == null) ownNotifi = new ArrayList<>();
                ownNotifi.add(notifiKey);
                user.setProperty("followNotification", ownNotifi);
                datastore.put(user);
            }
        }
    }
    
    /**
     *
     * @param text
     * @param date
     * @param type
     * @param Id
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void insertNotificationFollowAsync(String text, Date date, String type, String id) throws EntityNotFoundException, InterruptedException, ExecutionException{
        Entity n = new Entity("Notification");
        
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        n.setProperty("text", text);
        n.setProperty("date", date);
        n.setProperty("dateTime", date.getTime());
        n.setProperty("id", KeyFactory.stringToKey(id));
        n.setProperty("type", type);
        Future<Key> feNotifiKey = asyncDatastore.put(n);
        
        Future<Entity> fee = getByIdAsync(id);
        
        Key notifiKey = feNotifiKey.get();
        Entity e = fee.get();
        
        ArrayList<Key> usersFollowing = (ArrayList<Key>)e.getProperty("followNotification");
        
        if(usersFollowing != null){
            for(int i = 0; i < usersFollowing.size(); ++i){
                Entity user =  asyncDatastore.get(usersFollowing.get(i)).get();
                ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
                if(pNotifi == null) pNotifi = new ArrayList<>();
                pNotifi.add(notifiKey);
                user.setProperty("pendentNotification", pNotifi);
                ArrayList<Key> ownNotifi = (ArrayList<Key>)user.getProperty("followNotification");
                if(ownNotifi == null) ownNotifi = new ArrayList<>();
                ownNotifi.add(notifiKey);
                user.setProperty("followNotification", ownNotifi);
                asyncDatastore.put(user);
            }
        }
        
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @param notificationId
     * @throws EntityNotFoundException
     */
    public void markAsRead(String notificationId) throws EntityNotFoundException{
        
        
        Key nKey = KeyFactory.stringToKey(notificationId);
        Entity n = getById(nKey);
        Key userKey = (Key) n.getProperty("userId");
        
        Entity user = getById(userKey);
        ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
        pNotifi.remove(nKey);
        user.setProperty("pendentNotification", pNotifi);
        
        ArrayList<Key> notifi = (ArrayList<Key>)user.getProperty("notification");
        if(notifi == null) notifi = new ArrayList<>();
        notifi.add(nKey);
        user.setProperty("notification", notifi);
        
        datastore.put(n);
        datastore.put(user);
        
    }
    
    /**
     *
     * @param notificationId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void markAsReadAsync(String notificationId) throws EntityNotFoundException, InterruptedException, ExecutionException{
        Key nKey = KeyFactory.stringToKey(notificationId);
        
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity n = getByIdAsync(nKey).get();
        
        Key userKey = (Key) n.getProperty("userId");
        Entity user = getByIdAsync(userKey).get();
        
        ArrayList<Key> pNotifi = (ArrayList<Key>)user.getProperty("pendentNotification");
        pNotifi.remove(nKey);
        user.setProperty("pendentNotification", pNotifi);
        
        ArrayList<Key> notifi = (ArrayList<Key>)user.getProperty("notification");
        if(notifi == null) notifi = new ArrayList<>();
        notifi.add(nKey);
        user.setProperty("notification", notifi);
        
        asyncDatastore.put(n);
        asyncDatastore.put(user);
        
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @param userId
     * @throws EntityNotFoundException
     */
    public void markAllAsRead(String userId) throws EntityNotFoundException{
        Entity u = getById(userId);
        
        ArrayList<Key> pNotifi = (ArrayList<Key>) u.getProperty("notification");
        ArrayList<Key> notifi = (ArrayList<Key>) u.getProperty("pendentNotification");
        if(notifi == null)notifi = new ArrayList<>();
        notifi.addAll(pNotifi);
        pNotifi.clear();
        u.setProperty("notification", notifi);
        u.setProperty("pendentNotification", pNotifi);
        
        datastore.put(u);
    }
    
    /**
     *
     * @param userId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void markAllAsReadAsync(String userId) throws EntityNotFoundException, InterruptedException, ExecutionException{
        Entity u = getByIdAsync(userId).get();
        
        ArrayList<Key> pNotifi = (ArrayList<Key>) u.getProperty("notification");
        ArrayList<Key> notifi = (ArrayList<Key>) u.getProperty("pendentNotification");
        if(notifi == null)notifi = new ArrayList<>();
        notifi.addAll(pNotifi);
        pNotifi.clear();
        u.setProperty("notification", notifi);
        u.setProperty("pendentNotification", pNotifi);
        
        asyncDatastore.put(u).get();
    }    
    
    /**
     *
     * @param userId
     * @return
     * @throws EntityNotFoundException
     */
    public int pendentNotificationQuery(String userId) throws EntityNotFoundException{
        Entity u = getById(userId);
        int nrePendNoti = ((ArrayList<Key>)u.getProperty("pendentNotification")).size();
        return nrePendNoti;
    }
    
    /**
     *
     * @param userId
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public int pendentNotificationQueryAsync(String userId) throws EntityNotFoundException, InterruptedException, ExecutionException{
        Entity u = getByIdAsync(userId).get();
        int nrePendNoti = ((ArrayList<Key>)u.getProperty("pendentNotification")).size();
        return nrePendNoti;
    }
    
    public PreparedQuery getAllNotifications(){
        Query q = new Query("Notification");
        return datastore.prepare(q);
    }
    
    public void removeNotification(String notificationId) throws EntityNotFoundException{
        
        Entity result = getById(notificationId);
        String type = (String) result.getProperty("type");
        if(type.equals("crumb")){
            Key user =  (Key) result.getProperty("userId");
            Entity userE = getById(user);

            ArrayList<Key> followNotification =  (ArrayList<Key>) userE.getProperty("followNotification");
            ArrayList<Key> ownNotification =  (ArrayList<Key>) userE.getProperty("ownNotification");
            ArrayList<Key> pendentNotification =  (ArrayList<Key>) userE.getProperty("pendentNotification");

            if(followNotification!=null) followNotification.remove(result.getKey());
            if(ownNotification!=null) ownNotification.remove(result.getKey());
            if(pendentNotification!=null) pendentNotification.remove(result.getKey());

            userE.setProperty("followNotification", followNotification);
            userE.setProperty("ownNotification", ownNotification);
            userE.setProperty("pendentNotification", pendentNotification);

            datastore.put(userE);
        }
        if(type.equals("theme")){
            ArrayList<Key> users = (ArrayList<Key>) result.getProperty("userId");
            for(int i = 0; i < users.size(); ++i){

            Entity userE = getById(users.get(i));

            ArrayList<Key> followNotification =  (ArrayList<Key>) userE.getProperty("followNotification");
            ArrayList<Key> ownNotification =  (ArrayList<Key>) userE.getProperty("ownNotification");
            ArrayList<Key> pendentNotification =  (ArrayList<Key>) userE.getProperty("pendentNotification");

            if(followNotification!=null) followNotification.remove(result.getKey());
            if(ownNotification!=null) ownNotification.remove(result.getKey());
            if(pendentNotification!=null) pendentNotification.remove(result.getKey());

            userE.setProperty("followNotification", followNotification);
            userE.setProperty("ownNotification", ownNotification);
            userE.setProperty("pendentNotification", pendentNotification);

            datastore.put(userE);

            }
        }
        delete(result.getKey());
    }
    
}


