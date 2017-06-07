package com.crumbits;

import com.crumbits.Info.UserInfo;
import com.crumbits.Info.CrumbInfo;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.mail.internet.AddressException;
import com.crumbits.Utilities.*;
import com.crumbits.DB.ProfileQuery;
import com.crumbits.Info.AccessToken;
import com.crumbits.Info.PlaceInfo;
import com.crumbits.Info.ThemeInfo;
import com.crumbits.ReturnClasses.PaginationRet;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailServiceFactory;
import com.google.code.facebookapi.Attachment;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author Miquel Ferriol
 */
public class Profile {

    private static final Pattern _VALID_EMAIL_ADDRESS_REGEX_
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern _PASSWORD_PATTERN_
            = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{6,20})", Pattern.CASE_INSENSITIVE);

    private ProfileQuery DB = new ProfileQuery();
    private String userKey;

    /**
     *
     * @param token
     * @throws IllegalAccessException
     */
    public Profile(String token) throws IllegalAccessException{
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
     * Returns the userData
     *
     * @param userKey
     * @return UserInfo contains the user data
     * @throws EntityNotFoundException no exists a user with the given userKey in
     * the DB
     */
    public UserInfo getUserData() throws EntityNotFoundException {
        Entity user = DB.getById(userKey);
        Utilities u = new Utilities();
        return u.entityToUser(user);

    }

    /**
     * Checks if the given mail has the correct format. Then it changes the mail
     * of the given user.
     *
     * @param mail
     * @param userKey
     * @throws AddressException if mail is not correct
     * @throws EntityNotFoundException no exists a user with the given userKey in
     * the DB
     */
    public void changeUserMail(String mail) throws AddressException, EntityNotFoundException {

        Matcher matcher = _VALID_EMAIL_ADDRESS_REGEX_.matcher(mail);

        if (!matcher.find()) {
            throw new AddressException("Incorrect email address");
        }

        DB.changeMailQuery(mail, userKey);

    }

    /**
     * Checks if the given password has the minimum security requirements. Then
     * it changes the password of the given user.
     *
     * @param password
     * @param userKey
     * @throws EntityNotFoundException no exists a user with the given userKey in
     * the DB
     * @throws java.security.NoSuchAlgorithmException
     */
    public void changeUserPassword(String password) throws EntityNotFoundException, NoSuchAlgorithmException {
        Matcher matcher = _PASSWORD_PATTERN_.matcher(password);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Password not strong");
        }
        Utilities u = new Utilities();
        DB.changePasswordQuery(u.sha_512(password), userKey);

    }
    
    /**
     *
     * @param oldToken
     * @return
     * @throws IllegalAccessException
     */
    public AccessToken refreshToken(String oldToken) throws IllegalAccessException{
        Utilities u = new Utilities();
        AccessToken ac = new AccessToken();
        if(!u.verifyToken(oldToken)) throw new IllegalAccessException();
        Date date = Calendar.getInstance().getTime();
        
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(date); 
        cal.add(Calendar.HOUR_OF_DAY, 1440);
        ac.setAccessToken(u.generateToken(u.decodeToken(oldToken),cal.getTime()));
        ac.setTokenExpirationTime(cal.getTime().getTime()/1000);
        return ac;
    }

    /**
     * Return all the created crumbs of the given user sorted by date
     *
     * @param userKey
     * @return crumbs Array List of crumbs that contains the sorted crumbs.
     * @throws EntityNotFoundException no exists a user with the given userKey in
     * the DB
     * @throws java.io.IOException
     */
    public PaginationRet getUserCrumbs( int page, int pageSize) throws EntityNotFoundException, IOException {
        Utilities ut = new Utilities();
        ArrayList<Key> keys = (ArrayList<Key>) DB.getById(userKey).getProperty("created");
        ArrayList<CrumbInfo> crumbs = new ArrayList<>();
        if (keys == null) {
            PaginationRet pr = new PaginationRet();
            pr.setList(crumbs);
            pr.setLastPage(0);
            return pr;
        }

        for (int i = page*pageSize; i < page*pageSize+pageSize && i < keys.size(); ++i) {
            Entity c = DB.getById(keys.get(keys.size() - 1 - i));
            crumbs.add(ut.entityToCrumb(c,userKey));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(crumbs);
        pr.setLastPage(((int) Math.ceil(keys.size() / (double)pageSize)) - 1);
        return pr;
    }

    /**
     * Return all the thanked crumbs of the given user sorted by date
     *
     * @param userKey
     * @return crumbs Array List of crumbs that contains the sorted crumbs
     * @throws EntityNotFoundException no exists a user with the given userKey in
     * the DB
     * @throws java.io.IOException
     */
    public PaginationRet getUserThanks( int page, int pageSize) throws EntityNotFoundException, IOException {
        ArrayList<Key> keys = (ArrayList<Key>) DB.getById(userKey).getProperty("thanks");
        if (keys == null) {
            keys = new ArrayList<>();
        }
        ArrayList<CrumbInfo> crumbs = new ArrayList<>();
        Utilities u = new Utilities();
        for (int i = page*pageSize; i < page*pageSize+pageSize && i < keys.size(); ++i) {
            Entity c = DB.getById(keys.get(keys.size() - 1 - i));
            crumbs.add(u.entityToCrumb(c,userKey));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(crumbs);
        pr.setLastPage(((int) Math.ceil(keys.size() / (double)pageSize)) - 1);
        return pr;
    }
    
    /**
     *
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public PaginationRet getUserFollowingThemes( int page, int pageSize) throws EntityNotFoundException, IOException {
        ArrayList<Key> keys = (ArrayList<Key>) DB.getById(userKey).getProperty("followedThemes");
        if (keys == null) {
            keys = new ArrayList<>();
        }
        ArrayList<ThemeInfo> themes = new ArrayList<>();
        Utilities u = new Utilities();
        for (int i = page*pageSize; i < page*pageSize+pageSize && i < keys.size(); ++i) {
            Entity c = DB.getById(keys.get(i));
            themes.add(u.entityToTheme(c,userKey,0));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(themes);
        pr.setLastPage(((int) Math.ceil(keys.size() / (double)pageSize)) - 1);
        return pr;
    }
    
    /**
     *
     * @param userKey
     * @return
     * @throws EntityNotFoundException
     * @throws IOException
     */
    public PaginationRet getUserFollowingPlaces( int page, int pageSize) throws EntityNotFoundException, IOException {
        ArrayList<Key> keys = (ArrayList<Key>) DB.getById(userKey).getProperty("followedPlaces");
        if (keys == null) {
            keys = new ArrayList<>();
        }
        ArrayList<PlaceInfo> places = new ArrayList<>();
        Utilities u = new Utilities();
        for (int i = page*pageSize; i < page*pageSize+pageSize && i < keys.size(); ++i) {
            Entity c = DB.getById(keys.get(i));
            places.add(u.entityToPlace(c,userKey,0));
        }
        PaginationRet pr = new PaginationRet();
        pr.setList(places);
        pr.setLastPage(((int) Math.ceil(keys.size() / (double)pageSize)) - 1);
        return pr;
    }
    
    
}
