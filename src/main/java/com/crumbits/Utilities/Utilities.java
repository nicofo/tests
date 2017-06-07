package com.crumbits.Utilities;

import com.crumbits.Info.CrumbInfo;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import java.util.ArrayList;
import java.util.Date;
import com.crumbits.DB.Queries;
import com.crumbits.Error.ErrorStatus;
import com.crumbits.Error.ErrorStruct;
import com.crumbits.Info.CommentInfo;
import com.crumbits.Info.FileInfo;
import com.crumbits.Info.NotificationInfo;
import com.crumbits.Info.PlaceInfo;
import com.crumbits.Info.ThemeInfo;
import com.crumbits.Info.UserInfo;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.lang.Exception;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Miquel Ferriol
 */
public class Utilities {

    Queries DB = new Queries();
    private final int TOKEN_TIME = 60*24;
    private final String KEY = "01000000d08c9ddf0115d1118c7a00c04fc297eb010000001a114d45b8dd3f4aa11ad7c0abdae9800000000002000000000003660000a8000000100000005df63cea84bfb7d70bd6842e7efa79820000000004800000a000000010000000f10cd0f4a99a8d5814d94e0687d7430b100000008bf11f1960158405b2779613e9352c6d14000000e6b7bf46a9d485ff211b9b2a2df3bd\n" +
"6eb67aae41";
    
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    
    
    private final String bucket = "crumbit";
    private final String thumbnailBucket = "crumbit/thumbnails";
    
