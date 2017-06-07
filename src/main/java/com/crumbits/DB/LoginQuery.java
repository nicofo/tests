package com.crumbits.DB;

import com.crumbits.Info.UserInfo;
import com.crumbits.Utilities.Utilities;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.code.facebookapi.FacebookException;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.zip.DataFormatException;
import javax.mail.internet.AddressException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Miquel Ferriol
 */
public final class LoginQuery extends Queries {
    
    private final String apiKey = "1316954461653611";
    private final String secretKey = "9f6708cefb08d128aeb043fd1d2424d4"; 

    /**
     *
     * @param mail
     * @return
     */
    public PreparedQuery mailQuery(String mail) {
        Filter equal = new FilterPredicate("userMail",
                FilterOperator.EQUAL,
                mail);

        Query q = new Query("User").setFilter(equal);

        PreparedQuery pq = datastore.prepare(q);
        return pq;
    }
    
    /**
     *
     * @param mail
     * @return
     */
    public PreparedQuery mailQueryAsync(String mail) {
        Filter equal = new FilterPredicate("userMail",
                FilterOperator.EQUAL,
                mail);

        Query q = new Query("User").setFilter(equal);

        return asyncDatastore.prepare(q);
    }

    /**
     *
     * @param userName
     * @return
     */
    public PreparedQuery userQuery(String userName) {
        Filter equal = new FilterPredicate("userName",
                FilterOperator.EQUAL,
                userName);

        Query q = new Query("User").setFilter(equal);

        PreparedQuery pq = datastore.prepare(q);
        return pq;
    }
    
    /**
     *
     * @param userName
     * @return
     */
    public PreparedQuery userQueryAsync(String userName) {
        Filter equal = new FilterPredicate("userName",
                FilterOperator.EQUAL,
                userName);

        Query q = new Query("User").setFilter(equal);

        return asyncDatastore.prepare(q);
    }

    /**
     *
     * @param userName
     * @param userMail
     * @param password
     * @param fbId
     * @param fbToken
     */
    public void insertUser(String userName, String userMail, String password, String fbId, String fbToken) {
        Entity e = new Entity("User");
        e.setProperty("userName", userName);
        e.setProperty("userMail", userMail);
        e.setProperty("password", password);
        if(fbId == null || fbToken == null){
            e.setProperty("fbRegister", false);
        }
        else{
            e.setProperty("fbId", fbId);
            e.setProperty("fbToken", fbToken);
            e.setProperty("fbRegister", true);
        }
        datastore.put(e);
    }
    
    /**
     *
     * @param userName
     * @param userMail
     * @param password
     * @param fbId
     * @param fbToken
     * @return
     */
    public Future<Key> insertUserAsync(String userName, String userMail, String password, String fbId, String fbToken) {
        Entity user = new Entity("User");
        user.setProperty("userName", userName);
        user.setProperty("userMail", userMail);
        user.setProperty("password", password);
        if(fbId == null || fbToken == null){
            user.setProperty("fbRegister", false);
        }
        else{
            user.setProperty("fbId", fbId);
            user.setProperty("fbToken", fbToken);
            user.setProperty("fbRegister", true);
        }
        return asyncDatastore.put(user);
    }
    
    /**
     *
     * @param userId
     * @param playerId
     * @throws EntityNotFoundException
     */
    public void insertPlayerId(String userId, String playerId) throws EntityNotFoundException{
        Entity user = getById(userId);
        ArrayList<String> playerIds = (ArrayList<String>)user.getProperty("playerIds");
        if (playerIds == null) playerIds = new ArrayList<>();
        if(!playerIds.contains(playerId)) playerIds.add(playerId);
        user.setProperty("playerIds", playerIds);
        datastore.put(user);
    }
    
    /**
     *
     * @param userId
     * @param playerId
     * @return
     * @throws EntityNotFoundException
     */
    public Future<Key> insertPlayerIdAsync(String userId, String playerId) throws EntityNotFoundException{
        Entity user = getById(userId);
        ArrayList<String> playerIds = (ArrayList<String>)user.getProperty("playerIds");
        if (playerIds == null) playerIds = new ArrayList<>();
        if(!playerIds.contains(playerId)) playerIds.add(playerId);
        user.setProperty("playerIds", playerIds);
        return asyncDatastore.put(user);
    }
    
