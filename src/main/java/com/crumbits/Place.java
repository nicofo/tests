package com.crumbits;

import com.crumbits.ReturnClasses.SearchIdReturn;
import com.crumbits.Info.CrumbInfo;
import com.crumbits.Info.PlaceInfo;
import java.util.ArrayList;
import com.crumbits.Utilities.*;

import com.crumbits.DB.PlaceQuery;
import com.crumbits.ReturnClasses.IsFollowing;
import com.crumbits.ReturnClasses.PaginationRet;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.maps.model.*;
import java.io.IOException;

/**
 *
 * @author Miquel Ferriol
 */
public class Place {

    private final int PAGE_SIZE = 1;
    private PlaceQuery DB = new PlaceQuery();
    private String userKey;

    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Place(String token) throws IllegalAccessException {
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
     * @param name
     * @param lat
     * @param lng
     * @return
     */
    public PlaceInfo createPlace(String name, float lat, float lng, String googlePlaceId) throws EntityNotFoundException {
        Key k = DB.insertPlace(name, lat, lng, googlePlaceId);
        Utilities u = new Utilities();
        return u.entityToPlace(DB.getById(k), null, 0);
    }

    /**
     *
     * @param place
     * @param userKey
     * @return
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public ArrayList<PlaceInfo> placesAutocomplete(String place) throws EntityNotFoundException {
        ArrayList<PlaceInfo> autoPlaces = new ArrayList<>();
        PreparedQuery pq = DB.placesAutocomplete(place);
        Utilities u = new Utilities();
        for (Entity result : pq.asIterable()) {
            autoPlaces.add(u.entityToPlace(result, userKey,0)); 
        }
        return autoPlaces;
    }

    /**
     *
     * @param place
     * @return
     */
    public Object placesSearch(String place) {
        ArrayList<String> searchPlace = new ArrayList<>();
        ArrayList<Key> searchPlaceKey = new ArrayList<>();
        PreparedQuery pq = DB.getPlacesStart(place);

        for (Entity result : pq.asIterable()) {
            searchPlace.add((String) result.getProperty("name"));
            searchPlaceKey.add(result.getKey());
        }

        Utilities u = new Utilities();
        if (searchPlace.isEmpty()) {
            SearchIdReturn rc = new SearchIdReturn();
            rc.setSearchId(DB.insertSearch(null));
            return rc;
        }
        ArrayList<Integer> index = u.search(searchPlace, place);
        if (index == null) {
            SearchIdReturn rc = new SearchIdReturn();
            rc.setSearchId(DB.insertSearch(null));
            return rc;
        }

        ArrayList<Key> res = new ArrayList<>();
        for (int i = 0; i < index.size(); ++i) {
            res.add(searchPlaceKey.get(index.get(i)));
        }
        SearchIdReturn rc = new SearchIdReturn();
        rc.setSearchId(DB.insertSearch(res));
        return rc;
    }

    /**
     *
     * @param searchId
     * @param page
     * @param pageSize
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public PaginationRet getPlacesSearchResult(String searchId, int page, int pageSize) throws EntityNotFoundException {
        ArrayList<Key> searchIdResult = (ArrayList<Key>) DB.getById(searchId).getProperty("search");
        ArrayList<PlaceInfo> searchResult = new ArrayList<>();
        if (searchIdResult == null) {
            PaginationRet pr = new PaginationRet();
            pr.setList(searchResult);
            pr.setLastPage(0);
            return pr;
        }
        Utilities u = new Utilities();
        for (int i = page * pageSize; i < page * pageSize + pageSize; ++i) {
            if (i < searchIdResult.size()) {
                searchResult.add(u.entityToPlace(DB.getById(searchIdResult.get(i)),userKey,0));
            }
        }

        PaginationRet pr = new PaginationRet();
        pr.setList(searchResult);
        pr.setLastPage(((int) Math.ceil(searchIdResult.size() / (double)pageSize)) - 1);
        return pr;
    }

    /**
     *
     * @param placeId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public PlaceInfo getPlaceById(String placeId) throws EntityNotFoundException {
        Entity e = DB.getById(placeId);
        Utilities u = new Utilities();
        return u.entityToPlace(e,userKey,0);
    }
    
    /**
     *
     * @return
     */
    public ArrayList<LatLng> getAllCoordinates(){
        ArrayList<LatLng> coordinates = new ArrayList<>();
        PreparedQuery pq = DB.getAllPlaces();
        Utilities u = new Utilities();
        for (Entity result : pq.asIterable()) {
            LatLng chord = new LatLng((double)result.getProperty("lat"),(double)result.getProperty("lng"));
            coordinates.add(chord);
        }
        return coordinates;
    }

    /**
     *
     * @param placeId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws java.io.IOException
     */
    public PaginationRet getPlaceCrumbs(String placeId, int page, int pageSize) throws EntityNotFoundException, IOException {
        Entity e = DB.getById(placeId);
        ArrayList<Key> crumbKey = (ArrayList<Key>) e.getProperty("crumbs");
        if (crumbKey == null) {
            return null;
        }
        Utilities u = new Utilities();
        ArrayList<CrumbInfo> crumbs = new ArrayList<>();
        if(page == 0 && pageSize == 0){
            for (int i = 0; i < crumbKey.size(); ++i) {
                crumbs.add(u.entityToCrumb(DB.getById(crumbKey.get(crumbKey.size() - i -1)),userKey));
            }
        }
        else{
            for (int i = page*pageSize; i < page*pageSize+pageSize && i < crumbKey.size(); ++i) {
                crumbs.add(u.entityToCrumb(DB.getById(crumbKey.get(crumbKey.size() - i -1)),userKey));
            }
        }

        PaginationRet pr = new PaginationRet();
        pr.setList(crumbs);
        pr.setLastPage(((int) Math.ceil(crumbKey.size() / (double)pageSize)) - 1);
        return pr;
    }

    /**
     *
     * @param userKey
     * @param placeId
     * @return
     * @throws EntityNotFoundException
     */
    public IsFollowing getUserIsFollowingPlace( String placeId) throws EntityNotFoundException {
        Entity e = DB.getById(userKey);
        ArrayList<Key> follPlaces = (ArrayList<Key>) e.getProperty("followed");
        if (follPlaces == null) {
            return new IsFollowing(false);
        }
        return new IsFollowing(follPlaces.contains(KeyFactory.stringToKey(placeId)));
    }

    /**
     *
     * @param userKey
     * @param placeId
     * @throws EntityNotFoundException
     */
    public void userFollowPlace( String placeId) throws EntityNotFoundException {
        DB.addUserIsFollowingPlace(userKey, placeId);
    }

    /**
     *
     * @param userKey
     * @param placeId
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void userUnfollowPlace( String placeId) throws EntityNotFoundException {
        DB.delUserIsFollowingPlace(userKey, placeId);
    }

    /**
     *
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public ArrayList<PlaceInfo> getSuggestedPlaces() throws EntityNotFoundException{
        ArrayList<PlaceInfo> pi = new ArrayList<>();
        PreparedQuery pq = DB.getSuggestedPlaces();
        Utilities u = new Utilities();
        int i = 0;
        for(Entity result : pq.asIterable()){
            if(i < 15) pi.add(u.entityToPlace(result, userKey,1));
            ++i;
        }
        return pi;
    }
    
    /**
     *
     * @param googlePlaceId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public PlaceInfo getPlaceByGooglePlaceId(String googlePlaceId) throws EntityNotFoundException{
        Utilities u = new Utilities();
        PreparedQuery pq = DB.getPlaceByGooglePlaceId(googlePlaceId);
        
        return u.entityToPlace(pq.asSingleEntity(), userKey, 0);
    }
}
