package com.crumbits;

import com.crumbits.Info.CommentInfo;
import com.crumbits.Info.CrumbInfo;
import java.io.File;
import java.util.ArrayList;

import com.crumbits.DB.CrumbQuery;
import com.crumbits.ReturnClasses.IsSharing;
import com.crumbits.ReturnClasses.IsThanking;
import com.crumbits.ReturnClasses.PaginationRet;
import com.crumbits.ReturnClasses.Url;
import com.crumbits.Utilities.Utilities;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Date;

/**
 *
 * @author Miquel Ferriol
 */
public class Crumb {

    CrumbQuery DB = new CrumbQuery();
    private String userKey;

    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Crumb(String token) throws IllegalAccessException {
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

    /**
     *
     * @param creatorId
     * @param description
     * @param themes
     * @param place
     * @param googlePlaceId
     * @param lat
     * @param lng
     * @param date
     * @param fileId
     * @throws EntityNotFoundException
     */
    public void createCrumb(String creatorId, String description, ArrayList<String> themes, String place, String googlePlaceId, double lat, double lng, Date date, ArrayList<String> fileId, String thumbnail, double aspectRatio, boolean sensitiveContent) throws EntityNotFoundException, IOException {
        DB.insertCrumb(creatorId, description, themes, place, googlePlaceId, lat, lng, date, fileId, thumbnail, aspectRatio, sensitiveContent);
        Activity a = new Activity();
        for(int i = 0; i < themes.size(); ++i){
            a.createNotificationFollow("Nuevo crumb!","Un nuevo crumb sobre el tema que sigues!", date, "theme", themes.get(i));
        }
        PreparedQuery pEntity = DB.getByGoogleId(googlePlaceId);
        a.createNotificationFollow("Nuevo crumb!","Un nuevo crumb con el place que sigues!", date, "place", KeyFactory.keyToString(pEntity.asSingleEntity().getKey()));
    }

    /**
     *
     * @param crumbId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public CrumbInfo getCrumbById(String crumbId) throws EntityNotFoundException, IOException {
        Entity e = DB.getById(crumbId);

        Utilities u = new Utilities();
        return u.entityToCrumb(e,userKey);
    }

    /**
     *
     * @param crumbId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public IsThanking getIsUserThanksCrumb(String crumbId) throws EntityNotFoundException {
        Entity user = DB.getById(userKey);
        ArrayList<Key> resultThank = new ArrayList<>();
        resultThank = (ArrayList<Key>) user.getProperty("thanks");
        if(resultThank == null) return new IsThanking(false);
        return new IsThanking(resultThank.contains(KeyFactory.stringToKey(crumbId)));
    }

    /**
     *
     * @param crumbId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public IsSharing getIsUserSharesCrumb(String crumbId) throws EntityNotFoundException {
        Entity user = DB.getById(userKey);
        ArrayList<Key> resultThank = (ArrayList<Key>) user.getProperty("share");
        if(resultThank == null) return new IsSharing(false);
        return new IsSharing(resultThank.contains(KeyFactory.stringToKey(crumbId)));
    }
    
    public void deleteCrumb(String crumbId) throws EntityNotFoundException {
        Key user = (Key)DB.getById(crumbId).getProperty("created");
        if(KeyFactory.keyToString(user).equals(userKey)){
            DB.deleteCrumb(crumbId);
        }
        else{
             throw new IllegalArgumentException("You are not the creator if this crumb");
        }
    }

    /**
     *
     * @param crumbId
     * @return
     * @throws EntityNotFoundException
     */
    public ArrayList<CommentInfo> getLastComments(String crumbId) throws EntityNotFoundException {
        ArrayList<CommentInfo> comments = new ArrayList<>();
        ArrayList<Key> commentKey = (ArrayList<Key>) DB.getById(crumbId).getProperty("comment");
        Utilities u = new Utilities();
        for (int i = commentKey.size() - 1; i > commentKey.size() - 4; --i) {
            Entity c = DB.getById(commentKey.get(i));
            comments.add(u.entityToComment(c));
        }

        return comments;
    }

    /**
     *
     * @param crumbId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public ArrayList<CrumbInfo> getRelatedCrumbs(String crumbId) throws EntityNotFoundException, IOException {
        ArrayList<CrumbInfo> relatedCrumb = new ArrayList<>();
        ArrayList<Entity> ae = DB.getRelatedCrumbs(crumbId);//Necesitem el filtre de popularitat
        Utilities u = new Utilities();
        
        for (int i = 0; i < ae.size(); ++i) {
            if(i < 5) {
                relatedCrumb.add(u.entityToCrumb(ae.get(i),userKey));
            }
        }
        return relatedCrumb;
    }

    /**
     *
     * @param userKey
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void thanksCrumb( String crumbId) throws EntityNotFoundException, IOException {

        DB.addUserThanks(userKey, crumbId);
        
        Activity a = new Activity();
        //String tittle, String text, Date date,  String type, String crumbId
        Date date = Calendar.getInstance().getTime();
        a.createNotificationCrumb("Thanks crumb!","Un usuario ha dado thanks a tu crumb!", date, "crumb", crumbId);
    }
    
    /**
     *
     * @param userKey
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void unthanksCrumb( String crumbId) throws EntityNotFoundException {

        DB.delUserThanks(userKey, crumbId);
        //Notification
    }

    /**
     *
     * @param userKey
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void shareCrumb( String crumbId, String type) throws EntityNotFoundException, IOException {

        DB.addUserShare(userKey, crumbId, type);
        Activity a = new Activity();
        //String tittle, String text, Date date,  String type, String crumbId
        Date date = Calendar.getInstance().getTime();
        a.createNotificationCrumb("Share crumb!","Un usuario ha compartido tu crumb!", date, "crumb", crumbId);
        //Notification
    }

    /**
     *
     * @param userKey
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void reportCrumb( String crumbId, String type, String comments) throws EntityNotFoundException {

        DB.addUserReport(userKey, crumbId, type, comments);
        //Notification
    }
    
    /**
     *
     * @param botLat
     * @param botLng
     * @param topLat
     * @param topLng
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public ArrayList<CrumbInfo> getCrumbsByPlace(float botLat, float botLng, float topLat, float topLng) throws EntityNotFoundException, IOException{
        ArrayList<Key> crumbKeys = DB.getCrumbsByPlace(botLat, botLng, topLat, topLng);
        ArrayList<CrumbInfo> crumbInfo = new ArrayList<>();
        Utilities u = new Utilities();
        for(int i = 0; i < crumbKeys.size(); ++i){
            crumbInfo.add(u.entityToCrumb(DB.getById(crumbKeys.get(i)),userKey));
        }
        return crumbInfo;
    }
    
    /**
     *
     * @param botLat
     * @param botLng
     * @param topLat
     * @param topLng
     * @param initDate
     * @param endDate
     * @param page
     * @param pageSize
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public PaginationRet getLocalCrumbs(float botLat, float botLng, float topLat, float topLng, String initDate, String endDate, int page, int pageSize) throws EntityNotFoundException, IOException {
        return DB.getLocalCrumbs(botLat, botLng, topLat, topLng, initDate, endDate, page, pageSize, userKey);
        
    }
    
    /**
     *
     * @param themesId
     * @param placeId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public PaginationRet combinedSearchCrumbs(String initDate, String endDate, ArrayList<String> themesId, ArrayList<String> places,  int page, int pageSize) throws EntityNotFoundException, IOException{
        return DB.combinedSearchCrumbs(initDate, endDate, themesId, places, userKey, page, pageSize, userKey);
    }
    
    
    public ArrayList<CrumbInfo> combinedSearch(ArrayList<String> themeIds, ArrayList<String> placeIds, String description, int page, int pageSize) throws EntityNotFoundException, IOException{
        ArrayList<CrumbInfo> crumbInfo = new ArrayList<>();
        List<Entity> le = DB.combinedSearch(themeIds, placeIds, description, page, pageSize);
        ListIterator<Entity> it = le.listIterator();
        Utilities u = new Utilities();
        while(it.hasNext()){
            crumbInfo.add(u.entityToCrumb(it.next(),userKey));
        }
        return crumbInfo;
    }
    
    /**
     *
     * @param page
     * @param pageSize
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public PaginationRet getLastCrumbs(float botLat, float botLng, float topLat, float topLng, String initDate, String endDate, int page, int pageSize) throws EntityNotFoundException, IOException{
        return DB.getLastCrumbs(botLat, botLng, topLat, topLng, initDate, endDate, page, pageSize, userKey);
        
    }
    
    /**
     *
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void addViewToCrumb(String crumbId) throws EntityNotFoundException{
        DB.addView(crumbId);
    }
    
    /**
     *
     * @param d1
     * @param d2
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public ArrayList<CrumbInfo> getCrumbsByDate(String d1, String d2) throws EntityNotFoundException, IOException{
        PreparedQuery pq = DB.getCrumbsByDate(d1,d2);
        ArrayList<Entity> crumbKeys = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            crumbKeys.add(result);
        }
        ArrayList<CrumbInfo> crumb = new ArrayList<>();
        Utilities u = new Utilities();
        for(int i = 0; i < 15; ++i ){
            if(i < crumbKeys.size()) crumb.add(u.entityToCrumb(crumbKeys.get(i),userKey));
        }
        return crumb;
    }
    
    public Object getEmbedUrl(String crumbId) throws EntityNotFoundException, IOException{
        Url url = new Url();
        url.setUrl(DB.getEmbedUrl(crumbId));
        return url;
    }
    
}