    /**
     *
     * @param c
     * @param userId
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public CrumbInfo entityToCrumb(Entity c, String userId) throws EntityNotFoundException, IOException {
        CrumbInfo crumb = new CrumbInfo();
        
        ArrayList<Key> keyPlace = (ArrayList<Key>)c.getProperty("place");
        if(keyPlace != null){
            crumb.setPlace(entityToPlace(DB.getById(keyPlace.get(0)),userId,0));
        }
        else{
            Key placeId = (Key)c.getProperty("placeId");
            crumb.setPlace(entityToPlace(DB.getById(placeId),userId,0));
        }
        
        ArrayList<Key> keyTheme = (ArrayList<Key>) c.getProperty("theme");
        if(keyTheme != null){
            ArrayList<ThemeInfo> nameTheme = new ArrayList<>();
            Utilities u = new Utilities();
            for (int i = 0; i < keyTheme.size(); ++i) {
                nameTheme.add(u.entityToTheme(DB.getById(keyTheme.get(i)),userId,0));
            }
            crumb.setThemes(nameTheme);
        }

        crumb.setDescription((String) c.getProperty("description"));
        crumb.setDate((Date) c.getProperty("date"));
        crumb.setEpochDate(((Date) c.getProperty("date")).getTime()/1000);
        ArrayList<Key> crumbFile = (ArrayList<Key>) c.getProperty("file");
        ArrayList<FileInfo> filesInfo = new ArrayList<>();
        
        if(crumbFile != null){
            for(int i = 0; i < crumbFile.size(); ++i){
                Entity file = DB.getById(crumbFile.get(i));
                if((boolean)file.getProperty("isVideo")){
                    FileInfo fileInfo = new FileInfo();
                    
                    fileInfo.setBucket((String) file.getProperty("bucket"));
                    fileInfo.setFileId((String) file.getProperty("storageId"));
                    fileInfo.setIsVideo(true);
                    fileInfo.setAspectRatio((Double) file.getProperty("aspectRatio"));
                    filesInfo.add(fileInfo);
                    
                    Key thumbnailKey = (Key) file.getProperty("thumbnail");
                    
                    Entity thumbnail = DB.getById(thumbnailKey);
                    FileInfo tumbnailInfo = new FileInfo();
                    tumbnailInfo.setBucket((String) thumbnail.getProperty("bucket"));
                    tumbnailInfo.setFileId((String) thumbnail.getProperty("storageId"));
                    tumbnailInfo.setFileUrl((String) thumbnail.getProperty("fileUrl"));
                    tumbnailInfo.setIsVideo(false);
                    crumb.setThumbnail(tumbnailInfo);
                }
                else{
                    FileInfo fileInfo = new FileInfo();
                    
                    fileInfo.setBucket((String) file.getProperty("bucket"));
                    fileInfo.setFileId((String) file.getProperty("storageId"));
                    fileInfo.setFileUrl((String) file.getProperty("fileUrl"));
                    fileInfo.setIsVideo(false);
                    filesInfo.add(fileInfo);
                    
                }
            }
            crumb.setCrumbFile(filesInfo);
        }
        
        crumb.setRelevance(Integer.parseInt(c.getProperty("relevance").toString()));
        crumb.setNreReports(Integer.parseInt(c.getProperty("nreReports").toString()));
        crumb.setNreThanks(Integer.parseInt(c.getProperty("nreThanks").toString()));
        crumb.setNreShares(Integer.parseInt(c.getProperty("nreShares").toString()));
        
        //A cambiar quan els crumbs stigin ben fets.
        Object i = c.getProperty("nreComments");
        int aux;
        if(i == null) aux = 0;
        else aux = Integer.parseInt(i.toString());
        crumb.setNreComments(aux);
        
        Object i1 = c.getProperty("nreViews");
        int aux1;
        if(i1 == null) aux1 = 0;
        else aux1 = Integer.parseInt(i1.toString());
        crumb.setNreViews(aux1);
        
        crumb.setId(KeyFactory.keyToString(c.getKey()));
        
        if(userId != null){
            ArrayList<Key> userThanks = (ArrayList<Key>)c.getProperty("thanked");
            ArrayList<Key> userShare = (ArrayList<Key>)c.getProperty("shared");
            Key userKey = KeyFactory.stringToKey(userId);
            Key creatorKey = (Key) c.getProperty("created");
            crumb.setIsOwner(userKey.equals(creatorKey));
            if(userThanks != null){
                crumb.setIsUserThanks(userThanks.contains(KeyFactory.stringToKey(userId)));
            }
            else crumb.setIsUserThanks(false);
            
            if(userShare != null){
                crumb.setIsUserShare(userShare.contains(KeyFactory.stringToKey(userId)));
            }
            else crumb.setIsUserShare(false);
        }
        return crumb;
    }
    
    /**
     *
     * @param c
     * @param userId
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public CrumbInfo entityToCrumbAsync(Entity c, String userId) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {
        CrumbInfo crumb = new CrumbInfo();
        
        ArrayList<Key> keyPlace = (ArrayList<Key>)c.getProperty("place");
        if(keyPlace != null){
            crumb.setPlace(entityToPlace(DB.getByIdAsync(keyPlace.get(0)).get(),userId,0));
        }
        else{
            Key placeId = (Key)c.getProperty("placeId");
            crumb.setPlace(entityToPlace(DB.getByIdAsync(placeId).get(),userId,0));
        }
        
        ArrayList<Key> keyTheme = (ArrayList<Key>) c.getProperty("theme");
        if(keyTheme != null){
            ArrayList<ThemeInfo> nameTheme = new ArrayList<>();
            Utilities u = new Utilities();
            for (int i = 0; i < keyTheme.size(); ++i) {
                nameTheme.add(u.entityToTheme(DB.getByIdAsync(keyTheme.get(i)).get(),userId,0));
            }
            crumb.setThemes(nameTheme);
        }

        crumb.setDescription((String) c.getProperty("description"));
        crumb.setDate((Date) c.getProperty("date"));
        crumb.setEpochDate(((Date) c.getProperty("date")).getTime()/1000);
        ArrayList<String> crumbFile = (ArrayList<String>) c.getProperty("crumbFile");
        ArrayList<FileInfo> filesInfo = new ArrayList<>();
        
        
        if(crumbFile != null){
            GcsFilename fileName;
            for(int i = 0; i < crumbFile.size(); ++i){
                try{
                    fileName = new GcsFilename(bucket,crumbFile.get(i));
                    filesInfo.add(new FileInfo(bucket,crumbFile.get(i),null));
                }
                catch(Exception e){
                    filesInfo.add(new FileInfo(bucket,crumbFile.get(i),"ALGO ESTA FALLANT"));
                }
            }
        }
        
        crumb.setCrumbFile(filesInfo);
        crumb.setRelevance(Integer.parseInt(c.getProperty("relevance").toString()));
        crumb.setNreReports(Integer.parseInt(c.getProperty("nreReports").toString()));
        crumb.setNreThanks(Integer.parseInt(c.getProperty("nreThanks").toString()));
        crumb.setNreShares(Integer.parseInt(c.getProperty("nreShares").toString()));
        
        //A cambiar quan els crumbs stigin ben fets.
        Object i = c.getProperty("nreComments");
        int aux;
        if(i == null) aux = 0;
        else aux = Integer.parseInt(i.toString());
        crumb.setNreComments(aux);
        
        Object i1 = c.getProperty("nreViews");
        int aux1;
        if(i1 == null) aux1 = 0;
        else aux1 = Integer.parseInt(i1.toString());
        crumb.setNreViews(aux1);
        
        
        crumb.setId(KeyFactory.keyToString(c.getKey()));
        
        if(userId != null){
            ArrayList<Key> userThanks = (ArrayList<Key>)c.getProperty("thanked");
            ArrayList<Key> userShare = (ArrayList<Key>)c.getProperty("shared");
            if(userThanks != null){
                crumb.setIsUserThanks(userThanks.contains(KeyFactory.stringToKey(userId)));
            }
            else crumb.setIsUserThanks(false);
            
            if(userShare != null){
                crumb.setIsUserShare(userShare.contains(KeyFactory.stringToKey(userId)));
            }
            else crumb.setIsUserShare(false);
        }
        
        return crumb;

    }
    
    /**
     *
     * @param e
     * @return
     * @throws EntityNotFoundException
     */
    public CommentInfo entityToComment(Entity e) throws EntityNotFoundException {
        CommentInfo comment = new CommentInfo();

        comment.setComment((String) e.getProperty("comment"));
        comment.setDate((Date) e.getProperty("date"));
        comment.setEpochDate(((Date) e.getProperty("date")).getTime()/1000);
        Entity user = DB.getById((Key) e.getProperty("user"));
        comment.setUserId(KeyFactory.keyToString((Key)user.getKey()));
        comment.setUserName((String)user.getProperty("userName"));
        comment.setId(KeyFactory.keyToString(e.getKey()));
        return comment;
    }
    
