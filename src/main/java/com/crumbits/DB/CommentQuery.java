
package com.crumbits.DB;

import static com.crumbits.DB.Queries.asyncDatastore;
import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Miquel Ferriol
 */
public class CommentQuery extends Queries{
    
    /**
     *
     * @param comment
     * @param userId
     * @param crumbId
     * @param date
     * @throws EntityNotFoundException
     */
    public ArrayList<Key> insertComment(String comment, String userId, String crumbId, Date date) throws EntityNotFoundException {
        Entity e = new Entity("Comment");
        Entity user = getById(userId);
        Entity crumb = getById(crumbId);
        Key key;

        e.setProperty("comment", comment);
        e.setProperty("date", date);
        e.setProperty("user", KeyFactory.stringToKey(userId));
        e.setProperty("crumb", KeyFactory.stringToKey(crumbId));
        key = datastore.put(e);

        ArrayList<Key> commented = (ArrayList<Key>) user.getProperty("comments");
        if (commented == null) {
            commented = new ArrayList<>();
        }
        commented.add(key);
        user.setProperty("comments", commented);
        datastore.put(user);

        ArrayList<Key> comments = (ArrayList<Key>) crumb.getProperty("comments");
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(key);
        int nreComments = Integer.parseInt(crumb.getProperty("nreComments").toString());
        ++nreComments;
        crumb.setProperty("comments", comments);
        crumb.setProperty("nreComments", nreComments);
        datastore.put(crumb);
        return comments;
    }
    
    /**
     *
     * @param comment
     * @param userId
     * @param crumbId
     * @param date
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public void insertCommentAsync(String comment, String userId, String crumbId, Date date) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Entity e = new Entity("Comment");        
        
        Future<Entity> feUser = super.getByIdAsync(userId);
        Future<Entity> feCrumb = super.getByIdAsync(crumbId);
        
        Future<Transaction> fTrans = asyncDatastore.beginTransaction();
        
        Entity user = feUser.get();
        Entity crumb = feCrumb.get();
        
        e.setProperty("comment", comment);
        e.setProperty("date", date);
        e.setProperty("user", KeyFactory.stringToKey(userId));
        e.setProperty("crumb", KeyFactory.stringToKey(crumbId));
        
        Future<Key> feKey = asyncDatastore.put(e);

        ArrayList<Key> commented = (ArrayList<Key>) user.getProperty("comments");
        if (commented == null) {
            commented = new ArrayList<>();
        }
        
        user.setProperty("comments", commented);
        Key key = feKey.get();
        commented.add(key);
        asyncDatastore.put(user);

        ArrayList<Key> comments = (ArrayList<Key>) crumb.getProperty("comments");
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(key);
        int nreComments = Integer.parseInt(crumb.getProperty("nreComments").toString());
        ++nreComments;
        crumb.setProperty("comments", comments);
        crumb.setProperty("nreComments", nreComments);
        asyncDatastore.put(crumb);
        
        fTrans.get().commitAsync();
        
    }

}
