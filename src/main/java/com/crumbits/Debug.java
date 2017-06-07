/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits;

import com.crumbits.DB.ActivityQuery;
import com.crumbits.DB.CrumbQuery;
import com.crumbits.DB.FileQuery;
import com.crumbits.DB.LoginQuery;
import com.crumbits.DB.PlaceQuery;
import com.crumbits.DB.SocialQuery;
import com.crumbits.Info.CrumbInfo;
import com.crumbits.Info.ThemeInfo;
import com.crumbits.Utilities.Utilities;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.GeoRegion.Rectangle;
import com.google.appengine.api.datastore.Query.StContainsFilter;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ImagesServiceFailureException;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Miquel Ferriol Galmé
 */
public class Debug {
    
    CrumbQuery DBC = new CrumbQuery();
    PlaceQuery DBP = new PlaceQuery();
    SocialQuery DBS = new SocialQuery();
    LoginQuery DBL = new LoginQuery();
    FileQuery DBF = new FileQuery();
    ActivityQuery DBA = new ActivityQuery();
    
    
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    
    private final String bucket = "crumbit";
    private final String thumbnailBucket = "crumbit/thumbnails";
    
    /**
     *
     * @return
     */
    public Object removeAllUnexsitentThemes(){
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> ret = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                ArrayList<Key> s = (ArrayList<Key>) result.getProperty("theme");
                if(s != null){
                    for(int i = 0; i < s.size(); ++i){
                        try{
                            DBC.getById(s.get(i));
                        }
                        catch(EntityNotFoundException e){
                            ret.add(KeyFactory.keyToString(s.get(i)));
                            s.remove(i);
                        }
                        catch(Exception e){
                            
                        }
                    }
                }
                result.setProperty("theme",s);
                DBC.putEntity(result);
            }
            catch(Exception e){
                
            }
        }
        return ret;
    }
    
    /**
     *
     * @return
     */
    public Object removeAllUnexsitentPlaces(){
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> ret = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                ArrayList<Key> s = (ArrayList<Key>) result.getProperty("place");
                if(s != null){
                    for(int i = 0; i < s.size(); ++i){
                        try{
                            DBC.getById(s.get(i));
                        }
                        catch(EntityNotFoundException e){
                            ret.add(KeyFactory.keyToString(s.get(i)));
                            s.remove(i);
                        }
                        catch(Exception e){
                            
                        }
                    }
                }
                result.setProperty("place",s);
                DBC.putEntity(result);
            }
            catch(Exception e){
                
            }
        }
        return ret;
    }
    
    public Object removeAllUnexistentCrumbs(){
        PreparedQuery pq = DBP.getAllPlaces();
        ArrayList<String> ret = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                ArrayList<Key> s = (ArrayList<Key>) result.getProperty("crumbs");
                if(s != null){
                    for(int i = 0; i < s.size(); ++i){
                        try{
                            DBP.getById(s.get(i));
                        }
                        catch(EntityNotFoundException e){
                            ret.add(KeyFactory.keyToString(s.get(i)));
                            s.remove(i);
                        }
                        catch(Exception e){
                            
                        }
                    }
                }
                result.setProperty("crumbs",s);
                DBC.putEntity(result);
            }
            catch(Exception e){
                
            }
        }
        return ret;
    }
    
    /**
     *
     * @return
     * @throws IOException
     */
    public Object removeAllUnusedFiles() throws IOException{
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> s = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                s.addAll((ArrayList<String>) result.getProperty("crumbFile"));
            }
            catch(Exception e){
                
            }
        }
        //AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();

        ArrayList<String> ret = new ArrayList<>();
        
        ListResult result = gcsService.list(bucket, ListOptions.DEFAULT);
        while (result.hasNext()){
            ListItem l = result.next();
            String name = l.getName();

            ret.add(name);
        }
        
        ret.removeAll(s);
        
        GcsFilename fileName;
        for(int i = 0; i < ret.size(); ++i){
            fileName = new GcsFilename(bucket,ret.get(i));
            
            gcsService.delete(fileName);
        }
        return ret;
    }
    
    /**
     *
     * @throws IOException
     */
    public void setAllFilesPublic() throws IOException{

        ArrayList<String> ret = new ArrayList<>();
        
        ListResult result = gcsService.list(bucket, ListOptions.DEFAULT);
        while (result.hasNext()){
            ListItem l = result.next();
            String name = l.getName();

            ret.add(name);
        }
        
        GcsFilename fileName;
        GcsFileOptions options;
        for(int i = 0; i < ret.size(); ++i){
            fileName = new GcsFilename(bucket,ret.get(i));
            options = new GcsFileOptions.Builder()
                .acl("public-read").build();
            gcsService.getMetadata(fileName).getOptions().getMimeType();
            gcsService.update(fileName, options);
        }
    }
    
    /**
     *
     * @throws IOException
     */
    public void placesToPlaceId() throws IOException{

        ArrayList<String> ret = new ArrayList<>();
        
        PreparedQuery pq = DBC.getAllCrumbs();
        for (Entity result : pq.asIterable()) {
            ArrayList<Key> placeKey = (ArrayList<Key>)result.getProperty("place");
            if(placeKey != null){
                result.setProperty("placeId", placeKey.get(0));
                DBC.put(result);
            }
        }
    }
    
    /**
     *
     * @param id
     * @throws IOException
     * @throws EntityNotFoundException
     */
    public void fileError(String id) throws IOException, EntityNotFoundException{

        ArrayList<String> ret = new ArrayList<>();
        
        Entity result = DBC.getById(id);
        String placeKey = (String)result.getProperty("crumbFile");
        ret.add(placeKey);
        if(placeKey != null){
            result.setProperty("crumbFile", ret);
            DBC.put(result);
        }
    }
    
    /**
     *
     * @throws IllegalAccessException
     * @throws EntityNotFoundException
     */
    public void createCrumbMultipleFiles() throws IllegalAccessException, EntityNotFoundException, IOException{
        Crumb c = new Crumb("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZzV6Zm1OeWRXMWlhWFF0TVRNd05ISVJDeElFVlhObGNoaUFnSUNBX2V5QkNndyIsImV4cCI6MTUwMTkwMTgxN30.x9RlaPOoaUfJ664kdqnZ5BXmYSQIpIWFqeHXp3enC2SxQZA3Bd-7Hx2g0IR8m6gk-U71CnvYgMdx909wZ70O7A");
        //createCrumb(String creatorId, String description, ArrayList<String> themes, String place, String googlePlaceId, double lat, double lng, Date date, ArrayList<String> fileId)
        ArrayList<String> themesId = new ArrayList<>();
        themesId.add("ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgIqnhgoM");
        ArrayList<String> filesId = new ArrayList<>();
        filesId.add("288914791466433764013.avi");
        
        Date d = new Date();
        c.createCrumb("ag5zfmNydW1iaXQtMTMwNHIRCxIEVXNlchiAgICA0eKMCgw", "Un crumb con video", themesId, "Mallorca", "ChIJS4r-4M-OpBIR1s6qOHO12a4", 39.695263, 3.017571, Calendar.getInstance().getTime(), filesId,null,0,false);
    }
    
    /**
     *
     * @throws IllegalAccessException
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public void fileNames() throws IllegalAccessException, EntityNotFoundException, IOException{
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> newFiles;
        ArrayList<String> oldFiles = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                oldFiles = ((ArrayList<String>) result.getProperty("crumbFile"));
                newFiles = new ArrayList<>();
                for(int i = 0; i < oldFiles.size(); ++i){
                    if(oldFiles.get(i).equals("288914791466433764013")){
                        newFiles.add(oldFiles.get(i) + ".avi");
                    }
                    else{
                        newFiles.add(oldFiles.get(i) + ".jpeg");
                    }
                }
                result.setProperty("crumbFile", newFiles);
                DBC.put(result);
            }
            catch(Exception e){
                
            }
        }
    }
    
    /**
     *
     * @throws IOException
     */
    public void fileTypes() throws IOException{
    ArrayList<String> ret = new ArrayList<>();
        
        ListResult result = gcsService.list(bucket, ListOptions.DEFAULT);
        
        while (result.hasNext()){
            ListItem l = result.next();
            String name = l.getName();

            ret.add(name);
        }
        
        GcsFilename oldFileName;
        GcsFilename newFileName;
        GcsFileOptions newOptions;
        for(int i = 0; i < ret.size(); ++i){
            try{
            if(!ret.get(i).endsWith("jpeg"))
                if(!ret.get(i).equals("288914791466433764013")){
                    oldFileName = new GcsFilename(bucket,ret.get(i));
                    newFileName = new GcsFilename(bucket,ret.get(i) + ".jpeg");
                    newOptions = new GcsFileOptions.Builder().mimeType("image/jpeg").build();
                    gcsService.copy(oldFileName, newFileName);
                    gcsService.createOrReplace(newFileName, newOptions);
                    gcsService.delete(oldFileName);
                }
                else{
                    oldFileName = new GcsFilename(bucket,ret.get(i));
                    newFileName = new GcsFilename(bucket,ret.get(i) + ".avi");
                    newOptions = new GcsFileOptions.Builder().mimeType("video/avi").build();
                    gcsService.copy(oldFileName, newFileName);
                    gcsService.createOrReplace(newFileName, newOptions);
                    gcsService.delete(oldFileName);
                }
            }
        catch(Exception e){
                
                }
        }
    }
    
    /**
     *
     * @throws IOException
     */
    public void setAllFilesFormat() throws IOException{

        ArrayList<String> ret = new ArrayList<>();
        
        ListResult result = gcsService.list(bucket, ListOptions.DEFAULT);
        while (result.hasNext()){
            ListItem l = result.next();
            String name = l.getName();

            ret.add(name);
        }
        
        GcsFilename fileName;
        GcsFileOptions options;
        for(int i = 0; i < ret.size(); ++i){
            fileName = new GcsFilename(bucket,ret.get(i));
            options = new GcsFileOptions.Builder().mimeType("image/jpeg").build();
            gcsService.update(fileName, options);
        }
    }
    
    /**
     *
     * @throws IOException
     */
    public void setPlacesData() throws IOException{

        PreparedQuery pq = DBP.getAllPlaces();
        ArrayList<Key> userKey;
        ArrayList<Key> crumbKey;
        for( Entity result : pq.asIterable()){
            userKey = (ArrayList<Key>)result.getProperty("followed");
            if(userKey != null){
                result.setProperty("nreFollows", userKey.size());
            }
            else{
                result.setProperty("nreFollows", 0);
            }
            crumbKey = (ArrayList<Key>)result.getProperty("crumbs");
            if(crumbKey != null){
                result.setProperty("nreCrumbs", crumbKey.size());
            }
            else{
                result.setProperty("nreCrumbs", 0);
            }
            DBP.put(result);
        }
    }
    
    /**
     *
     * @throws IOException
     */
    public void setThemesData() throws IOException{

        PreparedQuery pq = DBS.getAllThemes();
        ArrayList<Key> userKey;
        ArrayList<Key> crumbKey;
        for( Entity result : pq.asIterable()){
            userKey = (ArrayList<Key>)result.getProperty("followed");
            if(userKey != null){
                result.setProperty("nreFollows", userKey.size());
            }
            else{
                result.setProperty("nreFollows", 0);
            }
            crumbKey = (ArrayList<Key>)result.getProperty("crumbs");
            if(crumbKey != null){
                result.setProperty("nreCrumbs", crumbKey.size());
            }
            else{
                result.setProperty("nreCrumbs", 0);
            }
            DBS.put(result);
        }
    }
    
    /**
     *
     * @return
     */
    public Object setCreationDate(){
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> ret = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                Date today = Calendar.getInstance().getTime();
                result.setProperty("creationDate",today);
                DBC.putEntity(result);
            }
            catch(Exception e){
                
            }
        }
        return ret;
    }
    
    /**
     *
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public Object performance() throws EntityNotFoundException, IOException{
        ArrayList<CrumbInfo> crumbs = new ArrayList<>();
        Utilities u = new Utilities();
        for(int i = 0; i < 50; ++i){
            crumbs.add(u.entityToCrumb(DBC.getById("ag5zfmNydW1iaXQtMTMwNHISCxIFQ3J1bWIYgICAgOPFjwgM"),null));
        }
        return crumbs;
    }
    
    /**
     *
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Object performanceAsync() throws EntityNotFoundException, IOException, InterruptedException, ExecutionException{
        ArrayList<CrumbInfo> crumbs = new ArrayList<>();
        Utilities u = new Utilities();
        ArrayList<Future<Entity>> fea = new  ArrayList<>();
        for(int i = 0; i < 50; ++i){
            fea.add(DBC.getByIdAsync("ag5zfmNydW1iaXQtMTMwNHISCxIFQ3J1bWIYgICAgOPFjwgM"));
        }
        for(int i = 0; i < 50; ++i){
            crumbs.add(u.entityToCrumb(fea.get(i).get(),null));
        }
        return crumbs;
    }
    
    public Object cleanThemes() throws EntityNotFoundException, IOException, InterruptedException, ExecutionException{
        PreparedQuery allThemes = DBS.getAllThemesSorted();

        ArrayList<String> newNames = new ArrayList<>();
        Entity first = null;
        boolean compare = true;
        for(Entity result : allThemes.asIterable()){
            if(!compare && areEquals(first,result)){
                String name = (String) result.getProperty("name");
                newNames.add(name);
            }
            else{
                newNames.add("--------------------------");
                first = result;
                compare = false;
            }
        }
        return newNames;
    }
    
    private boolean areEquals(Entity theme1, Entity theme2){
        return (((String) theme1.getProperty("name")).equals(((String) theme2.getProperty("name"))));
    }
    
    public Object cleanDoubleThemes() throws EntityNotFoundException, IOException, InterruptedException, ExecutionException{
        PreparedQuery allThemes = DBS.getAllThemesSorted();

        ArrayList<String> newNames = new ArrayList<>();
        ArrayList<Key> repeatedKeys = new ArrayList<>();
        Entity first = null;
        boolean compare = true;
        boolean end = false;
        Utilities u = new Utilities();
        ArrayList<Key> crumbs = new ArrayList<>();
        ArrayList<Key> followed = new ArrayList<>();
        ArrayList<Key> crumbsToAdd = new ArrayList<>();
        ArrayList<Key> followedToAdd = new ArrayList<>();
        for(Entity result : allThemes.asIterable()){
            if(result.getProperty("toRemove") == null){
            if(!compare && areEquals(first,result)){
                String name = (String) result.getProperty("name");
                crumbsToAdd = (ArrayList<Key>) result.getProperty("crumbs");
                followedToAdd = (ArrayList<Key>) result.getProperty("followed");
                if(crumbsToAdd != null){
                    crumbs.removeAll(crumbsToAdd);
                    crumbs.addAll(crumbsToAdd);
                    for(int i = 0; i < crumbsToAdd.size(); ++i){
                        Entity crumb = DBC.getById(crumbsToAdd.get(i));
                        ArrayList<Key> crumbThemes = (ArrayList<Key>) crumb.getProperty("theme");
                        if(crumbThemes != null){
                            crumbThemes.remove(result.getKey());
                            crumbThemes.remove(first.getKey());
                        }
                        else{
                            crumbThemes = new ArrayList<>();
                        }
                        crumbThemes.add(first.getKey());
                        crumb.setProperty("theme", crumbThemes);
                        DBC.put(crumb);
                    }
                }
                if (followedToAdd != null){
                    followed.removeAll(followedToAdd);
                    followed.addAll(followedToAdd);
                    for(int i = 0; i < followedToAdd.size(); ++i){
                        Entity user = DBC.getById(followedToAdd.get(i));
                        ArrayList<Key> followedThemes = (ArrayList<Key>) user.getProperty("followedThemes");
                        if(followedThemes != null){
                            followedThemes.remove(result.getKey());
                            //return first;
                            followedThemes.remove(first.getKey());
                        }
                        else{
                            followedThemes = new ArrayList<>();
                        }
                        followedThemes.add(first.getKey());
                        user.setProperty("followedThemes", followedThemes);
                        DBC.put(user);
                    }
                }
                if(crumbs != null){
                    first.setProperty("crumbs",crumbs);
                    first.setProperty("nreCrumbs",crumbs.size());
                }
                if(followed != null){
                    first.setProperty("followed",followed);
                    first.setProperty("nreFollows",followed.size());
                }
                newNames.add(name);
                repeatedKeys.add(result.getKey());
                end = true;
                result.setProperty("toRemove", true);
                DBC.put(result);
            }
                
            else{
                if(end){
                    DBC.put(first);
                    return u.entityToTheme(first, null, 0);
                }
                newNames.add("--------------------------");
                first = result;
                crumbs = (ArrayList<Key>) first.getProperty("crumbs");
                if (crumbs == null) crumbs = new ArrayList();
                followed = (ArrayList<Key>) first.getProperty("followed");
                if (followed == null) followed = new ArrayList();
                compare = false;
            }
            }
        }
        return newNames;
    }
    
    public Object deleteUnusedThemes() throws EntityNotFoundException, IOException, InterruptedException, ExecutionException{
        PreparedQuery allThemes = DBS.getAllThemes();
        ArrayList<String> toRemove = new ArrayList<>();
        for(Entity result : allThemes.asIterable()){
            ArrayList<Key> crumbs = (ArrayList<Key>) result.getProperty("crumbs");
            ArrayList<Key> followed = (ArrayList<Key>) result.getProperty("followed");
            if(crumbs == null){
                if(followed == null){
                    DBS.delete(result.getKey());
                }
                else{
                    for(int i = 0; i < followed.size(); ++i){
                        Entity user = DBS.getById(followed.get(i));
                        ArrayList<Key> themes = (ArrayList<Key>) user.getProperty("followedThemes");
                        if(themes != null){
                            themes.remove(result.getKey());
                            DBS.put(user);
                        }
                        DBS.delete(result.getKey());
                    }
                }
            }
        }
        return toRemove;
    }
    
    public Object separeThemes() throws EntityNotFoundException, IOException, InterruptedException, ExecutionException{
        PreparedQuery allThemes = DBS.getAllThemes();
        //Entity result = DBS.getById("ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAwNXjkgoM");
        int cleaned = 0;
        for(Entity result : allThemes.asIterable() ){
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
            if(cleaned < 10){
                String name = (String) result.getProperty("name");
                int cont = 0;
                indexes.clear();
                for(int i = 0; i < name.length(); ++i){
                    if(name.charAt(i) == '#'){
                        ++cont;
                        indexes.add(i);
                    }
                }
                if(cont >= 2){
                    ArrayList<Key> themeCrumbs = (ArrayList<Key>) result.getProperty("crumbs");
                    ArrayList<Key> usersFollowing = (ArrayList<Key>) result.getProperty("followed");
                    for(int i = 0; i < indexes.size()-1;++i){
                        String subName = name.substring(indexes.get(i), indexes.get(i+1));
                        if(subName.startsWith(" ")) subName = subName.substring(1, subName.length());
                        if(subName.endsWith(" ")) subName = subName.substring(0, subName.length()-1);
                        names.add(subName);
                    }
                    for(int i = 0; i < names.size(); ++i){
                        Entity e = new Entity("Theme");
                        e.setProperty("name", names.get(i));
                        Key newThemeKey = DBS.put(e);
                        Entity newTheme = DBS.getById(newThemeKey);

                        if(themeCrumbs != null){
                            for(int j = 0; j < themeCrumbs.size(); ++j){
                                Entity crumb = DBS.getById(themeCrumbs.get(j));
                                ArrayList<Key> themes = (ArrayList<Key>) crumb.getProperty("theme");
                                themes.remove(result.getKey());
                                themes.add(newThemeKey);
                                
                                DBS.put(crumb);
                            }
                            newTheme.setProperty("crumbs", themeCrumbs);
                            newTheme.setProperty("nreCrumbs", themeCrumbs.size());
                        }
                        else{
                            newTheme.setProperty("nreCrumbs", 0);
                        }
                        if(usersFollowing != null){
                            for(int j = 0; j < usersFollowing.size(); ++j){
                                Entity user = DBS.getById(usersFollowing.get(j));
                                ArrayList<Key> themes = (ArrayList<Key>) user.getProperty("followedThemes");
                                themes.remove(result.getKey());
                                themes.add(newThemeKey);
                                DBS.put(user);
                            }
                            newTheme.setProperty("followed", themeCrumbs);
                            newTheme.setProperty("nreFollows", usersFollowing.size());
                        }
                        else{
                            newTheme.setProperty("nreFollows", 0);
                        }
                        DBS.put(newTheme);
                    }
                    DBS.delete(result.getKey());
                    ++cleaned;
                }
            }
        }
        return null;
    
    }
    
    public Object indexTest() throws EntityNotFoundException{
        Utilities u = new Utilities();
        return u.entityToUser(DBL.indexTest().asSingleEntity());
    }
    
    public Object getUrl(){
        String key = "/gs/crumbit/10086297911472666831561.jpg"; // Such as /gs/example-bucket/categories/animals.png"
        ImagesService images = ImagesServiceFactory.getImagesService();
        GcsFilename gcsFilename = new GcsFilename("crumbit", "10086297911472666831561.jpg");
        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey = blobstore.createGsBlobKey(key);
        ServingUrlOptions opts = ServingUrlOptions.Builder.
                withBlobKey(blobKey).
                secureUrl(true);
        return images.getServingUrl(opts);
    }
    
    
    public void addFileInfo() throws IllegalAccessException, EntityNotFoundException, IOException{
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> oldFiles = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                ArrayList<Key> files = new ArrayList<>();
                files = (ArrayList<Key>) result.getProperty("file");
                if(files == null){
                    oldFiles = ((ArrayList<String>) result.getProperty("crumbFile"));
                    ArrayList<Key> keys = new ArrayList<>();
                    for(int i = 0; i < oldFiles.size(); ++i){
                        String file = oldFiles.get(i);
                        FileQuery fi = new FileQuery();
                        String thumbnail = (String) result.getProperty("thumbnail");
                        Key k = null;
                        if(thumbnail != null){
                            k = fi.insertFile(file, thumbnailBucket, false, result.getKey(), null,0);
                        }
                        keys.add(fi.insertFile(file, bucket, (file.endsWith(".mp4")|| file.endsWith(".avi")|| file.endsWith(".mkv")|| file.endsWith(".3gp")|| file.endsWith(".ts")), result.getKey(), k,0));
                        result.setProperty("file", keys);
                        DBC.put(result);
                    }
                }
            }
            catch(Exception e){
                
            }
        }
    }
    
    
    public ArrayList<String> fileInfoClean() throws IllegalAccessException, EntityNotFoundException, IOException{
        PreparedQuery pq = DBF.getAllFiles();
        ArrayList<String> errKeys = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            String url = (String) result.getProperty("fileUrl");
            if(url != null){
                if(url.equals("ImagesServiceFailureException")){
                    try{
                        String fileBucket = (String) result.getProperty("bucket");
                        String storageId = (String) result.getProperty("storageId");

                        String key = "/gs/"+fileBucket+"/"+storageId;
                        ImagesService images = ImagesServiceFactory.getImagesService();
                        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
                        BlobKey blobKey = blobstore.createGsBlobKey(key);
                        ServingUrlOptions opts = ServingUrlOptions.Builder.
                                withBlobKey(blobKey).
                                secureUrl(true);
                        result.setProperty("fileUrl", images.getServingUrl(opts));
                        DBF.put(result);
                    }
                    catch(Exception e){
                        
                        result.setProperty("storageId","10208161472743227464.jpg");
                        String key = "/gs/"+"crumbit"+"/"+"10208161472743227464.jpg";
                        ImagesService images = ImagesServiceFactory.getImagesService();
                        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
                        BlobKey blobKey = blobstore.createGsBlobKey(key);
                        ServingUrlOptions opts = ServingUrlOptions.Builder.
                                withBlobKey(blobKey).
                                secureUrl(true);
                        result.setProperty("fileUrl", images.getServingUrl(opts));
                        errKeys.add((String) result.getProperty("storageId"));
                        DBF.put(result);
                    }
                }
            }
        }
        return errKeys;
    }
    
    public void deleteCrumb(String crumbId) throws EntityNotFoundException{
        DBC.deleteCrumb(crumbId);
    }
    
    public ArrayList<CrumbInfo> multipleSearch() throws EntityNotFoundException, IOException{
        ArrayList<CrumbInfo> ret = new ArrayList<>();
        PreparedQuery pq = DBC.multipleSearch();
        Utilities u = new Utilities();
        for(Entity r : pq.asIterable()){
            ret.add(u.entityToCrumb(r, null));
        }
        return ret;
    }
    
    public Object removeAllUnexsitentNotifications() throws EntityNotFoundException{
        PreparedQuery pq = DBA.getAllNotifications();
        ArrayList<String> ret = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                Key key = (Key) result.getProperty("id");
                DBA.getById(key);
            }
            catch(EntityNotFoundException e){
                String type = (String) result.getProperty("type");
                if(type.equals("crumb")){
                    Key user =  (Key) result.getProperty("userId");
                    Entity userE = DBA.getById(user);
                    
                    ArrayList<Key> followNotification =  (ArrayList<Key>) userE.getProperty("followNotification");
                    ArrayList<Key> ownNotification =  (ArrayList<Key>) userE.getProperty("ownNotification");
                    ArrayList<Key> pendentNotification =  (ArrayList<Key>) userE.getProperty("pendentNotification");
                    
                    if(followNotification!=null) followNotification.remove(result.getKey());
                    if(ownNotification!=null) ownNotification.remove(result.getKey());
                    if(pendentNotification!=null) pendentNotification.remove(result.getKey());
                    
                    userE.setProperty("followNotification", followNotification);
                    userE.setProperty("ownNotification", ownNotification);
                    userE.setProperty("pendentNotification", pendentNotification);
                    
                    DBA.put(userE);
                }
                if(type.equals("theme")){
                    ArrayList<Key> users = (ArrayList<Key>) result.getProperty("userId");
                    for(int i = 0; i < users.size(); ++i){
                        
                    Entity userE = DBA.getById(users.get(i));
                    
                    ArrayList<Key> followNotification =  (ArrayList<Key>) userE.getProperty("followNotification");
                    ArrayList<Key> ownNotification =  (ArrayList<Key>) userE.getProperty("ownNotification");
                    ArrayList<Key> pendentNotification =  (ArrayList<Key>) userE.getProperty("pendentNotification");
                    
                    if(followNotification!=null) followNotification.remove(result.getKey());
                    if(ownNotification!=null) ownNotification.remove(result.getKey());
                    if(pendentNotification!=null) pendentNotification.remove(result.getKey());
                    
                    userE.setProperty("followNotification", followNotification);
                    userE.setProperty("ownNotification", ownNotification);
                    userE.setProperty("pendentNotification", pendentNotification);
                    
                    DBA.put(userE);
                        
                    }
                }
                
                DBA.delete(result.getKey());
                
            }
        }
        return ret;
    }
    
    public ArrayList<String> deleteAllCorruptedCrumbs() throws IllegalAccessException, EntityNotFoundException, IOException{
        PreparedQuery pq = DBC.getAllCrumbs();
        ArrayList<String> keys = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            try{
                DBC.getById((Key) result.getProperty("placeId"));
            }
            catch(EntityNotFoundException e){
                keys.add(KeyFactory.keyToString(result.getKey()));
                DBC.deleteCrumb(KeyFactory.keyToString(result.getKey()));
            }
            
        }
        return keys;
    }
    public void sensitiveContent() throws IllegalAccessException, EntityNotFoundException, IOException{
        PreparedQuery pq = DBC.getAllCrumbs();
        for (Entity result : pq.asIterable()) {
            
                result.setProperty("sensitiveContent", false);
                DBC.put(result);
            
            
        }
    }
    
    public void addQueueTask(String url){
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl(url));
    }
    
    public ArrayList<String> oneFormatThemes(){
        PreparedQuery pq = DBS.getAllThemes();
        ArrayList<String> incorrect = new ArrayList<>();
        for(Entity result: pq.asIterable()){
            String name1 = (String) result.getProperty("name");
            String name = name1.toLowerCase().replaceAll(" ", "");
            if(!name.startsWith("#")){
                name = "#"+ name;
            }
            if(!name.equals(name1)){
                incorrect.add(name1);
                result.setProperty("name", name);
                DBS.put(result);
            }
        }
        return incorrect;
    }
    
    public void aspectRatio() throws EntityNotFoundException, IOException{
            PreparedQuery pq = DBF.getAllFiles();
            Storage f = new Storage();
            for(Entity result: pq.asIterable()){
                Key thumbnailKey = (Key) result.getProperty("thumbnail");
                if(thumbnailKey != null){
                    Entity thumbnail = DBF.getById(thumbnailKey);
                    String storageId = (String) thumbnail.getProperty("storageId");
                    
                    GcsFilename filename = new GcsFilename(thumbnailBucket,storageId);
                    Image blobImage = ImagesServiceFactory.makeImage(f.downLoadFile(filename));
                    double width = (double)blobImage.getWidth();
                    double height = (double)blobImage.getHeight();
                    result.setProperty("aspectRatio", height/width);
                    DBA.put(result);
                }
            }
    }
    
    public ArrayList<ThemeInfo> themesAuto(String name) throws EntityNotFoundException{
        PreparedQuery pq = DBS.themesAutocomplete(name);
        ArrayList<ThemeInfo> autoThemes = new ArrayList<>();
        Utilities u = new Utilities();

        for (Entity result : pq.asIterable()) {
                    autoThemes.add(u.entityToTheme(result,null,0));
            }

        return autoThemes;
    }
    
    public void placesFormat() throws EntityNotFoundException{
        PreparedQuery pq = DBP.getAllPlaces();

        for (Entity result : pq.asIterable()) {
            String name = (String) result.getProperty("name");
            if(name != null){
                String formatedName = name.toLowerCase().replaceAll(" ", "");
                result.setProperty("formatedName", formatedName);
                DBP.put(result);
            }
        }

    }
    
    public void getEmbed(String crumbId) throws EntityNotFoundException, IOException{
        Storage storage = new Storage();
        storage.uploadHtml(crumbId);
    }
    public void geospacial(){
        Entity station = new Entity("GasStation");
        station.setProperty("brand", "Ocean Ave Shell");
        station.setProperty("location", new GeoPt(37.7913156f, -122.3926051f));
        DBC.put(station);
    }
    
    public List<Entity> geospacialCirlce(){
        
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        GeoPt southwest = new GeoPt(30f, -120f);
        GeoPt northeast = new GeoPt(40f, -123f);
        Filter f2 = new StContainsFilter("location", new Rectangle(southwest, northeast));
        Query q2 = new Query("GasStation").setFilter(f2);
        // [END geospatial_stcontainsfilter_examples]

        return datastore.prepare(q2).asList(FetchOptions.Builder.withDefaults());
    }
    public Object getPlaceList(float botLat, float botLng, float topLat, float topLng){
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
         ArrayList<Filter> filters = new ArrayList<>();
        
                Query placesLat = new Query("Place").setKeysOnly();
                Query placesLng = new Query("Place").setKeysOnly();
                Filter latG = new Query.FilterPredicate("lat", Query.FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
                Filter latL = new Query.FilterPredicate("lat", Query.FilterOperator.LESS_THAN_OR_EQUAL, topLat);

                Filter lngG = new Query.FilterPredicate("lng", Query.FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
                Filter lngL = new Query.FilterPredicate("lng", Query.FilterOperator.LESS_THAN_OR_EQUAL, topLng);
                
                Query.CompositeFilter latFilter = Query.CompositeFilterOperator.and(latG, latL);
                Query.CompositeFilter lngFilter = Query.CompositeFilterOperator.and(lngG, lngL);
                
                placesLat.setFilter(latFilter);
                placesLng.setFilter(lngFilter);
                
                List<Entity> listLat = datastore.prepare(placesLat).asList(FetchOptions.Builder.withDefaults());
                List<Entity> listLng = datastore.prepare(placesLng).asList(FetchOptions.Builder.withDefaults());
                
                listLat.retainAll(listLng);
                ListIterator<Entity> it = listLat.listIterator();
                 int limit = 0;
                ArrayList<ArrayList<Key>> matrixPlacesKey = new ArrayList<>();
                matrixPlacesKey.add(new ArrayList<Key>());
                int reachLimit = 0;
                while(it.hasNext() && reachLimit < 2){
                    matrixPlacesKey.get(reachLimit).add(it.next().getKey());
                    if(limit>=10){
                        limit = 0;
                        Filter placeFilter =  new Query.FilterPredicate("placeId", Query.FilterOperator.IN, matrixPlacesKey.get(reachLimit));
                        filters.add(placeFilter);
                        matrixPlacesKey.add(new ArrayList<Key>());
                        ++reachLimit;
                    }
                    ++limit;
                }
                if(reachLimit < matrixPlacesKey.size()) {
                    if(!matrixPlacesKey.get(reachLimit).isEmpty()){
                        Filter placeFilter =  new Query.FilterPredicate("placeId", Query.FilterOperator.IN, matrixPlacesKey.get(reachLimit));
                        filters.add(placeFilter);
                    }
                }
                Query.CompositeFilter totalFilter = Query.CompositeFilterOperator.or(filters);
                Query crumbs = new Query("Crumb");
                crumbs.setFilter(totalFilter).addSort("date",Query.SortDirection.DESCENDING);
                return datastore.prepare(crumbs).asList(FetchOptions.Builder.withLimit(10).offset(0*10));
    }
    
    public void removingToRemove(String theme){
        Query q = new Query("Theme");
        Filter latG = new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, theme);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        q.setFilter(latG);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity result : pq.asIterable()){
            if(((String)result.getProperty("name")).equals(theme)){
                result.removeProperty("toRemove");
                datastore.put(result);
            }
        }
    }
}