    /**
     *
     * @param e
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public CommentInfo entityToCommentAsync(Entity e) throws EntityNotFoundException, InterruptedException, ExecutionException {
        CommentInfo comment = new CommentInfo();

        comment.setComment((String) e.getProperty("comment"));
        comment.setDate((Date) e.getProperty("date"));
        comment.setEpochDate(((Date) e.getProperty("date")).getTime()/1000);
        Entity user = DB.getByIdAsync((Key) e.getProperty("user")).get();
        comment.setUserId(KeyFactory.keyToString((Key)user.getKey()));
        comment.setUserName((String)user.getProperty("userName"));
        comment.setId(KeyFactory.keyToString(e.getKey()));
        return comment;
    }
    
    /**
     *
     * @param e
     * @return
     */
    public NotificationInfo entityToNotification(Entity e) throws EntityNotFoundException{
        NotificationInfo ni = new NotificationInfo();
        ni.setDate((Date)e.getProperty("date"));
        ni.setEpochDate(((Date) e.getProperty("date")).getTime()/1000);
        ni.setText((String)e.getProperty("text"));
        ni.setRelatedId(KeyFactory.keyToString((Key) e.getProperty("id")));
        ni.setType((String)e.getProperty("type"));
        ni.setId(KeyFactory.keyToString(e.getKey()));
        String type = (String)e.getProperty("type");
        switch (type) {
            case "crumb":
                Entity crumb = DB.getById((Key) e.getProperty("id"));
                String frame = (String) crumb.getProperty("frame");
                if(frame == null){
                    ArrayList<String> files = (ArrayList<String>) crumb.getProperty("crumbFile");
                    if(files != null){
                        ni.setUrl("http://storage.googleapis.com/"+bucket+"/" + files.get(0));
                    }
                }
                else{
                    ni.setUrl("http://storage.googleapis.com/"+bucket+"/" + frame);
                }   
                ni.setName((String) crumb.getProperty("description"));
                break;
            case "place":
            {
                Entity place = DB.getById((Key) e.getProperty("id"));
                ni.setName((String) place.getProperty("name"));
                ArrayList<Key> crumbKeys = (ArrayList<Key>) place.getProperty("crumbs");
                if(crumbKeys != null){
                    Entity c = DB.getById(crumbKeys.get(0));
                    ArrayList<String> files = (ArrayList<String>)c.getProperty("crumbFile");
                    if(files != null){
                        ni.setUrl("http://storage.googleapis.com/"+bucket+"/"+files.get(0));
                    }
            }       break;
                }
            case "theme":
            {
                Entity theme = DB.getById((Key) e.getProperty("id"));
                ni.setName((String) theme.getProperty("name"));
                ArrayList<Key> crumbKeys = (ArrayList<Key>) theme.getProperty("crumbs");
                if(crumbKeys != null){
                    Entity c = DB.getById(crumbKeys.get(0));
                    ArrayList<String> files = (ArrayList<String>)c.getProperty("crumbFile");
                    if(files != null){
                        ni.setUrl("http://storage.googleapis.com/"+bucket+"/"+files.get(0));
                    }
            }       break;
                }
        }
        return ni;
    }
    
