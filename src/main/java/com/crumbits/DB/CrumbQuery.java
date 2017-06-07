package com.crumbits.DB;

import static com.crumbits.DB.Queries.asyncDatastore;
import com.crumbits.Info.CrumbInfo;
import com.crumbits.ReturnClasses.PaginationRet;
import com.crumbits.Storage;
import com.crumbits.Utilities.Utilities;
import java.util.ArrayList;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Transaction;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Miquel Ferriol
 */
  public class CrumbQuery extends Queries {


    private final String bucket = "crumbit";
    private final String thumbnailBucket = "crumbit/thumbnails";

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void addUserThanks(String userId, String crumbId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity crumb = getById(crumbId);

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("thanks");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        if(!crumbKey.contains(KeyFactory.stringToKey(crumbId))){
            crumbKey.add(KeyFactory.stringToKey(crumbId));
        }
        user.setProperty("thanks", crumbKey);
        datastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("thanked");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        if(!userKey.contains(KeyFactory.stringToKey(userId))){
            userKey.add(KeyFactory.stringToKey(userId));
        }
        crumb.setProperty("thanked", userKey);
        crumb.setProperty("nreThanks", userKey.size());

        int nreViews = Integer.parseInt(crumb.getProperty("nreViews").toString());
        ++nreViews;
        crumb.setProperty("nreViews", nreViews);
        datastore.put(crumb);
    }


    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addUserThanksAsync(String userId, String crumbId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();

        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> feCrumb = getByIdAsync(crumbId);

        Entity user = feUser.get();
        Entity crumb = feCrumb.get();

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("thanks");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(KeyFactory.stringToKey(crumbId));
        user.setProperty("thanks", crumbKey);
        asyncDatastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("thanked");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(KeyFactory.stringToKey(userId));
        crumb.setProperty("thanked", userKey);
        crumb.setProperty("nreThanks", userKey.size());
        asyncDatastore.put(crumb);

        fTrans.get().commitAsync();
    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void delUserThanks(String userId, String crumbId) throws EntityNotFoundException {
        Entity user = getById(userId);
        Entity crumb = getById(crumbId);

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("thanks");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.remove(KeyFactory.stringToKey(crumbId));
        user.setProperty("thanks", crumbKey);
        datastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("thanked");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.remove(KeyFactory.stringToKey(userId));
        crumb.setProperty("thanked", userKey);
        crumb.setProperty("nreThanks", userKey.size());
        datastore.put(crumb);
    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void delUserThanksAsync(String userId, String crumbId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();

        Future<Entity> feUser = getByIdAsync(userId);
        Future<Entity> feCrumb = getByIdAsync(crumbId);

        Entity user = feUser.get();
        Entity crumb = feCrumb.get();

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("thanks");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(KeyFactory.stringToKey(crumbId));
        user.setProperty("thanks", crumbKey);
        asyncDatastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("thanked");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(KeyFactory.stringToKey(userId));
        crumb.setProperty("thanked", userKey);
        crumb.setProperty("nreThanks", userKey.size());
        asyncDatastore.put(crumb);

        fTrans.get().commitAsync();
    }

    /**
     *
     * @param crumbId
     * @return
     * @throws EntityNotFoundException
     */
    public ArrayList<Entity> getRelatedCrumbs(String crumbId) throws EntityNotFoundException{

        Entity crumb = getById(crumbId);

        Key placeKey = (Key) crumb.getProperty("placeId");
        ArrayList<Key> themeKey = (ArrayList<Key>) crumb.getProperty("theme");
        Filter placeFilter = new FilterPredicate("place", FilterOperator.EQUAL, placeKey);
        Filter themeFilter = new FilterPredicate("theme", FilterOperator.IN, themeKey);
        Filter keyFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.NOT_EQUAL, KeyFactory.stringToKey(crumbId));
        /*
        CompositeFilter placeOrTheme = CompositeFilterOperator.or(placeFilter, themeFilter);
         */
        Query q = new Query("Crumb").setFilter(placeFilter).setFilter(keyFilter);
        Query q1 = new Query("Crumb").setFilter(themeFilter).setFilter(keyFilter);
        PreparedQuery pq = datastore.prepare(q);
        PreparedQuery pq1 = datastore.prepare(q1);
        List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        List<Entity> list1 = pq1.asList(FetchOptions.Builder.withDefaults());

        ArrayList<Entity> ae = new ArrayList();

        for (Entity result : pq1.asIterable()) {
            if (((Key) result.getProperty("placeId")).equals(placeKey)) {
                ae.add(result);
            }
        }


        if (ae.size() < 5) {
            ae.addAll(list);
            if (ae.size() < 5) {
                ae.addAll(list1);
            }
        }
        return ae;
    }

    /**
     *
     * @param crumbId
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public ArrayList<Entity> getRelatedCrumbsAsync(String crumbId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();

        Entity crumb = getByIdAsync(crumbId).get();

        Key placeKey = (Key) crumb.getProperty("placeId");
        ArrayList<Key> themeKey = (ArrayList<Key>) crumb.getProperty("theme");
        Filter placeFilter = new FilterPredicate("place", FilterOperator.EQUAL, placeKey);
        Filter themeFilter = new FilterPredicate("theme", FilterOperator.IN, themeKey);
        Filter keyFilter = new FilterPredicate(Entity.KEY_RESERVED_PROPERTY, FilterOperator.NOT_EQUAL, KeyFactory.stringToKey(crumbId));
        /* CompositeFilter placeOrTheme = CompositeFilterOperator.or(placeFilter, themeFilter);
         */
        Query q = new Query("Crumb").setFilter(placeFilter).setFilter(keyFilter);
        Query q1 = new Query("Crumb").setFilter(themeFilter).setFilter(keyFilter);
        PreparedQuery pq = asyncDatastore.prepare(q);
        PreparedQuery pq1 = asyncDatastore.prepare(q1);
        List<Entity> list = pq.asList(FetchOptions.Builder.withDefaults());
        List<Entity> list1 = pq1.asList(FetchOptions.Builder.withDefaults());

        ArrayList<Entity> ae = new ArrayList();

        for (Entity result : pq1.asIterable()) {
            if (((Key) result.getProperty("placeId")).equals(placeKey)) {
                ae.add(result);
            }
        }

        fTrans.get().commitAsync();

        if (ae.size() < 5) {
            ae.addAll(list);
            if (ae.size() < 5) {
                ae.addAll(list1);
            }
        }
        return ae;
    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void addUserShare(String userId, String crumbId, String socialNetwork) throws EntityNotFoundException{
        Entity e = new Entity("Report");
        e.setProperty("sharedCrumb",KeyFactory.stringToKey(crumbId));
        e.setProperty("userSharing", KeyFactory.stringToKey(userId));
        e.setProperty("socialNetwork", socialNetwork);
        e.setProperty("epochDate",Calendar.getInstance().getTimeInMillis());

         Key share = datastore.put(e);

        Entity user = getById(userId);
        Entity crumb = getById(crumbId);

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("share");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(share);
        user.setProperty("share", crumbKey);
        datastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("share");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(share);
        crumb.setProperty("shared", userKey);
        crumb.setProperty("nreShares", userKey.size());

        datastore.put(crumb);
    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addUserShareAsync(String userId, String crumbId, String socialNetwork) throws EntityNotFoundException, InterruptedException, ExecutionException {
       Future<Transaction> fTrans = asyncDatastore.beginTransaction();

       Entity e = new Entity("Report");
        e.setProperty("sharedCrumb",KeyFactory.stringToKey(crumbId));
        e.setProperty("userSharing", KeyFactory.stringToKey(userId));
        e.setProperty("socialNetwork", socialNetwork);
        e.setProperty("epochDate",Calendar.getInstance().getTimeInMillis());

        Future<Key> share = asyncDatastore.put(e);

        Entity user = getByIdAsync(userId).get();
        Entity crumb = getByIdAsync(crumbId).get();

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("share");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(share.get());
        user.setProperty("share", crumbKey);
        asyncDatastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("share");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(share.get());
        crumb.setProperty("shared", userKey);
        crumb.setProperty("nreShares", userKey.size());

        asyncDatastore.put(crumb);

        fTrans.get().commitAsync();
    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void addUserReport(String userId, String crumbId, String type, String comments) throws EntityNotFoundException {
        Entity e = new Entity("Report");
        e.setProperty("reportedCrumb",KeyFactory.stringToKey(crumbId));
        e.setProperty("userReporting", KeyFactory.stringToKey(userId));
        e.setProperty("type", type);
        e.setProperty("comments", comments);
        e.setProperty("epochDate",Calendar.getInstance().getTimeInMillis());

        Key report = datastore.put(e);

        Entity user = getById(userId);
        Entity crumb = getById(crumbId);

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("report");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(report);
        user.setProperty("report", crumbKey);
        datastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("report");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(report);
        crumb.setProperty("report", userKey);
        crumb.setProperty("nreReports", userKey.size());
        datastore.put(crumb);

    }

    /**
     *
     * @param userId
     * @param crumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addUserReportAsync(String userId, String crumbId, String type, String comments) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();

        Entity e = new Entity("Report");
        e.setProperty("reportedCrumb",KeyFactory.stringToKey(crumbId));
        e.setProperty("userReporting", KeyFactory.stringToKey(userId));
        e.setProperty("type", type);
        e.setProperty("comments", comments);

        Future<Key> report = asyncDatastore.put(e);

        Entity user = getByIdAsync(userId).get();
        Entity crumb = getByIdAsync(crumbId).get();

        ArrayList<Key> crumbKey = (ArrayList<Key>) user.getProperty("report");
        if (crumbKey == null) {
            crumbKey = new ArrayList<>();
        }
        crumbKey.add(report.get());
        user.setProperty("report", crumbKey);
        asyncDatastore.put(user);

        ArrayList<Key> userKey = (ArrayList<Key>) crumb.getProperty("report");
        if (userKey == null) {
            userKey = new ArrayList<>();
        }
        userKey.add(report.get());
        crumb.setProperty("report", userKey);
        crumb.setProperty("nreReports", userKey.size());
        asyncDatastore.put(crumb);

        fTrans.get().commitAsync();
    }

    /**
     *
     * @param creatorId
     * @param description
     * @param themes
     * @param place
     * @param googlePlaceId
     * @param lat
     * @param date
     * @param lng
     * @param fileId
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void insertCrumb(String creatorId, String description, ArrayList<String> themes, String place, String googlePlaceId, double lat, double lng, Date date, ArrayList<String> fileId, String thumbnail, double aspectRatio, boolean sensitiveContent) throws EntityNotFoundException {
        Entity e = new Entity("Crumb");
        Entity u = getById(creatorId);
        Key key;
        e.setProperty("description", description);
        e.setProperty("date", date);
        e.setProperty("sensitiveContent", sensitiveContent);

        if(thumbnail!= null) e.setProperty("thumbnail", thumbnail);

        ArrayList<Key> themesKeys = new ArrayList<>();

        for (int i = 0; i < themes.size(); ++i) {
            themesKeys.add(KeyFactory.stringToKey(themes.get(i)));
        }

        e.setProperty("theme", themesKeys);

        Key placeKey = this.repeatedPlace(googlePlaceId, lat, lng, place);
        e.setProperty("placeId", placeKey);

        e.setProperty("crumbFile", fileId);
        e.setProperty("created", KeyFactory.stringToKey(creatorId));
        e.setProperty("creationDate", Calendar.getInstance().getTime());
        e.setProperty("relevance", 0);
        e.setProperty("nreReports", 0);
        e.setProperty("nreThanks", 0);
        e.setProperty("nreShares", 0);
        e.setProperty("nreComments", 0);
        e.setProperty("nreViews", 0);

        key = datastore.put(e);

        ArrayList<Key> createdCrumbs = (ArrayList<Key>) u.getProperty("created");
        if (createdCrumbs == null) {
            createdCrumbs = new ArrayList<>();
        }
        createdCrumbs.add(key);
        u.setProperty("created", createdCrumbs);
        datastore.put(u);

        Entity theme;
        ArrayList<Key> crumbT;
        for (int i = 0; i < themes.size(); ++i) {
            Key themeKey = KeyFactory.stringToKey(themes.get(i));
            theme = getById(themeKey);
            crumbT = (ArrayList<Key>) theme.getProperty("crumbs");
            if (crumbT == null) {
                crumbT = new ArrayList<>();
            }
            crumbT.add(key);
            theme.setProperty("crumbs", crumbT);
            theme.setProperty("nreCrumbs", crumbT.size());

            datastore.put(theme);
        }

        ArrayList<Key> crumbP;
        Entity p = getById(placeKey);
        crumbP = (ArrayList<Key>) p.getProperty("crumbs");
        if (crumbP == null) {
            crumbP = new ArrayList<>();
        }
        crumbP.add(key);
        p.setProperty("crumbs", crumbP);

        p.setProperty("nreCrumbs", crumbP.size());

        datastore.put(p);


        FileQuery fi = new FileQuery();
        Key k = null;
        if(thumbnail != null){
            k = fi.insertFile(thumbnail, thumbnailBucket, false, key, null, 0);
        }
        ArrayList<Key> fileKeys = new ArrayList<>();
        for(int i = 0; i < fileId.size(); ++i){
            String file = fileId.get(i);
            fileKeys.add(fi.insertFile(file, bucket, (file.endsWith(".mp4")|| file.endsWith(".avi")|| file.endsWith(".mkv")|| file.endsWith(".3gp")|| file.endsWith(".ts")), key, k,aspectRatio));
        }
        e.setProperty("file", fileKeys);
        datastore.put(e);
    }

    public PaginationRet combinedSearchCrumbs(String initDate, String endDate, ArrayList<String> themeIds, ArrayList<String> placeIds, String userId, int page, int pageSize, String userKey) throws EntityNotFoundException, IOException{
        Query crumbQuery = new Query("Crumb");
        ArrayList<Filter> filters = new ArrayList<>();
        if(themeIds != null){
            ArrayList<Key> themeKeys = new ArrayList<>();
            for(int i = 0; i < themeIds.size(); ++i){
                themeKeys.add(KeyFactory.stringToKey(themeIds.get(i)));
            }
            Filter themes = new FilterPredicate("theme", FilterOperator.IN, themeKeys);
            filters.add(themes);
        }

        if(placeIds != null){
            ArrayList<Key> placeKeys = new ArrayList<>();
            for(int i = 0; i < placeIds.size(); ++i){
                placeKeys.add(KeyFactory.stringToKey(placeIds.get(i)));
            }
            Filter places = new FilterPredicate("placeId", FilterOperator.IN, placeKeys);
            filters.add(places);
        }
        if(initDate != null || endDate != null){
                Date date1 = new Date(Long.valueOf(initDate).longValue()*1000);
                Filter initDateFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, date1);

                Date date2 = new Date(Long.valueOf(endDate).longValue()*1000);
                Filter endDateFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, date2);

                CompositeFilter dateFilter = CompositeFilterOperator.and(initDateFilter, endDateFilter);
                filters.add(dateFilter);
            }

            if(filters.size() >= 2){
                CompositeFilter totalFilter = CompositeFilterOperator.or(filters);
                crumbQuery.setFilter(totalFilter).addSort("date",SortDirection.DESCENDING);
            }
            else if(filters.size() == 1){
                crumbQuery.setFilter(filters.get(0)).addSort("date",SortDirection.DESCENDING);
            }
            else{
                crumbQuery.addSort("date",SortDirection.DESCENDING);
            }

        PreparedQuery pq = datastore.prepare(crumbQuery);
        ListIterator<Entity> iter = pq.asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize)).listIterator();
        Utilities u = new Utilities();
        ArrayList<CrumbInfo> crumbInfo = new ArrayList<>();
        while(iter.hasNext()) {
            crumbInfo.add(u.entityToCrumb(iter.next(),userKey));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(crumbInfo);
        pr.setLastPage(((int) Math.ceil(pq.countEntities() / (double)pageSize)) - 1);
        return pr;


    }


    public List<Entity> combinedSearch(ArrayList<String> themeIds, ArrayList<String> placeIds, String description, int page, int pageSize){
        Query q = new Query("Crumb");
        ArrayList<Filter> filters = new ArrayList<>();
        if(themeIds != null){
            ArrayList<Key> themeKeys = new ArrayList<>();
            for(int i = 0; i < themeIds.size(); ++i){
                themeKeys.add(KeyFactory.stringToKey(themeIds.get(i)));
            }
            Filter themes = new FilterPredicate("theme", FilterOperator.IN, themeKeys);
            filters.add(themes);
        }
        if(placeIds != null){
            ArrayList<Key> placeKeys = new ArrayList<>();
            for(int i = 0; i < placeIds.size(); ++i){
                placeKeys.add(KeyFactory.stringToKey(placeIds.get(i)));
            }
            Filter places = new FilterPredicate("placeId", FilterOperator.IN, placeKeys);
            filters.add(places);
        }
        CompositeFilter total = CompositeFilterOperator.and(filters);
        q.setFilter(total).addSort("date", SortDirection.DESCENDING);
        return datastore.prepare(q).asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize));
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
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void insertCrumbAsync(String creatorId, String description, ArrayList<String> themes, String place, String googlePlaceId, double lat, double lng, Date date, ArrayList<String> fileId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();

        Future<Entity> feUser = getByIdAsync(creatorId);

        Entity e = new Entity("Crumb");
        e.setProperty("description", description);
        e.setProperty("date", date);

        ArrayList<Key> themesKeys = new ArrayList<>();

        for (int i = 0; i < themes.size(); ++i) {
            themesKeys.add(KeyFactory.stringToKey(themes.get(i)));
        }

        e.setProperty("theme", themesKeys);

        Key placeKey = this.repeatedPlaceAsync(googlePlaceId, lat, lng, place);
        e.setProperty("placeId", placeKey);

        e.setProperty("crumbFile", fileId);
        e.setProperty("created", KeyFactory.stringToKey(creatorId));
        e.setProperty("creationDate", Calendar.getInstance().getTime());
        e.setProperty("relevance", 0);
        e.setProperty("nreReports", 0);
        e.setProperty("nreThanks", 0);
        e.setProperty("nreShares", 0);
        e.setProperty("nreComments", 0);
        e.setProperty("nreViews", 0);

        Future<Key> fkKey = asyncDatastore.put(e);


        Entity user = feUser.get();
        ArrayList<Key> createdCrumbs = (ArrayList<Key>) user.getProperty("created");
        if (createdCrumbs == null) {
            createdCrumbs = new ArrayList<>();
        }
        Key key = fkKey.get();
        createdCrumbs.add(key);
        user.setProperty("created", createdCrumbs);
        asyncDatastore.put(user);

        ArrayList<Future<Entity>> feTheme = new ArrayList<>();
        Entity theme;
        ArrayList<Key> crumbT;
        for (int i = 0; i < themes.size(); ++i) {
            Key themeKey = KeyFactory.stringToKey(themes.get(i));
            feTheme.add(getByIdAsync(themeKey));
        }
        for (int i = 0; i < feTheme.size(); ++i) {
            theme = feTheme.get(i).get();
            crumbT = (ArrayList<Key>)theme.getProperty("crumbs");
            if (crumbT == null) {
                crumbT = new ArrayList<>();
            }
            crumbT.add(key);
            theme.setProperty("crumbs", crumbT);
            theme.setProperty("nreCrumbs", crumbT.size());

            asyncDatastore.put(theme);
        }

        ArrayList<Key> crumbP;
        Entity p = getByIdAsync(placeKey).get();
        crumbP = (ArrayList<Key>) p.getProperty("crumbs");
        if (crumbP == null) {
            crumbP = new ArrayList<>();
        }
        crumbP.add(key);
        p.setProperty("crumbs", crumbP);

        p.setProperty("nreCrumbs", crumbP.size());
        asyncDatastore.put(p);

        fTrans.get().commitAsync();
    }

    /**
     *
     * @param botLat
     * @param botLng
     * @param topLat
     * @param topLng
     * @return
     */
    public ArrayList<Key> getCrumbsByPlace(float botLat, float botLng, float topLat, float topLng) {

        Filter latG = new FilterPredicate("lat", FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
        Filter latL = new FilterPredicate("lat", FilterOperator.LESS_THAN_OR_EQUAL, topLat);

        Filter lngG = new FilterPredicate("lng", FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
        Filter lngL = new FilterPredicate("lng", FilterOperator.LESS_THAN_OR_EQUAL, topLng);

        CompositeFilter latFilter = CompositeFilterOperator.and(latG, latL);
        CompositeFilter lngFilter = CompositeFilterOperator.and(lngG, lngL);

        Query qLat = new Query("Place").setFilter(latFilter);
        Query qLng = new Query("Place").setFilter(lngFilter);

        PreparedQuery pqLat = datastore.prepare(qLat);
        PreparedQuery pqLng = datastore.prepare(qLng);

        List<Entity> searchResult = pqLat.asList(FetchOptions.Builder.withDefaults());
        searchResult.retainAll(pqLng.asList(FetchOptions.Builder.withDefaults()));

        ArrayList<Key> crumbsKey = new ArrayList<>();
        for (int i = 0; i < searchResult.size(); ++i) {
            ArrayList<Key> keys = (ArrayList<Key>) searchResult.get(i).getProperty("crumbs");
            if (keys != null) {
                crumbsKey.addAll(keys);
            }
        }

        Set<Key> remDupl = new HashSet<>();
        remDupl.addAll(crumbsKey);
        crumbsKey.clear();
        crumbsKey.addAll(remDupl);

        return crumbsKey;
    }

    /**
     *
     * @param botLat
     * @param botLng
     * @param topLat
     * @param topLng
     * @return
     */

    //REPASAR!
    public ArrayList<Key> getCrumbsByPlaceAsync(float botLat, float botLng, float topLat, float topLng) {

        Filter latG = new FilterPredicate("lat", FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
        Filter latL = new FilterPredicate("lat", FilterOperator.LESS_THAN_OR_EQUAL, topLat);

        Filter lngG = new FilterPredicate("lng", FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
        Filter lngL = new FilterPredicate("lng", FilterOperator.LESS_THAN_OR_EQUAL, topLng);

        CompositeFilter latFilter = CompositeFilterOperator.and(latG, latL);
        CompositeFilter lngFilter = CompositeFilterOperator.and(lngG, lngL);

        Query q = new Query("Place").setFilter(latFilter).setFilter(lngFilter);

        PreparedQuery pq = asyncDatastore.prepare(q);
        ArrayList<Key> crumbsKey = new ArrayList<>();
        for (Entity result : pq.asIterable()) {
            ArrayList<Key> keys = (ArrayList<Key>) result.getProperty("crumbs");
            if (keys != null) {
                crumbsKey.addAll(keys);
            }
        }

        Set<Key> remDupl = new HashSet<>();
        remDupl.addAll(crumbsKey);
        crumbsKey.clear();
        crumbsKey.addAll(remDupl);

        return crumbsKey;
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
     * @return
     */
    public PaginationRet getLocalCrumbs(float botLat, float botLng, float topLat, float topLng, String d1, String d2, int page, int pageSize, String userKey) throws EntityNotFoundException, IOException {
        Query crumbs = new Query("Crumb");

        ArrayList<Filter> filters = new ArrayList<>();
        ArrayList<Key> placesKey = new ArrayList<>();
        Query placesLat = new Query("Place").setKeysOnly();
        Query placesLng = new Query("Place").setKeysOnly();
        Filter latG = new FilterPredicate("lat", FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
        Filter latL = new FilterPredicate("lat", FilterOperator.LESS_THAN_OR_EQUAL, topLat);

        Filter lngG = new FilterPredicate("lng", FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
        Filter lngL = new FilterPredicate("lng", FilterOperator.LESS_THAN_OR_EQUAL, topLng);

        CompositeFilter latFilter = CompositeFilterOperator.and(latG, latL);
        CompositeFilter lngFilter = CompositeFilterOperator.and(lngG, lngL);

        placesLat.setFilter(latFilter);
        placesLng.setFilter(lngFilter);

        List<Entity> listLat = datastore.prepare(placesLat).asList(FetchOptions.Builder.withDefaults());
        List<Entity> listLng = datastore.prepare(placesLng).asList(FetchOptions.Builder.withDefaults());

        listLat.retainAll(listLng);
        ListIterator<Entity> it = listLat.listIterator();
        int limit = 0;
        while(it.hasNext()){
            placesKey.add(it.next().getKey());
            if(limit>=29){
                limit = 0;
                Filter placeFilter =  new FilterPredicate("placeId", FilterOperator.IN, placesKey);
                filters.add(placeFilter);
                placesKey.clear();
            }
            ++limit;
        }

        Filter placeFilter =  new FilterPredicate("placeId", FilterOperator.IN, placesKey);
        filters.add(placeFilter);

        if(filters.size() >= 2){
            CompositeFilter totalFilter = CompositeFilterOperator.and(filters);
            crumbs.setFilter(totalFilter).addSort("date",SortDirection.DESCENDING);
        }
        else if(filters.size() == 1){
            crumbs.setFilter(filters.get(0)).addSort("date",SortDirection.DESCENDING);
        }
        else{
            crumbs.addSort("date",SortDirection.DESCENDING);
        }
        PreparedQuery pq = datastore.prepare(crumbs);
        ListIterator<Entity> iter = pq.asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize)).listIterator();
        Utilities u = new Utilities();
        ArrayList<CrumbInfo> crumbInfo = new ArrayList<>();
        while(iter.hasNext()) {
            crumbInfo.add(u.entityToCrumb(iter.next(),userKey));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(crumbInfo);
        pr.setLastPage(((int) Math.ceil(pq.countEntities() / (double)pageSize)) - 1);
        return pr;
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
     * @return
     */
    public List<Entity> getLocalCrumbsAsync(float botLat, float botLng, float topLat, float topLng, Date initDate, Date endDate, int page, int pageSize) {

        Filter latG = new FilterPredicate("lat", FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
        Filter latL = new FilterPredicate("lat", FilterOperator.LESS_THAN_OR_EQUAL, topLat);

        Filter lngG = new FilterPredicate("lng", FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
        Filter lngL = new FilterPredicate("lng", FilterOperator.LESS_THAN_OR_EQUAL, topLng);

        Filter dateG = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, initDate);
        Filter dateL = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, endDate);

        CompositeFilter latFilter = CompositeFilterOperator.and(latG, latL);
        CompositeFilter lngFilter = CompositeFilterOperator.and(lngG, lngL);
        CompositeFilter dateFilter = CompositeFilterOperator.and(dateG, dateL);

        Query q = new Query("Place").setFilter(latFilter).setFilter(lngFilter).setFilter(dateFilter).addSort("date", SortDirection.DESCENDING);

        PreparedQuery pq = asyncDatastore.prepare(q);
        return pq.asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize));
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllCrumbsKey() {
        Query q = new Query("Crumb").setKeysOnly();

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllCrumbsKeyAsync() {
        Query q = new Query("Crumb").setKeysOnly();

        PreparedQuery pq = asyncDatastore.prepare(q);

        return pq;
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllCrumbs() {
        Query q = new Query("Crumb");

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }

    /**
     *
     * @return
     */
    public PreparedQuery getAllCrumbsAsync() {
        Query q = new Query("Crumb");

        PreparedQuery pq = asyncDatastore.prepare(q);

        return pq;
    }

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    public PaginationRet getLastCrumbs(float botLat, float botLng, float topLat, float topLng, String initDate, String endDate, int page, int pageSize, String userKey) throws EntityNotFoundException, IOException {

            Query crumbs = new Query("Crumb");

            ArrayList<Filter> filters = new ArrayList<>();

            if(initDate != null || endDate != null){
                Date date1 = new Date(Long.valueOf(initDate).longValue()*1000);
                Filter initDateFilter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, date1);

                Date date2 = new Date(Long.valueOf(endDate).longValue()*1000);
                Filter endDateFilter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, date2);

                CompositeFilter dateFilter = CompositeFilterOperator.and(initDateFilter, endDateFilter);
                filters.add(dateFilter);
            }


            if(botLat != 0.0f || botLng != 0.0f || topLat != 0.0f || topLng != 0.0f){
                Query placesLat = new Query("Place").setKeysOnly();
                Query placesLng = new Query("Place").setKeysOnly();
                Filter latG = new FilterPredicate("lat", FilterOperator.GREATER_THAN_OR_EQUAL, botLat);
                Filter latL = new FilterPredicate("lat", FilterOperator.LESS_THAN_OR_EQUAL, topLat);

                Filter lngG = new FilterPredicate("lng", FilterOperator.GREATER_THAN_OR_EQUAL, botLng);
                Filter lngL = new FilterPredicate("lng", FilterOperator.LESS_THAN_OR_EQUAL, topLng);

                CompositeFilter latFilter = CompositeFilterOperator.and(latG, latL);
                CompositeFilter lngFilter = CompositeFilterOperator.and(lngG, lngL);

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
                    if(limit>=9){
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
            }
            if(filters.size() >= 2){
                CompositeFilter totalFilter = CompositeFilterOperator.or(filters);
                crumbs.setFilter(totalFilter).addSort("date",SortDirection.DESCENDING);
            }
            else if(filters.size() == 1){
                crumbs.setFilter(filters.get(0)).addSort("date",SortDirection.DESCENDING);
            }
            else{
                crumbs.addSort("date",SortDirection.DESCENDING);
            }



        PreparedQuery pq = datastore.prepare(crumbs);
        ListIterator<Entity> iter = pq.asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize)).listIterator();
        Utilities u = new Utilities();
        ArrayList<CrumbInfo> crumbInfo = new ArrayList<>();
        while(iter.hasNext()) {
            crumbInfo.add(u.entityToCrumb(iter.next(),userKey));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(crumbInfo);
        pr.setLastPage(((int) Math.ceil(pq.countEntities() / (double)pageSize)) - 1);
        return pr;


    }

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    public List<Entity> getLastCrumbsAsync(int page, int pageSize) {
        Query q = new Query("Crumb");
        q.addSort("creationDate", SortDirection.DESCENDING);
        PreparedQuery pq = asyncDatastore.prepare(q);
        return pq.asList(FetchOptions.Builder.withLimit(pageSize).offset(page*pageSize));
    }

    /**
     *
     * @param e
     * @return
     */
    public Key putEntity(Entity e) {
        return datastore.put(e);
    }

    /**
     *
     * @param e
     * @return
     */
    public Future<Key> putEntityAsync(Entity e) {
        return asyncDatastore.put(e);
    }

    /**
     *
     * @param crumbId
     * @throws EntityNotFoundException
     */
    public void addView(String crumbId) throws EntityNotFoundException {
        Entity crumb = getById(crumbId);
        int nreViews = Integer.parseInt(crumb.getProperty("nreViews").toString());
        ++nreViews;
        crumb.setProperty("nreViews", nreViews);
        datastore.put(crumb);

    }

    /**
     *
     * @param crumbId
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void addViewAsync(String crumbId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Entity crumb = getByIdAsync(crumbId).get();
        int nreViews = Integer.parseInt(crumb.getProperty("nreViews").toString());
        ++nreViews;
        crumb.setProperty("nreViews", nreViews);
        asyncDatastore.put(crumb).get();

    }

    /**
     *
     * @param googlePlaceId
     * @param lat
     * @param lng
     * @param placeName
     * @return
     */
    public Key repeatedPlace(String googlePlaceId, double lat, double lng, String placeName) {
        Filter placeFilter = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, googlePlaceId);
        Query q = new Query("Place").setFilter(placeFilter);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            return result.getKey();
        }
        Entity newP = new Entity("Place");
        newP.setProperty("googlePlaceId", googlePlaceId);
        newP.setProperty("lat", lat);
        newP.setProperty("lng", lng);
        newP.setProperty("name", placeName);
        return datastore.put(newP);
    }

    /**
     *
     * @param googlePlaceId
     * @param lat
     * @param lng
     * @param placeName
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Key repeatedPlaceAsync(String googlePlaceId, double lat, double lng, String placeName) throws InterruptedException, ExecutionException {
        Filter placeFilter = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, googlePlaceId);
        Query q = new Query("Place").setFilter(placeFilter);
        PreparedQuery pq = asyncDatastore.prepare(q);
        for (Entity result : pq.asIterable()) {
            return result.getKey();
        }
        Entity newP = new Entity("Place");
        newP.setProperty("googlePlaceId", googlePlaceId);
        newP.setProperty("lat", lat);
        newP.setProperty("lng", lng);
        newP.setProperty("name", placeName);
        return asyncDatastore.put(newP).get();
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public PreparedQuery getCrumbsByDate(String d1, String d2) {


        if(d1 != null && d2 != null){
            Date date1 = new Date(Long.valueOf(d1).longValue()*1000);
            Filter date1Filter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, date1);

            Date date2 = new Date(Long.valueOf(d2).longValue()*1000);
            Filter date2Filter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, date2);

            CompositeFilter dateFilter = CompositeFilterOperator.and(date1Filter, date2Filter);

            Query q = new Query("Crumb").setFilter(dateFilter);
            return datastore.prepare(q);
        }
        else if(d1 != null && d2 == null){
            Date date1 = new Date(Long.valueOf(d1).longValue()*1000);
            Filter date1Filter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, date1);
            Query q = new Query("Crumb").setFilter(date1Filter);
            return datastore.prepare(q);
        }
        else if(d1 == null && d2 != null){
            Date date2 = new Date(Long.valueOf(d2).longValue()*1000);
            Filter date2Filter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, date2);
            Query q = new Query("Crumb").setFilter(date2Filter);
            return datastore.prepare(q);
        }
        else{
            Query q = new Query("Crumb");
            return datastore.prepare(q);
        }
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    //REPASAR!
    public PreparedQuery getCrumbsByDateAsync(Date d1, Date d2) {
        Filter date1Filter = new FilterPredicate("date", FilterOperator.GREATER_THAN_OR_EQUAL, d1);
        Filter date2Filter = new FilterPredicate("date", FilterOperator.LESS_THAN_OR_EQUAL, d2);
        Query q = new Query("Crumb").setFilter(date2Filter).setFilter(date1Filter);
        return asyncDatastore.prepare(q);
    }
    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public PreparedQuery getByGoogleId(String id) {
        Filter idFilter = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, id);
        Query q = new Query("Place").setFilter(idFilter);
        return datastore.prepare(q);
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public PreparedQuery getByGoogleIdAsync(String id) {
        Filter idFilter = new FilterPredicate("googlePlaceId", FilterOperator.EQUAL, id);
        Query q = new Query("Place").setFilter(idFilter);
        return asyncDatastore.prepare(q);
    }




    public void deleteCrumb(String crumbId) throws EntityNotFoundException {
        Entity crumb = this.getById(crumbId);
        Key creatorId = (Key) crumb.getProperty("created");
        Key placeId = (Key) crumb.getProperty("placeId");
        ArrayList<Key> themeKeys = (ArrayList<Key>) crumb.getProperty("theme");
        ArrayList<Key> commentKeys = (ArrayList<Key>) crumb.getProperty("comments");
        ArrayList<Key> thankedKeys = (ArrayList<Key>) crumb.getProperty("thanked");

        Entity creator = datastore.get(creatorId);
        ArrayList<Key> createdCrumbs = (ArrayList<Key>) creator.getProperty("created");
         if(createdCrumbs != null){
            createdCrumbs.remove(KeyFactory.stringToKey(crumbId));
            creator.setProperty("created", createdCrumbs);
            datastore.put(creator);
         }
        try{
        Entity place = datastore.get(placeId);
        ArrayList<Key> PCrumbs = (ArrayList<Key>) place.getProperty("crumbs");
        if(PCrumbs!= null){
            PCrumbs.remove(KeyFactory.stringToKey(crumbId));
            place.setProperty("crumbs", PCrumbs);
            place.setProperty("nreCrumbs", PCrumbs.size());
            datastore.put(place);
        }
        }
        catch(EntityNotFoundException e){

        }

        try{
            if(themeKeys != null){
        for(int i = 0; i < themeKeys.size(); ++i){
            Entity theme = datastore.get(themeKeys.get(i));
            ArrayList<Key> TCrumbs = (ArrayList<Key>) theme.getProperty("crumbs");
            if(TCrumbs!= null){
                TCrumbs.remove(KeyFactory.stringToKey(crumbId));
                theme.setProperty("crumbs", TCrumbs);
                theme.setProperty("nreCrumbs", TCrumbs.size());
                datastore.put(theme);
            }
        }
            }
        }
        catch(EntityNotFoundException e){

        }
        if(commentKeys != null){
            for(int i = 0; i < commentKeys.size(); ++i){
                datastore.delete(commentKeys.get(i));
            }
        }
        try{
        if(thankedKeys != null){
            for(int i = 0; i < thankedKeys.size(); ++i){
                Entity userThanks = datastore.get(thankedKeys.get(i));
                ArrayList<Key> TCrumbs = (ArrayList<Key>) userThanks.getProperty("thanks");
                if(TCrumbs!= null){
                    TCrumbs.remove(KeyFactory.stringToKey(crumbId));
                    userThanks.setProperty("thanks", TCrumbs);
                    datastore.put(userThanks);
                }
            }
        }
        }
        catch(EntityNotFoundException e){

        }
        Filter equal = new FilterPredicate("id",
                FilterOperator.EQUAL,
                crumb.getKey());

        Query q = new Query("Notification").setFilter(equal).setKeysOnly();

        PreparedQuery pq = datastore.prepare(q);
        ActivityQuery aq = new ActivityQuery();
        for(Entity result: pq.asIterable()){
            aq.removeNotification(KeyFactory.keyToString(result.getKey()));
        }
        datastore.delete(KeyFactory.stringToKey(crumbId));

    }

    public PreparedQuery multipleSearch(){
        Filter themeFilter = new FilterPredicate("theme", FilterOperator.EQUAL,KeyFactory.stringToKey( "ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgL_-lgsM"));
        Filter placeFilter = new FilterPredicate("placeId", FilterOperator.EQUAL, KeyFactory.stringToKey("ag5zfmNydW1iaXQtMTMwNHISCxIFUGxhY2UYgICAgLS9lAsM"));
        Query q = new Query("Crumb");
        CompositeFilter placeAndTheme = CompositeFilterOperator.and(themeFilter,placeFilter);
        q.setFilter(placeAndTheme).addSort("date",SortDirection.DESCENDING);
        return datastore.prepare(q);
    }

    public String getEmbedUrl(String crumbId) throws IOException, EntityNotFoundException{
        Entity c = super.getById(crumbId);
        String url = (String) c.getProperty("embedUrl");
        if(url == null){
            Storage storage = new Storage();
            storage.uploadHtml(crumbId);
            url = "http://storage.googleapis.com/crumbit/embeds/"+ crumbId + ".html";
            c.setProperty("embedUrl", url);
            super.put(c);
        }
        return url;
    }

}
