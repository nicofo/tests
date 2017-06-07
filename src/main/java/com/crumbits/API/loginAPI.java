/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;


import com.crumbits.API.*;
import com.crumbits.ReturnClasses.Exists;
import com.crumbits.Entrada.EntradaLogin;
import com.crumbits.Info.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaProfile;
import com.crumbits.Error.Errors;
import com.crumbits.Error.Success;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.code.facebookapi.FacebookException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Miquel Ferriol
 */
@Api(name = "loginAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))

public class loginAPI {    
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "authenticateUser",path = "authenticateUser",httpMethod = ApiMethod.HttpMethod.POST)
    public Object authenticateUser(EntradaLogin en) throws NoSuchAlgorithmException {
        try{
            Login l = new Login();
            UserInfo u = l.authenticateUser(en.getUserVal(), en.getPassword());
            Success s = new Success();
            s.setRet(u);
            return s;
        }
        catch(LoginException e){
            return new Errors().loginError;
        }
        catch(NullPointerException e){
            return new Errors().userValError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "validateNewMail",path = "validateNewMail",httpMethod = ApiMethod.HttpMethod.POST)
    public Object validateNewMail(EntradaLogin en) {
        try{
            Login l = new Login();
            l.validateNewMail(en.getMail());
            Success s = new Success();
            return s;
            
        }
        catch(AddressException e){
            return new Errors().mailError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "validateMail",path = "validateMail",httpMethod = ApiMethod.HttpMethod.POST)
    public Object validateMail(EntradaLogin en) {
        try{
            Login l = new Login();
            l.validateMail(en.getMail());
            Success s = new Success();
            s.setRet(new Exists(true));
            return s;
        }
        catch(AddressException e){
            Success s = new Success();
            s.setRet(new Exists(false));
            return s;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "validateNewUsername",path = "validateNewUsername",httpMethod = ApiMethod.HttpMethod.POST)
    public Object validateNewUsername(EntradaLogin en) throws DataFormatException {
        try{
            Login l = new Login();
            l.validateNewUsername(en.getUserName());
            return new Success();
            
        }
        catch(AddressException e){
            return new Errors().userError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "validateNewPassword",path = "validateNewPassword",httpMethod = ApiMethod.HttpMethod.POST)
    public Object validateNewPassword(EntradaLogin en) {
        try{
            Login l = new Login();
            l.validateNewPassword(en.getPassword());
            return new Success();
            
        }
        catch(IllegalArgumentException e){
            return new Errors().passwordError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "createUser",path = "createUser",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createUser(EntradaLogin en) throws NoSuchAlgorithmException {
        try{
            Login l = new Login();
            l.createUser(en.getUserName(), en.getMail(), en.getPassword(),en.getFbId(),en.getFbToken());
            return new Success();
            
        }
        catch(DataFormatException e){
            return new Errors().userError;
        }
        catch(AddressException e){
            return new Errors().mailError;
        }
        catch(IllegalArgumentException e){
            return new Errors().passwordError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "sendPlayerId",path = "sendPlayerId",httpMethod = ApiMethod.HttpMethod.POST)
    public Object sendPlayerId(EntradaLogin en) {
        try{
            Login l = new Login();
            l.sendPlayerId(en.getUserId(),en.getPlayerId());
            
            return new Success();
            
        }
        catch(EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @return
     */
    @ApiMethod(name = "accessFB",path = "accessFB",httpMethod = ApiMethod.HttpMethod.POST)
    public Object accessFB(EntradaLogin en) throws EntityNotFoundException, IOException {
        try{
            Login l = new Login();
            
            
            Success s = new Success();
            s.setRet(l.accesFB(en.getMail(),en.getFbId()));
            return s;
        }
        catch (FacebookException e){
            return new Errors().facebookError;
        }
        catch (AddressException e){
            return new Errors().userValError;
        }
        catch (DataFormatException e){
            return new Errors().fbExistsError;
        }
    }
    
    @ApiMethod(name = "resetPassword", path = "resetPassword", httpMethod = ApiMethod.HttpMethod.POST)
    public Object resetPassword(EntradaProfile en, HttpServletRequest req) throws IOException, MessagingException, NoSuchAlgorithmException{
        try{
            Login l = new Login();
            
            Success s = new Success();
            l.resetPassword(en.getMail());
            return s;
            
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
}
