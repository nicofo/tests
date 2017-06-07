package com.crumbits.DB;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.AsyncDatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import java.util.concurrent.Future;


/**
 *
 * @author Miquel Ferriol
 */
public class Queries {

    /**
     *
     */
    protected static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    /**
     *
     */
    protected static AsyncDatastoreService  asyncDatastore = DatastoreServiceFactory.getAsyncDatastoreService();

    /**
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public Entity getById(String id) throws EntityNotFoundException {
        return datastore.get(KeyFactory.stringToKey(id));
    }
    
    /**
     *
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public Future<Entity> getByIdAsync(String id) throws EntityNotFoundException {
        return asyncDatastore.get(KeyFactory.stringToKey(id));
    }

    /**
     *
     * @param key
     * @return
     * @throws EntityNotFoundException
     */
    public Entity getById(Key key) throws EntityNotFoundException {
        return datastore.get(key);
    }
    
    /**
     *
     * @param key
     * @return
     * @throws EntityNotFoundException
     */
    public Future<Entity> getByIdAsync(Key key) throws EntityNotFoundException {
        return asyncDatastore.get(key);
    }
    
    /**
     *
     * @param e
     * @return
     */
    public Key put(Entity e){
        return datastore.put(e);
    }
    
    /**
     *
     * @param e
     * @return
     */
    public Future<Key> putAsync(Entity e){
        return asyncDatastore.put(e);
    }
    
    /**
     *
     * @param k
     */
    public void delete(Key k){
        datastore.delete(k);
    }
    
    /**
     *
     * @param k
     * @return
     */
    public Future<Void> deleteAsync(Key k){
        return asyncDatastore.delete(k);
    }
    
}
