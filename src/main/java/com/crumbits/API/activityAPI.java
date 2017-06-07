/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.*;
import com.crumbits.Entrada.EntradaActivity;
import com.google.api.server.spi.config.Api;
import com.crumbits.Error.*;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author Miquel Ferriol
 */
@Api(name = "activityAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))

public class activityAPI {
    
    /**
     *
     * @param en
     * @param req
     * @return
     */
    @ApiMethod(name = "getNumberPendentNotif",path = "getNumberPendentNotif",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getNumberPendentNotif(EntradaActivity en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Activity a = new Activity((String)req.getHeader("accessToken"));
            int nre = a.getNumberPendentNotiﬁcation();
            Success s = new Success();
            s.setRet(nre);
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
    @ApiMethod(name = "getUserOwnActivity",path = "getUserOwnActivity",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserOwnActivity(EntradaActivity en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Activity a = new Activity((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(a.getUserOwnActivity(en.getPage(),en.getPageSize()));
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
    @ApiMethod(name = "getUserFollowingActivity",path = "getUserFollowingActivity",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUserFollowingActivity(EntradaActivity en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Activity a = new Activity((String)req.getHeader("accessToken"));
            
            Success s = new Success();
            s.setRet(a.getUserFollowingActivity(en.getPage(),en.getPageSize()));
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
    @ApiMethod(name = "markNotificationAsRead",path = "markNotificationAsRead",httpMethod = ApiMethod.HttpMethod.POST)
    public Object markNotificationAsRead(EntradaActivity en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Activity a = new Activity((String)req.getHeader("accessToken"));
            a.markNotificationAsRead(en.getNotificationId());
            return new Success();
            
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
    @ApiMethod(name = "markAllNotificationsAsRead",path = "markAllNotificationsAsRead",httpMethod = ApiMethod.HttpMethod.POST)
    public Object markAllNotificationsAsRead(EntradaActivity en, HttpServletRequest req) throws EntityNotFoundException {
        try{
            
            Activity a = new Activity((String)req.getHeader("accessToken"));
            a.markAllNotificationsAsRead();
            return new Success();
            
        }
        catch(IllegalAccessException iae){
            return new Errors().tokenError;
        }
    }
}
