package com.crumbits.DB;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author Miquel Ferriol
 */
public final class ProfileQuery extends Queries {

    /**
     *
     * @param mail
     * @param userId
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void changeMailQuery(String mail, String userId) throws EntityNotFoundException {
        Entity u = (Entity) getById(userId);
        u.setProperty("userMail", mail);
        datastore.put(u);
    }
    
    /**
     *
     * @param mail
     * @param userId
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Future<Key> changeMailQueryAsync(String mail, String userId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feU = super.getByIdAsync(userId);
        Entity user = feU.get();
        user.setProperty("userMail", mail);
        return asyncDatastore.put(user);
    }

    /**
     *
     * @param password
     * @param userId
     * @throws com.google.appengine.api.datastore.EntityNotFoundException
     */
    public void changePasswordQuery(String password, String userId) throws EntityNotFoundException {
        Entity u = (Entity) getById(userId);
        u.setProperty("password", password);
        datastore.put(u);
    }
    
    /**
     *
     * @param mail
     * @param userId
     * @return
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Future<Key> changePasswordQueryAsync(String mail, String userId) throws EntityNotFoundException, InterruptedException, ExecutionException {
        Future<Entity> feU = super.getByIdAsync(userId);
        Entity user = feU.get();
        user.setProperty("password", mail);
        return asyncDatastore.put(user);
    }
    
}
