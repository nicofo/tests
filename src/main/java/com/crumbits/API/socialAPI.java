/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaSocial;
import com.crumbits.Error.Errors;
import com.crumbits.Error.Success;
import com.crumbits.ReturnClasses.IdReturn;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Miquel Ferriol
 */
@Api(name = "socialAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))

public class socialAPI {
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "themesAutocomplete",path = "themesAutocomplete",httpMethod = ApiMethod.HttpMethod.POST)
    public Object themesAutocomplete(EntradaSocial en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.themesAutocomplete(en.getThemeName()));
            return su;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "themesSearch",path = "themesSearch",httpMethod = ApiMethod.HttpMethod.POST)
    public Object themesSearch(EntradaSocial en, HttpServletRequest req) {
        try{
            Social s = new Social((String)req.getHeader("accessToken"));
            Success su = new Success();
            su.setRet(s.themesSearch(en.getThemeName()));
            return su;
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getThemesSearchResult",path = "getThemesSearchResult",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getThemesSearchResult(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.getThemesSearchResult(en.getSearchId(), en.getPage(), en.getPageSize()));
            return su;
            
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
    @ApiMethod(name = "getThemeById",path = "getThemeById",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getThemeById(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.getThemeById(en.getThemeId()));
            return su;
            
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
    @ApiMethod(name = "getThemeCrumbs",path = "getThemeCrumbs",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getThemeCrumbs(EntradaSocial en, HttpServletRequest req) throws IOException {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.getThemeCrumbs(en.getThemeId(),en.getPage(), en.getPageSize()));
            return su;
            
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
    @ApiMethod(name = "getUserIsFollowingTheme",path = "getUserIsFollowingTheme",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserIsFollowingTheme(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.getUserIsFollowingTheme( en.getThemeId()));
            return su;
            
            
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
    @ApiMethod(name = "userFollowTheme",path = "userFollowTheme",httpMethod = ApiMethod.HttpMethod.POST)
    public Object userFollowTheme(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.userFollowTheme( en.getThemeId()));
            return su;
            
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
    @ApiMethod(name = "userUnfollowTheme",path = "userUnfollowTheme",httpMethod = ApiMethod.HttpMethod.POST)
    public Object userUnfollowTheme(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.userUnfollowTheme( en.getThemeId()));
            return su;
            
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
    @ApiMethod(name = "createTheme",path = "createTheme",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createTheme(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            
            IdReturn id = new IdReturn();
            id.setThemeId(s.createTheme(en.getThemeName()));
            su.setRet(id);
            return su;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "createMultipleThemes",path = "createMultipleThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createMultipleThemes(EntradaSocial en, HttpServletRequest req) {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.createMultipleThemes(en.getThemesNames()));
            return su;
            
            
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
    @ApiMethod(name = "getSuggestedThemes",path = "getSuggestedThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getSuggestedThemes(EntradaSocial en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Social s = new Social((String)req.getHeader("accessToken"));
            
            Success su = new Success();
            su.setRet(s.getSuggestedThemes());
            return su;
            
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
}
