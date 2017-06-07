package com.crumbits;

import java.util.ArrayList;
import java.util.Date;

import com.crumbits.DB.CommentQuery;
import com.crumbits.Info.CommentInfo;
import com.crumbits.ReturnClasses.PaginationRet;
import com.crumbits.Utilities.Utilities;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import java.io.IOException;
import java.util.Calendar;

/**
 *
 * @author Miquel Ferriol
 */
public class Comment {

    CommentQuery DB = new CommentQuery();
    private String userKey;

    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Comment(String token) throws IllegalAccessException{
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
     * @param userKey
     * @param crumbId
     * @param comment
     * @param date
     * @throws EntityNotFoundException
     */
    public PaginationRet createComment( String crumbId, String comment, Date date, int page, int pageSize) throws EntityNotFoundException, IOException {
        ArrayList <Key> commentKey = DB.insertComment(comment, userKey, crumbId, date);
        ArrayList <CommentInfo> commentInfo = new ArrayList<>();
        Utilities u = new Utilities();
        
        if(pageSize != 0){
            for(int i = commentKey.size() -1 - page*pageSize; i > commentKey.size() -1 - page*pageSize - pageSize; --i){
                if( i >= 0) commentInfo.add(u.entityToComment(DB.getById(commentKey.get(i))));
            }
        }
        
        Activity a = new Activity();
        a.createNotificationCrumb("Comentario!","Un usuario ha comentado tu crumb!",date,"crumb", crumbId);
        
        
        PaginationRet pr = new PaginationRet();
        pr.setList(commentInfo);
        pr.setLastPage(((int) Math.ceil(commentKey.size() / (double)pageSize)) - 1);
        return pr;
    }

    /**
     *
     * @param crumbId
     * @param page
     * @param pageSize
     * @return
     * @throws EntityNotFoundException
     */
    public PaginationRet getAllCommentsByCrumbId(String crumbId, int page, int pageSize, boolean ascending) throws EntityNotFoundException {
        Utilities u = new Utilities();
        ArrayList<Key> searchIdResult = (ArrayList<Key>) DB.getById(crumbId).getProperty("comments");
        ArrayList<CommentInfo> searchResult = new ArrayList<>();
        
        if(searchIdResult == null){
            PaginationRet pr = new PaginationRet();
            pr.setList(searchResult);
            pr.setLastPage(0);
            return pr;
        }
        if(ascending){
            for (int i = page*pageSize; i < page * pageSize + pageSize && i < searchIdResult.size(); ++i) {
                searchResult.add(u.entityToComment(DB.getById(searchIdResult.get(i))));
            }
        PaginationRet pr = new PaginationRet();
        pr.setList(searchResult);
        pr.setLastPage(((int) Math.ceil(searchIdResult.size() / (double)pageSize)) - 1);
        return pr;
        }
        
        for (int i = searchIdResult.size()-1 - page*pageSize; i > searchIdResult.size()-1 - page*pageSize - pageSize; --i) {
            if( i >= 0) searchResult.add(u.entityToComment(DB.getById(searchIdResult.get(i))));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(searchResult);
        pr.setLastPage(((int) Math.ceil(searchIdResult.size() / (double)pageSize)) - 1);
        return pr;
    }
    
    public ArrayList<CommentInfo> getAllCrumbComments(String crumbId, boolean ascending) throws EntityNotFoundException {
        Utilities u = new Utilities();
        ArrayList<Key> searchIdResult = (ArrayList<Key>) DB.getById(crumbId).getProperty("comments");
        ArrayList<CommentInfo> searchResult = new ArrayList<>();
        
        if(searchIdResult == null){
            return searchResult;
        }
        if(ascending){
            for (int i = 0; i < searchIdResult.size()-1; ++i) {
                searchResult.add(u.entityToComment(DB.getById(searchIdResult.get(i))));
            }
            return searchResult;
        }
        
        for (int i = searchIdResult.size(); i >= 0; --i) {
            searchResult.add(u.entityToComment(DB.getById(searchIdResult.get(i))));
        }
        return searchResult;
    }
}
