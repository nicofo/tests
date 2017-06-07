/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaComment;
import com.crumbits.Error.Errors;
import com.crumbits.Error.Success;
import com.google.api.server.spi.config.Api;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Miquel Ferriol
 */
@Api(name = "commentAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))

public class commentAPI {
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "createComment",path = "createComment",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createComment(EntradaComment en, HttpServletRequest req) throws IOException {
        try{
            Comment c = new Comment((String)req.getHeader("accessToken"));
            Date today = Calendar.getInstance().getTime(); 
            Success s = new Success();
            s.setRet(c.createComment( en.getCrumbId(), en.getComment(),today,en.getPage(),en.getPageSize()));
            
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
    @ApiMethod(name = "getAllCommentsByCrumbId", path = "getAllCommentsByCrumbId",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getAllCommentsByCrumbId(EntradaComment en, HttpServletRequest req) {
        try{
            
            Comment c = new Comment((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(c.getAllCommentsByCrumbId(en.getCrumbId(), en.getPage(), en.getPageSize(), en.isAscending()));
            return s;
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
        catch (EntityNotFoundException e){
            return new Errors().idError;
        }
    }
    
    
    @ApiMethod(name = "getAllCrumbComments", path = "getAllCrumbComments",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getAllCrumbComments(EntradaComment en, HttpServletRequest req) {
        try{
            
            Comment c = new Comment((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(c.getAllCrumbComments(en.getCrumbId(), en.isAscending()));
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
