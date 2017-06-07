/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.API;

import com.crumbits.API.*;
import com.crumbits.*;
import com.crumbits.Error.Errors;
import com.crumbits.Error.Success;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.inject.Named;
/**
 *
 * @author Miquel Ferriol
 */
@Api(name = "debugAPI",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "com.crumbits",
        ownerName = "com.crumbits",
        packagePath=""))
public class debugAPI {
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "removeAllUnexsitentPlaces",path = "removeAllUnexsitentPlaces",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removeAllUnexsitentPlaces(@Named ("password") String password) {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.removeAllUnexsitentPlaces());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "removeAllUnexistentCrumbs",path = "removeAllUnexistentCrumbs",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removeAllUnexistentCrumbs(@Named ("password") String password) {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.removeAllUnexistentCrumbs());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "removeAllUnexsitentThemes",path = "removeAllUnexsitentThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removeAllUnexsitentThemes(@Named ("password") String password) {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.removeAllUnexsitentThemes());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "removeAllUnusedFiles",path = "removeAllUnusedFiles",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removeAllUnusedFiles(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.removeAllUnusedFiles());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "setAllFilesPublic",path = "setAllFilesPublic",httpMethod = ApiMethod.HttpMethod.POST)
    public Object setAllFilesPublic(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.setAllFilesPublic();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "placesToPlaceId",path = "placesToPlaceId",httpMethod = ApiMethod.HttpMethod.POST)
    public Object placesToPlaceId(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.placesToPlaceId();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @param id
     * @return
     */
    @ApiMethod(name = "fileError",path = "fileError",httpMethod = ApiMethod.HttpMethod.POST)
    public Object fileError(@Named ("password") String password, @Named ("id") String id) throws IOException, EntityNotFoundException {
       
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.fileError(id);
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "createCrumbMultipleFiles",path = "createCrumbMultipleFiles",httpMethod = ApiMethod.HttpMethod.POST)
    public Object createCrumbMultipleFiles(@Named ("password") String password) throws IllegalAccessException, EntityNotFoundException, IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.createCrumbMultipleFiles();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "fileNames",path = "fileNames",httpMethod = ApiMethod.HttpMethod.POST)
    public Object fileNames(@Named ("password") String password) throws IllegalAccessException, EntityNotFoundException, IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.fileNames();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "fileTypes",path = "fileTypes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object fileTypes(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.fileTypes();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "setAllFilesFormat",path = "setAllFilesFormat",httpMethod = ApiMethod.HttpMethod.POST)
    public Object setAllFilesFormat(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.setAllFilesFormat();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "setThemesData",path = "setThemesData",httpMethod = ApiMethod.HttpMethod.POST)
    public Object setThemesData(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.setThemesData();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "setPlacesData",path = "setPlacesData",httpMethod = ApiMethod.HttpMethod.POST)
    public Object setPlacesData(@Named ("password") String password) throws IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.setPlacesData();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    /**
     *
     * @param password
     * @return
     */
    @ApiMethod(name = "setCreationDate",path = "setCreationDate",httpMethod = ApiMethod.HttpMethod.POST)
    public Object setCreationDate(@Named ("password") String password) {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.setCreationDate();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "cleanThemes",path = "cleanThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object cleanThemes(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {
       
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.cleanThemes());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "cleanDoubleThemes",path = "cleanDoubleThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object cleanDoubleThemes(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {
       
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.cleanDoubleThemes());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "separeThemes",path = "separeThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object separeThemes(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.separeThemes());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    
    @ApiMethod(name = "indexTest",path = "indexTest",httpMethod = ApiMethod.HttpMethod.POST)
    public Object indexTest(@Named ("password") String password) throws EntityNotFoundException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.indexTest());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "getUrl",path = "getUrl",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getUrl(@Named ("password") String password) {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.getUrl());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
     @ApiMethod(name = "addFileInfo",path = "addFileInfo",httpMethod = ApiMethod.HttpMethod.POST)
    public Object addFileInfo(@Named ("password") String password) throws IllegalAccessException, EntityNotFoundException, IOException {
       
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.addFileInfo();
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "fileInfoClean",path = "fileInfoClean",httpMethod = ApiMethod.HttpMethod.POST)
    public Object fileInfoClean(@Named ("password") String password) throws IllegalAccessException, EntityNotFoundException, IOException {
        
            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.fileInfoClean());
                return s; 
            }
            return new Errors().passwordError;
        
    }
    
    @ApiMethod(name = "deleteCrumb",path = "deleteCrumb",httpMethod = ApiMethod.HttpMethod.POST)
    public Object deleteCrumb(@Named ("password") String password, @Named ("crumbId") String crumbId) throws EntityNotFoundException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.deleteCrumb(crumbId);
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "multipleSearch",path = "multipleSearch",httpMethod = ApiMethod.HttpMethod.POST)
    public Object multipleSearch(@Named ("password") String password) throws EntityNotFoundException, IOException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.multipleSearch());
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "removeAllUnexsitentNotifications",path = "removeAllUnexsitentNotifications",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removeAllUnexsitentNotifications(@Named ("password") String password) throws EntityNotFoundException, IOException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.removeAllUnexsitentNotifications();
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "deleteAllCorruptedCrumbs",path = "deleteAllCorruptedCrumbs",httpMethod = ApiMethod.HttpMethod.POST)
    public Object deleteAllCorruptedCrumbs(@Named ("password") String password) throws EntityNotFoundException, IOException, IllegalAccessException, InterruptedException, InterruptedException, InterruptedException, InterruptedException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.deleteAllCorruptedCrumbs());
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "addQueueTask",path = "addQueueTask",httpMethod = ApiMethod.HttpMethod.POST)
    public Object addQueueTask(@Named ("password") String password, @Named ("url") String url) throws EntityNotFoundException, IOException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.addQueueTask(url);
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "threadExc",path = "threadExc",httpMethod = ApiMethod.HttpMethod.POST)
    public Object threadExc(@Named ("password") String password) throws EntityNotFoundException, IOException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                while(true){
                    
                }
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "deleteUnusedThemes",path = "deleteUnusedThemes",httpMethod = ApiMethod.HttpMethod.POST)
    public Object deleteUnusedThemes(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.deleteUnusedThemes());
                return s; 
            }
            return new Errors().passwordError;
    }
    
    
    @ApiMethod(name = "aspectRatio",path = "aspectRatio",httpMethod = ApiMethod.HttpMethod.POST)
    public Object aspectRatio(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.aspectRatio();
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "themesAuto",path = "themesAuto",httpMethod = ApiMethod.HttpMethod.POST)
    public Object themesAuto(@Named ("password") String password, @Named ("name") String name) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.themesAuto(name));
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "placesFormat",path = "placesFormat",httpMethod = ApiMethod.HttpMethod.POST)
    public Object placesFormat(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.placesFormat();
                return s; 
            }
            return new Errors().passwordError;
    }
    @ApiMethod(name = "sensitiveContent",path = "sensitiveContent",httpMethod = ApiMethod.HttpMethod.POST)
    public Object sensitiveContent(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.sensitiveContent();
                return s; 
            }
            return new Errors().passwordError;
    }
    
    
    @ApiMethod(name = "getEmbed",path = "getEmbed",httpMethod = ApiMethod.HttpMethod.POST)
    public Object getEmbed(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.getEmbed("ag5zfmNydW1iaXQtMTMwNHISCxIFQ3J1bWIYgICAoJPOgAoM");
                return s; 
            }
            return new Errors().passwordError;
    }
    
    
    @ApiMethod(name = "addGeospacial",path = "addGeospacial",httpMethod = ApiMethod.HttpMethod.POST)
    public Object addGeospacial(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.geospacial();
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "geospacialCirlce",path = "geospacialCirlce",httpMethod = ApiMethod.HttpMethod.POST)
    public Object geospacialCirlce(@Named ("password") String password) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                s.setRet(d.geospacialCirlce());
                return s; 
            }
            return new Errors().passwordError;
    }
    
    @ApiMethod(name = "removingToRemove",path = "removingToRemove",httpMethod = ApiMethod.HttpMethod.POST)
    public Object removingToRemove(@Named ("password") String password, @Named ("theme") String theme) throws EntityNotFoundException, IOException, InterruptedException, ExecutionException, IllegalAccessException {

            if(password.equals("Crumbit1234")){
                Debug d = new Debug();
                Success s = new Success();
                d.removingToRemove(theme);
                return s; 
            }
            return new Errors().passwordError;
    }
}
