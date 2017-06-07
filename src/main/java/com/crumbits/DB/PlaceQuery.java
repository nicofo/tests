package com.crumbits.DB;

import static com.crumbits.DB.Queries.asyncDatastore;
import static com.crumbits.DB.Queries.datastore;
import java.util.ArrayList;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.GeoRegion.Rectangle;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Transaction;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 *
 * @author Miquel Ferriol
 */
public class PlaceQuery extends Queries {

    /**
     *
     * @param name
     * @param lat
     * @param lng
     * @return
     */
    public Key insertPlace(String name, float lat, float lng, String googlePlaceId) {
        Filter placeFilter = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, googlePlaceId);
        Query q = new Query("Place").setFilter(placeFilter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            return result.getKey();
        }
        
        Entity e = new Entity("Place");

        e.setProperty("name", name);
        e.setProperty("lat", lat);
        e.setProperty("lng", lng);
        e.setProperty("googlePlaceId", googlePlaceId);
        e.setProperty("formatedPlace", name.toLowerCase().replaceAll("", " "));
        return datastore.put(e);

    }
    
    /**
     *
     * @param name
     * @param lat
     * @param lng
     * @return
     */
    public Future<Key> insertPlaceAsync(String name, float lat, float lng) {
        Entity e = new Entity("Place");

        e.setProperty("name", name);
        GeoPt g = new GeoPt(lat, lng);
        Rectangle r = new Rectangle(g, g);
        r.contains(g);
        e.setProperty("coordiante", g);
        e.setProperty("lat", lat);
        e.setProperty("lng", lng);
        return asyncDatastore.put(e);
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllPlaces() {

        Query q = new Query("Place");

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getAllPlacesAsync() {

        Query q = new Query("Place");

        return asyncDatastore.prepare(q);
    }
    
    /**
     *
     * @param start
     * @return
     */
    public PreparedQuery getPlacesStart(String start){
        Query q = new Query("Place").addSort("name", SortDirection.ASCENDING);

        return datastore.prepare(q);
    }
    
    /**
     *
     * @param start
     * @return
     */
    public PreparedQuery getPlacesStartAsync(String start){
        Query q = new Query("Place").addSort("name", SortDirection.ASCENDING);

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
     * @param placeId
     * @throws EntityNotFoundException
     */
    public void addUserIsFollowingPlace(String userId, String placeId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity place = getById(placeId);

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedPlaces");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.add(place.getKey());
        user.setProperty("followedPlaces", userKey);
        datastore.put(user);

        ArrayList<Key> placeKey = (ArrayList<Key>) place.getProperty("followed");
        if(placeKey == null) placeKey = new ArrayList<>();
        placeKey.add(user.getKey());
        place.setProperty("followed", placeKey);
        
        place.setProperty("nreFollows",placeKey.size());
        
        datastore.put(place);
    }
    
    /**
     *
     * @param userId
     * @param placeId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addUserIsFollowingPlaceAsync(String userId, String placeId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> fePlace = getByIdAsync(placeId);
        
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity user = feUser.get();
        Entity place = fePlace.get(); 

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedPlaces");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.add(place.getKey());
        user.setProperty("followedPlaces", userKey);
        asyncDatastore.put(user);

        ArrayList<Key> placeKey = (ArrayList<Key>) place.getProperty("followed");
        if(placeKey == null) placeKey = new ArrayList<>();
        placeKey.add(user.getKey());
        place.setProperty("followed", placeKey);
        
        place.setProperty("nreFollows",placeKey.size());
        
        asyncDatastore.put(place);
        
        fTrans.get().commitAsync();
    }

    /**
     *
     * @param userId
     * @param placeId
     * @throws EntityNotFoundException
     */
    public void delUserIsFollowingPlace(String userId, String placeId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity place = getById(placeId);

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedPlaces");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.remove(place.getKey());
        user.setProperty("followedPlaces", userKey);
        datastore.put(user);

        ArrayList<Key> placeKey = (ArrayList<Key>) place.getProperty("followed");
        if(placeKey == null) placeKey = new ArrayList<>();
        placeKey.remove(user.getKey());
        place.setProperty("followed", placeKey);
        
        place.setProperty("nreFollows",placeKey.size());
        
        datastore.put(place);
    }
    
    /**
     *
     * @param userId
     * @param placeId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void delUserIsFollowingPlaceAsync(String userId, String placeId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> fePlace = getByIdAsync(placeId);
        
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity user = feUser.get();
        Entity place = fePlace.get(); 

        ArrayList<Key> userKey = (ArrayList<Key>) user.getProperty("followedPlaces");
        if(userKey == null) userKey = new ArrayList<>();
        userKey.remove(place.getKey());
        user.setProperty("followedPlaces", userKey);
        asyncDatastore.put(user);

        ArrayList<Key> placeKey = (ArrayList<Key>) place.getProperty("followed");
        if(placeKey == null) placeKey = new ArrayList<>();
        placeKey.remove(user.getKey());
        place.setProperty("followed", placeKey);
        
        place.setProperty("nreFollows",placeKey.size());
        
        asyncDatastore.put(place);
        
        fTrans.get().commitAsync();
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getSuggestedPlaces(){
        Query q = new Query("Place").addSort("nreFollows", SortDirection.DESCENDING);
        return datastore.prepare(q);
    }
    
    /**
     *
     * @return
     */
    public PreparedQuery getSuggestedPlacesAsync(){
        Query q = new Query("Place").addSort("nreFollows", SortDirection.DESCENDING);
        return asyncDatastore.prepare(q);
    }
    
    /**
     *
     * @param googlePlaceId
     * @return
     */
    public PreparedQuery getPlaceByGooglePlaceId(String googlePlaceId){
        Filter filterId = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, googlePlaceId);
        Query q = new Query("Place").setFilter(filterId);
        return datastore.prepare(q);
    }
    
    /**
     *
     * @param googlePlaceId
     * @return
     */
    public PreparedQuery getPlaceByGooglePlaceIdAsync(String googlePlaceId){
        Filter filterId = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, googlePlaceId);
        Query q = new Query("Place").setFilter(filterId);
        return asyncDatastore.prepare(q);
    }
    
    public PreparedQuery placesAutocomplete(String name){
        String formatedName = name.toLowerCase().replaceAll(" ", "");
        Filter placeFilterGreater = new FilterPredicate("formatedName", Query.FilterOperator.GREATER_THAN_OR_EQUAL, formatedName);
        Filter placeFilterLess = new FilterPredicate("formatedName", Query.FilterOperator.LESS_THAN_OR_EQUAL, formatedName+ "\uFFFD");
        Query placeQuery = new Query("Place");
        Query.CompositeFilter place = Query.CompositeFilterOperator.and(placeFilterGreater, placeFilterLess);
        placeQuery.setFilter(place);
        return datastore.prepare(placeQuery);
    }
}