    /**
     *
     * @param e
     * @return
     */
    public NotificationInfo entityToNotificationAsync(Entity e){
        NotificationInfo ni = new NotificationInfo();
        ni.setDate((Date)e.getProperty("date"));
        ni.setEpochDate(((Date) e.getProperty("date")).getTime()/1000);
        ni.setText((String)e.getProperty("text"));
        //ni.setUserId(KeyFactory.keyToString((Key)e.getProperty("userId")));
        ni.setType((String)e.getProperty("type "));
        ni.setId(KeyFactory.keyToString(e.getKey()));
        return ni;
    }
    
    /**
     *
     * @param e
     * @return
     * @throws EntityNotFoundException
     */
    public UserInfo entityToUser(Entity e) throws EntityNotFoundException{
        UserInfo ui = new UserInfo();
        
        ui.setUserName((String)e.getProperty("userName"));
        ui.setMail((String)e.getProperty("userMail"));
        ui.setUserId(KeyFactory.keyToString(e.getKey()));
        ui.setId(KeyFactory.keyToString(e.getKey()));
        //Nre crumbs
        ArrayList<Key> keys = (ArrayList<Key>)e.getProperty("created");
        if(keys == null){
            ui.setNreCrumbs(0);
            ui.setNreThanks(0);
        }
        else{
            ui.setNreCrumbs(keys.size());
            int nre = 0;
            //Nre thanks
            for(int i = 0; i < keys.size(); ++i){
                nre += Integer.parseInt(DB.getById(keys.get(i)).getProperty("nreThanks").toString());
            }
            ui.setNreThanks(nre);
        }
        
        
        //Nre crumbs thanked        
        keys = (ArrayList<Key>)e.getProperty("thanks");
        if(keys == null) ui.setNreCrumbsThanked(0);
        else ui.setNreCrumbsThanked(keys.size());
        
        //Nre places
        keys = (ArrayList<Key>)e.getProperty("followedPlaces");
        if(keys == null) ui.setNrePlaces(0);
        else ui.setNrePlaces(keys.size());
        
        //Nre themes
        keys = (ArrayList<Key>)e.getProperty("followedThemes");
        if(keys == null) ui.setNreThemes(0);
        else ui.setNreThemes(keys.size());
        
        
        return ui;
    }
    