    /**
     *
     * @param fbToken
     * @param fbId
     * @return
     * @throws FacebookException
     * @throws EntityNotFoundException
     */
    public UserInfo accessFB(String email, String fbId) throws FacebookException, EntityNotFoundException, IOException, AddressException, DataFormatException{
        Filter equal = new FilterPredicate("fbId",
                FilterOperator.EQUAL,
                fbId);

        Query q = new Query("User").setFilter(equal);
        
        PreparedQuery pq = datastore.prepare(q);
        
        int ent = pq.countEntities();

        if(ent == 1){
            Entity user = pq.asSingleEntity();
            if(user.getProperty("fbRegister") != null){
                if((boolean)user.getProperty("fbRegister")){
                Utilities u = new Utilities();
                return u.entityToUser(user);
                    
                }
            else{
                if(email.equals((String)user.getProperty("userMail"))){
                    Utilities u = new Utilities();
                    return u.entityToUser(user);
                }
                else{
                    throw new FacebookException(1,"mail and fbId doesn't match");
                }
            }
            } else{
                if(email.equals((String)user.getProperty("userMail"))){
                    Utilities u = new Utilities();
                    return u.entityToUser(user);
                }
                else{
                    throw new FacebookException(1,"mail and fbId doesn't match");
                }
            }
        }
        else{
            
            Filter mailEqual = new FilterPredicate("userMail",
                FilterOperator.EQUAL,
                email);
            Query q1 = new Query("User").setFilter(mailEqual);
            PreparedQuery pq1 = datastore.prepare(q1);
            
            Entity user = pq1.asSingleEntity();
            if(user == null) throw new AddressException("This email doesn't exists");
            if(user.getProperty("fbId") == null){
                user.setProperty("fbId",fbId);
                datastore.put(user);
                Utilities u = new Utilities();
                return u.entityToUser(user);
            }
            else{
                throw new DataFormatException("This username already exists");
            }
        }
    }
    
    /**
     *
     * @param fbToken
     * @param fbId
     * @return
     * @throws FacebookException
     * @throws EntityNotFoundException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    //REVISAR
    public UserInfo accessFBAsync(String fbToken, String fbId) throws FacebookException, EntityNotFoundException, InterruptedException, ExecutionException{
        Filter equal = new FilterPredicate("fbId",
                FilterOperator.EQUAL,
                fbId);

        Query q = new Query("User").setFilter(equal);
        
        PreparedQuery pq = asyncDatastore.prepare(q);
        
        int ent = pq.countEntities();

        if(ent == 1){
            Entity user = pq.asSingleEntity();
            if((boolean) user.getProperty("fbRegister")){
                Utilities u = new Utilities();
                return u.entityToUser(user);
            }
            else{
                FacebookClient.AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(apiKey, secretKey);
                DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbToken);
                User fbUser = facebookClient.fetchObject("me", User.class);
                if(fbUser.getEmail().equals((String)user.getProperty("userMail"))){
                    Utilities u = new Utilities();
                    return u.entityToUser(user);
                }
                else{
                    throw new FacebookException(1,"mail and fbId doesn't match");
                }
            }
        }
        else{
            FacebookClient.AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(apiKey, secretKey);
            DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbToken);
            
            ObjectMapper mapper = new ObjectMapper();
            
            User fbUser = facebookClient.fetchObject("me", User.class);
            
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            // do various things, perhaps:
            //String someJsonString = mapper.writeValueAsString(someClassInstance);
//SomeClass someClassInstance = mapper.readValue(someJsonString, SomeClass.class)
            String mail = fbUser.getEmail();
            Filter mailEqual = new FilterPredicate("userMail",
                FilterOperator.EQUAL,
                fbId);
            Query q1 = new Query("User").setFilter(mailEqual);
            PreparedQuery pq1 = asyncDatastore.prepare(q);
            
            Entity user = pq1.asSingleEntity();
            if(((String)user.getProperty("fbId")) == null){
                user.setProperty("fbId",fbId);
                user.setProperty("fbToken",fbToken);
                asyncDatastore.put(user).get();
                Utilities u = new Utilities();
                return u.entityToUser(user);
            }
            else{
                throw new FacebookException(1,"there is already another fb account");
            }
        }
    }
    
    
    public PreparedQuery indexTest(){
        Filter mailEqual = new FilterPredicate("userMail",
                FilterOperator.EQUAL,
                "mik_ferri@hotmail.com");
        Filter userEqual = new FilterPredicate("userName",
                FilterOperator.EQUAL,
                "LaiaxanIV");
        CompositeFilter heightRangeFilter =
    CompositeFilterOperator.and(mailEqual, userEqual);

            Query q1 = new Query("User").setFilter(heightRangeFilter);
            PreparedQuery pq1 = datastore.prepare(q1);
            return pq1;
    }
}
