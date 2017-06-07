/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.Info;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;

/**
 *
 * @author Miquel Ferriol Galmé
 */
public class FileInfo {
    private String bucket;
    private String fileId;
    private String mimeType;
    private String fileUrl;
    private boolean isVideo;
    private String type;
    private Double aspectRatio;

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIsVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    /**
     *
     * @return
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     *
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     *
     * @param bucket
     * @param fileId
     * @param mimeType
     */
    public FileInfo(String bucket, String fileId, String mimeType) {
        this.bucket = bucket;
        this.fileId = fileId;
        this.mimeType = mimeType;
        try{
            String key = "/gs/"+bucket+"/"+fileId;
            ImagesService images = ImagesServiceFactory.getImagesService();
            BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
            BlobKey blobKey = blobstore.createGsBlobKey(key);
            ServingUrlOptions opts = ServingUrlOptions.Builder.
                    withBlobKey(blobKey).
                    secureUrl(true);
            fileUrl = images.getServingUrl(opts);
        }
        catch(Exception e){
            fileUrl = "ImagesServiceFailureException";
        }
        
    }
    
    public FileInfo(String bucket, String fileId, boolean isVideo) {
        this.bucket = bucket;
        this.fileId = fileId;
        this.isVideo = isVideo;
        if(!isVideo){
             try{
                String key = "/gs/"+bucket+"/"+fileId;
                ImagesService images = ImagesServiceFactory.getImagesService();
                BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
                BlobKey blobKey = blobstore.createGsBlobKey(key);
                ServingUrlOptions opts = ServingUrlOptions.Builder.
                        withBlobKey(blobKey).
                        secureUrl(true);
                fileUrl = images.getServingUrl(opts);
            }
            catch(Exception e){
                fileUrl = "ImagesServiceFailureException";
            }
        }
        
    }

    /**
     *
     */
    public FileInfo() {
    }
    
    /**
     *
     * @return
     */
    public String getBucket() {
        return bucket;
    }

    /**
     *
     * @param bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     *
     * @return
     */
    public String getFileId() {
        return fileId;
    }

    /**
     *
     * @param fileId
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