    /**
     *
     * @param e
     * @return
     * @throws EntityNotFoundException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public UserInfo entityToUserAsync(Entity e) throws EntityNotFoundException, ExecutionException, InterruptedException{
        UserInfo ui = new UserInfo();
        
        ui.setUserName((String)e.getProperty("userName"));
        ui.setMail((String)e.getProperty("userMail"));
        ui.setUserId(KeyFactory.keyToString(e.getKey()));
        ui.setId(KeyFactory.keyToString(e.getKey()));
        //Nre crumbs
        ArrayList<Key> keys = (ArrayList<Key>)e.getProperty("created");
        if(keys == null){
            ui.setNreCrumbs(0);
            ui.setNreThanks(0);
        }
        else{
            ui.setNreCrumbs(keys.size());
            int nre = 0;
            //Nre thanks
            for(int i = 0; i < keys.size(); ++i){
                nre += Integer.parseInt(DB.getByIdAsync(keys.get(i)).get().getProperty("nreThanks").toString());
            }
            ui.setNreThanks(nre);
        }
        
        
        //Nre crumbs thanked        
        keys = (ArrayList<Key>)e.getProperty("thanks");
        if(keys == null) ui.setNreCrumbsThanked(0);
        else ui.setNreCrumbsThanked(keys.size());
        
        //Nre places
        keys = (ArrayList<Key>)e.getProperty("followedPlaces");
        if(keys == null) ui.setNrePlaces(0);
        else ui.setNrePlaces(keys.size());
        
        //Nre themes
        keys = (ArrayList<Key>)e.getProperty("followedThemes");
        if(keys == null) ui.setNreThemes(0);
        else ui.setNreThemes(keys.size());
        
        
        return ui;
    }
    
    /**
     *
     * @param e
     * @param userId
     * @param op
     * @return
     * @throws EntityNotFoundException
     */
    public ThemeInfo entityToTheme(Entity e, String userId, int op) throws EntityNotFoundException {
        ThemeInfo ti = new ThemeInfo();

        ti.setName((String) e.getProperty("name"));
        ti.setId(KeyFactory.keyToString(e.getKey()));
        
        ArrayList<Key> userKeys = (ArrayList<Key>)e.getProperty("followed");
        if(userKeys != null){
            ti.setNreUsersFollowing(userKeys.size());
            if(userId != null) ti.setIsFollowing(userKeys.contains(KeyFactory.stringToKey(userId)));
        }
        else{
            ti.setNreUsersFollowing(0);
            ti.setIsFollowing(false);
        }
        ArrayList<Key> crumbKeys = (ArrayList<Key>)e.getProperty("crumbs");
        if(crumbKeys != null){
            ti.setNreCrumbs(crumbKeys.size());
        }
        else{
            ti.setNreCrumbs(0);
        }
        
        if(op == 1){
            if(crumbKeys != null){
                Entity c = DB.getById(crumbKeys.get(0));
                ArrayList<Key> keyFiles = (ArrayList<Key>) c.getProperty("file");
                if(keyFiles!= null){
                    Entity file = DB.getById(keyFiles.get(0));
                    Key thumbnail = (Key) file.getProperty("thumbnail");
                    FileInfo fi = new FileInfo();
                    if(thumbnail != null){
                        Entity thumbnailFile = DB.getById(thumbnail);
                        fi.setFileId((String) thumbnailFile.getProperty("storageId"));
                        fi.setFileUrl((String) thumbnailFile.getProperty("fileUrl"));
                        fi.setBucket((String) thumbnailFile.getProperty("bucket"));
                    }
                    else{
                        fi.setFileId((String) file.getProperty("storageId"));
                        fi.setFileUrl((String) file.getProperty("fileUrl"));
                        fi.setBucket((String) file.getProperty("bucket"));
                    }
                    ti.setThemeFile(fi);                
                }
            }
        }
        return ti;
    }
    
