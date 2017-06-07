/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.ReturnClasses;

/**
 *
 * @author Miquel Ferriol
 */
public class GcsFile {
    private String bucket;
    private String fileId;
    private String mimeType;

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
     */
    public GcsFile(String bucket, String fileId) {
        this.bucket = bucket;
        this.fileId = fileId;
    }
    
    /**
     *
     */
    public GcsFile() {
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
