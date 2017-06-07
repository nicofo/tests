/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.Info.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaProfile;
import com.crumbits.Error.Errors;
import com.crumbits.Error.Success;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Miquel Ferriol Galmé
 */
    @Api(name = "profileAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))
    
public class profileAPI {
        
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getUserData",path = "getUserData",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserData(EntradaProfile en, HttpServletRequest req) {
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            Success s = new Success();
            s.setRet(p.getUserData());
            return s;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
        catch (Exception e){
            return new Errors().otherError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "refreshToken",path = "refreshToken",httpMethod = ApiMethod.HttpMethod.POST)
    public Object refreshToken(EntradaProfile en, HttpServletRequest req) {
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            AccessToken ac = p.refreshToken((String)req.getHeader("accessToken"));
            Success s = new Success();
            s.setRet(ac);
            return s;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (Exception e){
            return new Errors().otherError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "changeUserMail",path = "changeUserMail",httpMethod = ApiMethod.HttpMethod.POST)
    public Object changeUserMail(EntradaProfile en, HttpServletRequest req){
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            p.changeUserMail(en.getMail());
            
            return new Success();
            
        }
        catch(AddressException ae){
            return new Errors().mailError;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "changeUserPassword",path = "changeUserPassword", httpMethod = ApiMethod.HttpMethod.POST)
    public Object changeUserPassword(EntradaProfile en, HttpServletRequest req) throws NoSuchAlgorithmException{
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            p.changeUserPassword(en.getPassword());
            //return new ErrorInfo("success",null,null,0);ErrorInfo err = new ErrorInfo();
        
            return new Success();
        }            
        catch(IllegalArgumentException ae){
            return new Errors().passwordError;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getUserCrumbs", path = "getUserCrumbs", httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserCrumbs(EntradaProfile en, HttpServletRequest req) throws IOException{
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getUserCrumbs(en.getPage(),en.getPageSize()));
            return s;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getUserThanks", path = "getUserThanks", httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserThanks(EntradaProfile en, HttpServletRequest req) throws IOException{
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getUserThanks(en.getPage(),en.getPageSize()));
            return s;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getUserFollowingThemes", path = "getUserFollowingThemes", httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserFollowingThemes(EntradaProfile en, HttpServletRequest req) throws IOException{
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getUserFollowingThemes(en.getPage(),en.getPageSize()));
            return s;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getUserFollowingPlaces", path = "getUserFollowingPlaces", httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserFollowingPlaces(EntradaProfile en, HttpServletRequest req) throws IOException{
        try{
            Profile p = new Profile((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getUserFollowingPlaces(en.getPage(),en.getPageSize()));
            return s;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    
    
    
}
