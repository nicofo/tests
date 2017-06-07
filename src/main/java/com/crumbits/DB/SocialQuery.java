
package com.crumbits.DB;


import static com.crumbits.DB.Queries.datastore;

import java.util.ArrayList;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Miquel Ferriol
 */
public class SocialQuery extends Queries {
    
    /**
     *
     * @param name
     * @return
     */
    public Key insertTheme(String name){
        if(!name.startsWith("#")) name= "#"+name;
        name= name.toLowerCase();
        Filter themeFilter = new FilterPredicate("name", Query.FilterOperator.EQUAL, name);
        Query q = new Query("Theme").setFilter(themeFilter);
        PreparedQuery pq = datastore.prepare(q);
        for(Entity result : pq.asIterable()){
            return result.getKey();
        }
        Entity e = new Entity("Theme");
        e.setProperty("name", name);
        
        return datastore.put(e);
    }
    
    /**
     *
     * @param name
     * @return
     */
    public Future<Key> insertThemeAsync(String name){
        Entity e = new Entity("Theme");
        e.setProperty("name", name);
        
        return super.putAsync(e);
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllThemes() {

        Query q = new Query("Theme");

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }
    
    public PreparedQuery getAllThemesSorted() {

        Query q = new Query("Theme").addSort("name", Query.SortDirection.DESCENDING);

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getAllThemesAsync() {

        Query q = new Query("Theme");

        PreparedQuery pq = asyncDatastore.prepare(q);

        return pq;
    }
    
    /**
     *
     * @param start
     * @return
     */
    public PreparedQuery getThemesStart(String start){
        Query q = new Query("Theme").addSort("name", Query.SortDirection.ASCENDING);

        return datastore.prepare(q);
    }
    
    /**
     *
     * @param start
     * @return
     */
    public PreparedQuery getThemesStartAsync(String start){
        Query q = new Query("Theme").addSort("name", Query.SortDirection.ASCENDING);

        return asyncDatastore.prepare(q);
    }


    /**
     *
     * @param searchPlaces
     * @return
     */
    public String insertSearch(ArrayList<Key> searchPlaces) {
        Entity e = new Entity("Search");
        e.setProperty("search", searchPlaces);
        return KeyFactory.keyToString(datastore.put(e));
    }
    
    /**
     *
     * @param searchPlaces
     * @return
     */
    public Future<Key> insertSearchAsync(ArrayList<Key> searchPlaces) {
        Entity e = new Entity("Search");
        e.setProperty("search", searchPlaces);
        return asyncDatastore.put(e);
    }


    /**
     *
     * @param userId
     * @param themeId
     * @throws EntityNotFoundException
     */
    public void addUserIsFollowing(String userId, String themeId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity theme = getById(themeId);

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedThemes");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.add(theme.getKey());
        user.setProperty("followedThemes", userKey);
        datastore.put(user);

        ArrayList<Key> themeKey = (ArrayList<Key>) theme.getProperty("followed");
        if(themeKey == null) themeKey = new ArrayList<>();
        themeKey.add(user.getKey());
        theme.setProperty("followed", themeKey);
        
        theme.setProperty("nreFollows",themeKey.size());
        
        datastore.put(theme);
    }
    
    /**
     *
     * @param userId
     * @param themeId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addUserIsFollowingAsync(String userId, String themeId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> feTheme = getByIdAsync(themeId);

        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity user = feUser.get();
        Entity theme = feTheme.get();
        
        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedThemes");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.add(theme.getKey());
        user.setProperty("followedThemes", userKey);
        
        asyncDatastore.put(user);

        ArrayList<Key> themeKey = (ArrayList<Key>) theme.getProperty("followed");
        if(themeKey == null) themeKey = new ArrayList<>();
        themeKey.add(user.getKey());
        theme.setProperty("followed", themeKey);
        
        theme.setProperty("nreFollows",themeKey.size());
        
        asyncDatastore.put(theme);
        
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @param themeId
     * @return
     * @throws EntityNotFoundException
     */
    public ArrayList<Key> getThemeCrumbs(String themeId) throws EntityNotFoundException{
        Entity e = getById(themeId);
        return (ArrayList<Key>)e.getProperty("crumbs");
    }
    
    /**
     *
     * @param themeId
     * @return
     * @throws EntityNotFoundException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public ArrayList<Key> getThemeCrumbsAsync(String themeId) throws EntityNotFoundException, ExecutionException, InterruptedException{
        ArrayList<Key> crumbKey = new ArrayList<>();
        Future<Entity> feTheme = getByIdAsync(themeId);
        return (ArrayList<Key>)feTheme.get().getProperty("crumbs");
    }

    /**
     *
     * @param userId
     * @param themeId
     * @throws EntityNotFoundException
     */
    public void delUserIsFollowing(String userId, String themeId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity theme = getById(themeId);

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedThemes");
        userKey.remove(theme.getKey());
        user.setProperty("followedThemes", userKey);
        datastore.put(user);

        ArrayList<Key> themeKey = (ArrayList<Key>) theme.getProperty("followed");
        if(themeKey == null){
            theme.setProperty("nreFollows",0);
        }
        else{
            themeKey.remove(user.getKey());
            theme.setProperty("followed", themeKey);

            theme.setProperty("nreFollows",themeKey.size());
        }
        
        
        datastore.put(theme);
    }
    
    /**
     *
     * @param userId
     * @param themeId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void delUserIsFollowingAsync(String userId, String themeId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> feTheme = getByIdAsync(themeId);

        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity user = feUser.get();
        Entity theme = feTheme.get();
        
        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedThemes");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.remove(theme.getKey());
        user.setProperty("followedThemes", userKey);
        
        asyncDatastore.put(user);

        ArrayList<Key> themeKey = (ArrayList<Key>) theme.getProperty("followed");
        if(themeKey == null) themeKey = new ArrayList<>();
        themeKey.remove(user.getKey());
        theme.setProperty("followed", themeKey);
        
        theme.setProperty("nreFollows",themeKey.size());
        
        asyncDatastore.put(theme);
        
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getSuggestedThemes(){
        Query q = new Query("Theme").addSort("nreFollows", Query.SortDirection.DESCENDING);
        return datastore.prepare(q);
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getSuggestedThemesAsync(){
        Query q = new Query("Theme").addSort("nreFollows", Query.SortDirection.DESCENDING);
        return asyncDatastore.prepare(q);
    }
    
    public PreparedQuery themesAutocomplete(String name){
        String formatedName = name.toLowerCase().replaceAll(" ", "");
        if(!formatedName.startsWith("#"))formatedName ="#"+formatedName;
        Filter themeFilterGreater = new FilterPredicate("name", Query.FilterOperator.GREATER_THAN_OR_EQUAL, formatedName);
        Filter themeFilterLess = new FilterPredicate("name", Query.FilterOperator.LESS_THAN_OR_EQUAL, formatedName + "\uFFFD");
        Query themeQuery = new Query("Theme");
        CompositeFilter theme = CompositeFilterOperator.and(themeFilterGreater, themeFilterLess);
        themeQuery.setFilter(theme);
        return datastore.prepare(themeQuery);
    }
    

}