    /**
     *
     * @param e
     * @param userId
     * @param op
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public ThemeInfo entityToThemeAsync(Entity e, String userId, int op) throws EntityNotFoundException, InterruptedException, ExecutionException {
        ThemeInfo ti = new ThemeInfo();

        ti.setName((String) e.getProperty("name"));
        ti.setId(KeyFactory.keyToString(e.getKey()));
        
        ArrayList<Key> userKeys = (ArrayList<Key>)e.getProperty("followed");
        if(userKeys != null){
            ti.setNreUsersFollowing(userKeys.size());
            if(userId != null) ti.setIsFollowing(userKeys.contains(KeyFactory.stringToKey(userId)));
        }
        else{
            ti.setNreUsersFollowing(0);
            ti.setIsFollowing(false);
        }
        ArrayList<Key> crumbKeys = (ArrayList<Key>)e.getProperty("crumbs");
        if(crumbKeys != null){
            ti.setNreCrumbs(crumbKeys.size());
        }
        else{
            ti.setNreCrumbs(0);
        }
        
        if(op == 1){
            if(crumbKeys != null){
                Entity c = DB.getByIdAsync(crumbKeys.get(0)).get();
                ArrayList<String> files = (ArrayList<String>)c.getProperty("crumbFile");
                if(files != null){
                    ti.setThemeFile(new FileInfo(bucket,files.get(0),null));
                }
            }
        }
        return ti;
    }
    
    /**
     *
     * @param e
     * @param userId
     * @param op
     * @return
     * @throws EntityNotFoundException
     */
    public PlaceInfo entityToPlace(Entity e, String userId, int op) throws EntityNotFoundException {
        PlaceInfo p = new PlaceInfo();

        p.setName((String) e.getProperty("name"));
        p.setCoordinate(new LatLng((double)e.getProperty("lat"),(double)e.getProperty("lng")));
        p.setId(KeyFactory.keyToString(e.getKey()));
        p.setGoogleId((String) e.getProperty("googlePlaceId"));
        ArrayList<Key> userKeys = (ArrayList<Key>)e.getProperty("followed");
        if(userKeys != null){
            p.setNreUsersFollowing(userKeys.size());
            if(userId != null) p.setIsFollowing(userKeys.contains(KeyFactory.stringToKey(userId)));
        }
        else{
            p.setNreUsersFollowing(0);
            p.setIsFollowing(false);
        }
        ArrayList<Key> crumbKeys = (ArrayList<Key>)e.getProperty("crumbs");
        if(crumbKeys != null){
            p.setNreCrumbs(crumbKeys.size());
        }
        else{
            p.setNreCrumbs(0);
        }
        if(op == 1){
            if(crumbKeys != null){
                Entity c = DB.getById(crumbKeys.get(0));
                ArrayList<Key> keyFiles = (ArrayList<Key>) c.getProperty("file");
                if(keyFiles!= null){
                    Entity file = DB.getById(keyFiles.get(0));
                    Key thumbnail = (Key) file.getProperty("thumbnail");
                    FileInfo fi = new FileInfo();
                    if(thumbnail != null){
                        Entity thumbnailFile = DB.getById(thumbnail);
                        fi.setFileId((String) thumbnailFile.getProperty("storageId"));
                        fi.setFileUrl((String) thumbnailFile.getProperty("fileUrl"));
                        fi.setBucket((String) thumbnailFile.getProperty("bucket"));
                    }
                    else{
                        fi.setFileId((String) file.getProperty("storageId"));
                        fi.setFileUrl((String) file.getProperty("fileUrl"));
                        fi.setBucket((String) file.getProperty("bucket"));
                    }
                    p.setPlaceFile(fi);                
                }
            }
            if(crumbKeys != null){
                Entity c = DB.getById(crumbKeys.get(0));
                ArrayList<String> files = (ArrayList<String>)c.getProperty("crumbFile");
                if(files != null){
                    p.setPlaceFile(new FileInfo(bucket,files.get(0),null));
                }
            }
        }
        return p;
    }
    
