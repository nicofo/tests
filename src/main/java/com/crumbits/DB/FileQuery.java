/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.DB;

import static com.crumbits.DB.Queries.datastore;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

/**
 *
 * @author Miquel Ferriol
 */
public class FileQuery extends Queries{
    public Key insertFile(String storageId, String bucket, boolean isVideo, Key crumbId, Key thumbnail, double aspectRatio){
        Entity e = new Entity("File");

        e.setProperty("storageId", storageId);
        e.setProperty("bucket", bucket);
        e.setProperty("isVideo", isVideo);
        e.setProperty("crumb", crumbId);
        
        if(!isVideo){
             try{
                String key = "/gs/"+bucket+"/"+storageId;
                ImagesService images = ImagesServiceFactory.getImagesService();
                BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
                BlobKey blobKey = blobstore.createGsBlobKey(key);
                ServingUrlOptions opts = ServingUrlOptions.Builder.
                        withBlobKey(blobKey).
                        secureUrl(true);
                
                String fileUrl = images.getServingUrl(opts);
                e.setProperty("fileUrl", fileUrl);
            }
            catch(Exception ex){
                String fileUrl = "ImagesServiceFailureException";
                e.setProperty("fileUrl", fileUrl);
            }
        }
        else{
            e.setProperty("thumbnail", thumbnail);
            e.setProperty("aspectRatio", aspectRatio);
        }
        return datastore.put(e);
    }
    
    public PreparedQuery getAllFiles() {
        Query q = new Query("File");

        PreparedQuery pq = datastore.prepare(q);

        return pq;
    }
}
