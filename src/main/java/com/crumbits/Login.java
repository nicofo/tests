package com.crumbits;

import com.crumbits.Info.UserInfo;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.mail.internet.AddressException;
import com.crumbits.DB.LoginQuery;
import com.crumbits.Utilities.Utilities;
import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.code.facebookapi.FacebookException;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.zip.DataFormatException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author Miquel Ferriol
 */
public class Login {

    private static final Pattern _VALID_EMAIL_ADDRESS_REGEX_
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern _PASSWORD_PATTERN_
            = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{6,20})", Pattern.CASE_INSENSITIVE);

    private LoginQuery DB = new LoginQuery();

    
    /**
     * Given the access data, it checks if the user exists in the DB. Then,
     * depending on what type of login data is typed (mail/username) checks if
     * the introduced password and the user password matches.
     *
     * @param userVal mail or user name
     * @param password
     * @return UserInfo information of the user
     * @throws LoginException if the userVal and the password don't match.
     * @throws java.security.NoSuchAlgorithmException
     */
    public UserInfo authenticateUser(String userVal, String password) throws LoginException, NoSuchAlgorithmException, TooManyResultsException {
        UserInfo ui;
        Matcher matcher = _VALID_EMAIL_ADDRESS_REGEX_.matcher(userVal);
        if (!matcher.find()) {
            Entity e = DB.userQuery(userVal).asSingleEntity();
            String userMail = (String) e.getProperty("userMail");
            Utilities u = new Utilities();
            if (!e.getProperty("password").equals(u.sha_512(password))) {
                throw new LoginException("Username and password don't match");
            }
            
            ui = new UserInfo();
            
            Date date = Calendar.getInstance().getTime();
        
            Calendar cal = Calendar.getInstance(); 
            cal.setTime(date); 
            cal.add(Calendar.HOUR_OF_DAY, 1440); 
            
            ui.setAccesToken(u.generateToken(KeyFactory.keyToString(e.getKey()),cal.getTime()));
            ui.setUserName(userVal);
            ui.setMail(userMail);
            ui.setUserId(KeyFactory.keyToString(e.getKey()));
            ui.setTokenExpirationTime(cal.getTime().getTime()/1000);
        }
        else {

            Entity e1 = DB.mailQuery(userVal).asSingleEntity();
            String userName = (String) e1.getProperty("userName");
            Utilities u = new Utilities();
            if (!e1.getProperty("password").equals(u.sha_512(password))) {
                throw new LoginException("Mail and password don't match");
            }
            Date date = Calendar.getInstance().getTime();
        
            Calendar cal = Calendar.getInstance(); 
            cal.setTime(date); 
            cal.add(Calendar.HOUR_OF_DAY, 1440); 
        
            ui = new UserInfo();
            ui.setAccesToken(u.generateToken(KeyFactory.keyToString(e1.getKey()),cal.getTime()));
            ui.setUserName(userName);
            ui.setMail(userVal);
            ui.setUserId(KeyFactory.keyToString(e1.getKey()));
            
            ui.setTokenExpirationTime(cal.getTime().getTime()/1000);

        }
        return ui;
    }
    
    

    /**
     * Checks if the given mail has the correct format and if it doesn't exist in the DB.
     *
     * @param mail
     * @throws AddressException if the mail is not correct
     */
    public void validateNewMail(String mail) throws AddressException {

        Matcher matcher = _VALID_EMAIL_ADDRESS_REGEX_.matcher(mail);

        if (!matcher.find()) {
            throw new AddressException("Incorrect email address");
        }

        if (!(DB.mailQuery(mail).asSingleEntity() == null)) {
            throw new AddressException("This email already exists");
        }
    }
    
    /**
     * Checks if the given mail has the correct format and if it exists in the DB.
     *
     * @param mail 
     * @throws AddressException if the mail is not correct
     */
    public void validateMail(String mail) throws AddressException {

        Matcher matcher = _VALID_EMAIL_ADDRESS_REGEX_.matcher(mail);

        if (!matcher.find()) {
            throw new AddressException("Incorrect email address");
        }

        if (DB.mailQuery(mail).asSingleEntity() == null) {
            throw new AddressException("This email doesn't exists");
        }
    }

    /**
     * Validates if the given user name doesn't exist in the DB.
     *
     * @param userName
     * @throws AddressException if the user name already exists.
     * @throws java.util.zip.DataFormatException
     */
    public void validateNewUsername(String userName) throws AddressException, DataFormatException  {
        if (!(DB.userQuery(userName).asSingleEntity() == null)) {
            throw new DataFormatException("This username already exists");
        }
    }

    /**
     * Validates if the given password has the minimum security requirements.
     *
     * @param password
     * @throws IllegalArgumentException if the password isn't strong.
     */
    public void validateNewPassword(String password) throws IllegalArgumentException{
        Matcher matcher = _PASSWORD_PATTERN_.matcher(password);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Password not strong");
        }
    }

    /**
     * Given all the necessary data it creates a user in the BD.
     *
     * @param userName
     * @param mail
     * @param password
     * @param fbId
     * @param fbToken
     * @throws AddressException if the userName already exists or mail is
     * incorrect
     * @throws java.util.zip.DataFormatException
     * @throws java.security.NoSuchAlgorithmException
     */
    public void createUser(String userName, String mail, String password, String fbId, String fbToken) throws AddressException,IllegalArgumentException, NoSuchAlgorithmException, DataFormatException  {
        validateNewUsername(userName);
        validateNewMail(mail);
        validateNewPassword(password);
        Utilities u = new Utilities();
        DB.insertUser(userName, mail, u.sha_512(password), fbId, fbToken);
    }
    
    /**
     *
     * @param userId
     * @param playerId
     * @throws EntityNotFoundException
     */
    public void sendPlayerId(String userId, String playerId) throws EntityNotFoundException{
        DB.insertPlayerId(userId,playerId);
    }
    
    /**
     *
     * @param fbToken
     * @param fbId
     * @return
     * @throws FacebookException
     * @throws EntityNotFoundException
     */
    public UserInfo accesFB(String mail, String fbId) throws FacebookException, EntityNotFoundException, IOException, AddressException, DataFormatException {
        UserInfo ui = DB.accessFB(mail, fbId);
        Utilities u = new Utilities(); Calendar cal = Calendar.getInstance(); 
        Date date = Calendar.getInstance().getTime();
        cal.setTime(date); 
        cal.add(Calendar.HOUR_OF_DAY, 1440);
        ui.setAccesToken(u.generateToken(ui.getUserId(),cal.getTime()));
        ui.setTokenExpirationTime(cal.getTime().getTime()/1000);
        return ui;
        
    }
    
    public void resetPassword(String userMail) throws EntityNotFoundException, IOException, MessagingException, NoSuchAlgorithmException {
        String host = "smtp.google.com";
        Entity user = DB.mailQuery(userMail).asSingleEntity();
        String to = (String) user.getProperty("userMail");
        String from = "miquelferriol@gmail.com";
        String subject = "Reset your Crumbit password";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String pwd = RandomStringUtils.random( 15, characters );
        Utilities u = new Utilities();
        user.setProperty("password", u.sha_512(pwd));
        DB.put(user);
        String messageText = "Your new password is: " + pwd;
        boolean sessionDebug = false;

        Properties props = System.getProperties();
        props.put("mail.host", host);
        props.put("mail.transport.protocol", "smtp");
        Session mailSession = Session.getDefaultInstance(props, null);

        mailSession.setDebug(sessionDebug);
        Message msg = new MimeMessage(mailSession);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(messageText);
        Transport.send(msg);
    }
}