    /**
     *
     * @param e
     * @param userId
     * @param op
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public PlaceInfo entityToPlaceAsync(Entity e, String userId, int op) throws EntityNotFoundException, InterruptedException, ExecutionException {
        PlaceInfo p = new PlaceInfo();

        p.setName((String) e.getProperty("name"));
        p.setCoordinate(new LatLng((double)e.getProperty("lat"),(double)e.getProperty("lng")));
        p.setId(KeyFactory.keyToString(e.getKey()));
        
        ArrayList<Key> userKeys = (ArrayList<Key>)e.getProperty("followed");
        if(userKeys != null){
            p.setNreUsersFollowing(userKeys.size());
            if(userId != null) p.setIsFollowing(userKeys.contains(KeyFactory.stringToKey(userId)));
        }
        else{
            p.setNreUsersFollowing(0);
            p.setIsFollowing(false);
        }
        ArrayList<Key> crumbKeys = (ArrayList<Key>)e.getProperty("crumbs");
        if(crumbKeys != null){
            p.setNreCrumbs(crumbKeys.size());
        }
        else{
            p.setNreCrumbs(0);
        }
        if(op == 1){
            if(crumbKeys != null){
                Entity c = DB.getByIdAsync(crumbKeys.get(0)).get();
                ArrayList<String> files = (ArrayList<String>)c.getProperty("crumbFile");
                if(files != null){
                    p.setPlaceFile(new FileInfo(bucket,files.get(0),null));
                }
            }
        }
        return p;
    }
    
    private int binarySearch(String[] array, String value, int left, int right) {

      if (left > right)

            return -1;

      int middle = (left + right) / 2;

      if (array[middle].startsWith(value))
            return middle;

      else if (array[middle].compareTo(value) > 0)
            return binarySearch(array, value, left, middle - 1);
      else
            return binarySearch(array, value, middle + 1, right);           
      
    }
    
    /**
     *
     * @param search
     * @param searchId
     * @return
     */
    public ArrayList<Integer> search(ArrayList<String> search, String searchId){
        String[] array = search.toArray(new String[search.size()]);
        ArrayList<Integer> id = new ArrayList<>();
        int pointer = binarySearch(array,searchId,0,search.size());
        if(pointer < 0) return null;
        id.add(pointer);
        boolean found = false;
        int cont = pointer-1;
        while(!found && cont >= 0){
            if((search.get(cont)).startsWith(searchId)) id.add(cont);
            else found = true;
            --cont;
        }
        cont = pointer+1;
        found = false;
        while(!found && cont < search.size()){
            if((search.get(cont)).startsWith(searchId)) id.add(cont);
            else found = true;
            ++cont;
        }
        return id;
    }
    
    /**
     *
     * @param authConsumerSecret
     * @param date
     * @return
     */
    public String generateToken(String authConsumerSecret, Date date){
        
        return Jwts.builder().setSubject(authConsumerSecret).setExpiration(date).signWith(SignatureAlgorithm.HS512, KEY).compact();
 
    }
    
    /**
     *
     * @param oldToken
     * @return
     */
    public String decodeToken(String token){
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().getSubject();
    }
    
    /**
     *
     * @param token
     * @return
     */
    public boolean verifyToken(String token){
        Date date = Calendar.getInstance().getTime();
        return Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().getExpiration().after(date);
    }
    
    /**
     *
     * @param errorTitle
     * @param errorMessage
     * @param errorCode
     * @return
     */
    public ErrorStatus createError(String errorTitle,String errorMessage,int errorCode){
        ErrorStatus error = new ErrorStatus();
        ErrorStruct e = new ErrorStruct();
        error.setStatus("error");
        e.setErrorCode(errorCode);
        e.setErrorMessage(errorMessage);
        e.setErrorTitle(errorTitle);
        error.setError(e);
        
        return error;
    }
    
    /**
     *
     * @param orig
     * @param dest
     * @return
     * @throws Exception
     */
    public long distance(LatLng orig, LatLng dest) throws Exception {
        GeoApiContext _API_CONTEXT_ = new GeoApiContext().setApiKey("AIzaSyC4AiXoS1AfElLxpqIpNUMLwo-7AAGI3SQ");
        DistanceMatrixApiRequest d = com.google.maps.DistanceMatrixApi.getDistanceMatrix(_API_CONTEXT_, new String[1], new String[1]);
        d.destinations(orig);
        d.origins(dest);
        DistanceMatrix dm = d.await();
        Distance dis = dm.rows[0].elements[0].distance;

        return (dis.inMeters);
    }
    
    private String convertByteToHex(byte data[])
    {
        StringBuffer hexData = new StringBuffer();
        for (int byteIndex = 0; byteIndex < data.length; byteIndex++)
            hexData.append(Integer.toString((data[byteIndex] & 0xff) + 0x100, 16).substring(1));
        
        return hexData.toString();
    }
    
    /**
     *
     * @param pass
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String sha_512(String pass) throws NoSuchAlgorithmException{
        MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
        sha512.update(pass.getBytes());
        return convertByteToHex(sha512.digest());
    }
}
