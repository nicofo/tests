package com.crumbits;

import com.crumbits.Info.ThemeInfo;
import com.crumbits.Info.CrumbInfo;
import java.util.ArrayList;
import com.crumbits.Utilities.*;

import com.crumbits.DB.SocialQuery;
import com.crumbits.ReturnClasses.PaginationRet;
import com.crumbits.ReturnClasses.SearchIdReturn;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import java.io.IOException;

/**
 *
 * @author Miquel Ferriol
 */
public class Social {

    private SocialQuery DB = new SocialQuery();
    private String userKey;
    
    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Social(String token) throws IllegalAccessException{
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
     * @return
     */
    public String createTheme(String name){
        return KeyFactory.keyToString(DB.insertTheme(name)); 
    }

    /**
     *
     * @param theme
     * @param userKey
     * @return
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public ArrayList<ThemeInfo> themesAutocomplete(String theme) throws EntityNotFoundException {
        ArrayList<ThemeInfo> autoThemes = new ArrayList<>();
        PreparedQuery pq = DB.themesAutocomplete(theme);
        Utilities u = new Utilities();
        
        for (Entity result : pq.asIterable()) {
            if(result.getProperty("toRemove") == null){
                autoThemes.add(u.entityToTheme(result,userKey,0));
            }
        }
        
        return autoThemes;
    }

    /**
     *
     * @param themesNames
     * @return
     */
    public ArrayList<String> createMultipleThemes (ArrayList<String> themesNames){
        ArrayList<String> keyThemes = new ArrayList();
        for(int i = 0; i < themesNames.size(); ++i){
            keyThemes.add(createTheme(themesNames.get(i)));
        }
        return keyThemes;
    }
    /**
     *
     * @param theme
     * @return
     */
    public Object themesSearch(String theme) {
        ArrayList<String> searchTheme = new ArrayList<>();
        ArrayList<Key> searchThemeKey = new ArrayList<>();
        PreparedQuery pq = DB.getThemesStart(theme);
        

        for (Entity result : pq.asIterable()) {
            searchTheme.add((String)result.getProperty("name"));
            searchThemeKey.add(result.getKey());
        }
        
        Utilities u = new Utilities();
        if(searchTheme.isEmpty()){
            SearchIdReturn rc = new SearchIdReturn();
            rc.setSearchId(DB.insertSearch(null));
            return rc;
        }
        ArrayList<Integer> index = u.search(searchTheme, theme);
        if(index == null){
            SearchIdReturn rc = new SearchIdReturn();
            rc.setSearchId(DB.insertSearch(null));
            return rc;
        }
        
        ArrayList<Key> res = new ArrayList<>();
        for(int i = 0; i < index.size(); ++i){
            res.add(searchThemeKey.get(index.get(i)));
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
    public PaginationRet getThemesSearchResult(String searchId, int page, int pageSize) throws EntityNotFoundException {
        ArrayList<Key> searchIdResult = (ArrayList<Key>) DB.getById(searchId).getProperty("search");
        if(searchIdResult == null){
            PaginationRet pr = new PaginationRet();
            pr.setList(new ArrayList<>());
            pr.setLastPage(0);
            return pr;
        }
        ArrayList<ThemeInfo> searchResult = new ArrayList<>();
        Utilities u = new Utilities();
        for (int i = page*pageSize; i < page * pageSize + pageSize; ++i) {
            if(i < searchIdResult.size()) searchResult.add(u.entityToTheme(DB.getById(searchIdResult.get(i)),userKey,0));
        }

        PaginationRet pr = new PaginationRet();
        pr.setList(searchResult);
        pr.setLastPage(((int) Math.ceil(searchIdResult.size() / (double)pageSize)) - 1);
        return pr;
    }

    
    /**
     *
     * @param themeId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public ThemeInfo getThemeById(String themeId) throws EntityNotFoundException {
        Entity e = DB.getById(themeId);
        Utilities u = new Utilities();
        return u.entityToTheme(e,userKey,0);
    }

    /**
     *
     * @param themeId
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws java.io.IOException
     */
    public PaginationRet getThemeCrumbs(String themeId, int page, int pageSize) throws EntityNotFoundException, IOException {
        Entity e = DB.getById(themeId);
        ArrayList<Key> crumbKey = (ArrayList<Key>) e.getProperty("crumbs");
        if(crumbKey == null){
            
            PaginationRet pr = new PaginationRet();
            pr.setList(new ArrayList<>());
            pr.setLastPage(0);
            return pr;
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
     * @param themeId
     * @return
     * @throws EntityNotFoundException
     */
    public boolean getUserIsFollowingTheme( String themeId) throws EntityNotFoundException {
        Entity e = DB.getById(userKey);
        ArrayList<Key> follThemes = (ArrayList<Key>) e.getProperty("followedThemes");
        if(follThemes == null) return false;
        return follThemes.contains(KeyFactory.stringToKey(themeId));
    }

    /**
     *
     * @param userKey
     * @param themeId
     * @return
     * @throws EntityNotFoundException
     */
    public boolean userFollowTheme( String themeId) throws EntityNotFoundException {
        try {
            DB.addUserIsFollowing(userKey, themeId);

            return true;
        } catch (EntityNotFoundException e) {

            return false;
        }
    }

    /**
     *
     * @param userKey
     * @param themeId
     * @return
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public boolean userUnfollowTheme( String themeId) throws EntityNotFoundException {
            DB.delUserIsFollowing(userKey, themeId);
            return true;

    }
    
    /**
     *
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     */
    public ArrayList<ThemeInfo> getSuggestedThemes() throws EntityNotFoundException{
        ArrayList<ThemeInfo> pi = new ArrayList<>();
        PreparedQuery pq = DB.getSuggestedThemes();
        Utilities u = new Utilities();
        int i = 0;
        for(Entity result : pq.asIterable()){
            if(i < 15) pi.add(u.entityToTheme(result, userKey,1));
            ++i;
        }
        return pi;
    }
    
}
