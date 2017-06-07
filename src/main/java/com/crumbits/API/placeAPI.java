/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaCrumb;
import com.crumbits.Entrada.EntradaPlace;
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
@Api(name = "placeAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))

public class placeAPI {
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "placesAutocomplete",path = "placesAutocomplete",httpMethod = ApiMethod.HttpMethod.POST)
    public Object placesAutocomplete(EntradaPlace en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));            
            
            Success s = new Success();
            s.setRet(p.placesAutocomplete(en.getPlace()));
            return s; 
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
    @ApiMethod(name = "getAllCoordinates",path = "getAllCoordinates",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getAllCoordinates(EntradaPlace en, HttpServletRequest req) {
        try{
            Place p = new Place((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getAllCoordinates());
            return s;
            
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
    @ApiMethod(name = "createPlace",path = "createPlace",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createPlace(EntradaPlace en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            Success s = new Success();
            s.setRet(p.createPlace(en.getPlace(),en.getLat(),en.getLng(), en.getGooglePlaceId()));
            
            return s;
            
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
    @ApiMethod(name = "placesSearch",path = "placesSearch",httpMethod = ApiMethod.HttpMethod.POST)
    public Object placesSearch(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            
            
            Success s = new Success();
            s.setRet(p.placesSearch(en.getPlace()));
            return s; 
            
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
    @ApiMethod(name = "getPlacesSearchResult",path = "getPlacesSearchResult",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getPlacesSearchResult(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getPlacesSearchResult(en.getSearchId(), en.getPage(), en.getPageSize()));
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
    @ApiMethod(name = "getPlaceById",path = "getPlaceById",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getPlaceById(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getPlaceById(en.getPlaceId()));
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
    @ApiMethod(name = "getPlaceCrumbs",path = "getPlaceCrumbs",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getPlaceCrumbs(EntradaPlace en, HttpServletRequest req) throws IOException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));            
            Success s = new Success();
            s.setRet(p.getPlaceCrumbs(en.getPlaceId(),en.getPage(),en.getPageSize()));
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
    @ApiMethod(name = "getUserIsFollowingPlace",path = "getUserIsFollowingPlace",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserIsFollowingPlace(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            Success s = new Success();
            s.setRet(p.getUserIsFollowingPlace( en.getPlaceId()));
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
    @ApiMethod(name = "userFollowPlace",path = "userFollowPlace",httpMethod = ApiMethod.HttpMethod.POST)
    public Object userFollowPlace(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            p.userFollowPlace( en.getPlaceId());
            return new Success();
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
    @ApiMethod(name = "userUnfollowPlace",path = "userUnfollowPlace",httpMethod = ApiMethod.HttpMethod.POST)
    public Object userUnfollowPlace(EntradaPlace en, HttpServletRequest req) {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            p.userUnfollowPlace( en.getPlaceId());
            return new Success();
            
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
    @ApiMethod(name = "getSuggestedPlaces",path = "getSuggestedPlaces",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getSuggestedPlaces(EntradaPlace en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getSuggestedPlaces());
            return s; 
            
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
    @ApiMethod(name = "getPlaceByGooglePlaceId",path = "getPlaceByGooglePlaceId",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getPlaceByGooglePlaceId(EntradaPlace en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(p.getPlaceByGooglePlaceId(en.getGooglePlaceId()));
            return s; 
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
    
    @ApiMethod(name = "getPlacesByChoords",path = "getPlacesByChoords",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getPlacesByChoords(EntradaCrumb en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Place p = new Place((String)req.getHeader("accessToken"));
            Debug d = new Debug();
            Success s = new Success();
            s.setRet(d.getPlaceList(en.getBotLat(),en.getBotLng(),en.getTopLat(),en.getTopLng()));
            return s; 
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
}
