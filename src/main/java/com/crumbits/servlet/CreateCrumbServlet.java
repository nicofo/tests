/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.servlet;
import com.crumbits.Crumb;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
  
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.IOUtils;
import org.jcodec.common.NIOUtils;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
  
/**
 *
 * @author Miquel Ferriol
 */
public class CreateCrumbServlet extends HttpServlet {
     
    /**
     * Directory where uploaded files will be saved, its relative to
     * the web application directory.
     */
    
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    
    private final String bucket = "crumbit";
    private final String thumbnailBucket = "crumbit/thumbnails";
    
    
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    
    @Override
    protected void doPost(HttpServletRequest req,
            HttpServletResponse res) throws ServletException, IOException {
    
        String token = req.getHeader("accessToken");
    try {
        Crumb c = new Crumb(token);
        ServletFileUpload upload = new ServletFileUpload();
        
        double lat = 0;
        double lng = 0;
        String creatorId = "";
        String googlePlaceId = "";
        String description = "";
        Date date = new Date();
        String placeName = "";
        String thumbnail = null;
        boolean sensitiveContent = false;
        double aspectRatio = 0;
        ArrayList<String> themeIds = new ArrayList<>();

        JSONObject json = new JSONObject();
        FileItemIterator iter = upload.getItemIterator(req);
        ArrayList<String> fileIds = new ArrayList<>();
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            if (item.isFormField()) {
                
                String type = item.getContentType();
                if(type.startsWith("image")){
                    
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    Date today = Calendar.getInstance().getTime();
                    String fileIdentifier = String.valueOf(item.hashCode()) +  String.valueOf(today.getTime() + "." + type.subSequence(6, type.length()));
                    if(name.equals("thumbnail")){
                        thumbnail = fileIdentifier;
                        GcsOutputChannel outputChannel;
                        GcsFilename filename = new GcsFilename(thumbnailBucket,fileIdentifier);
                        outputChannel = gcsService.createOrReplace(filename,new GcsFileOptions.Builder().mimeType(type).acl("public-read").build());
                        copy(stream, Channels.newOutputStream(outputChannel));
                        
                        Image blobImage = ImagesServiceFactory.makeImage(downLoadFile(filename));
                        double width = (double)blobImage.getWidth();
                        double height = (double)blobImage.getHeight();
                        aspectRatio = height/width;
                    }
                    else{
                        GcsOutputChannel outputChannel;
                        outputChannel = gcsService.createOrReplace(new GcsFilename(bucket, fileIdentifier),new GcsFileOptions.Builder().mimeType(type).acl("public-read").build());
                        copy(stream, Channels.newOutputStream(outputChannel));
                        fileIds.add(fileIdentifier);
                    }
                    
                }
                else if(type.startsWith("video")){
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    Date today = Calendar.getInstance().getTime();
                    String fileIdentifier = String.valueOf(item.hashCode()) +  String.valueOf(today.getTime() + "." + type.subSequence(6, type.length()));
                    
                    GcsOutputChannel outputChannel;
                    outputChannel = gcsService.createOrReplace(new GcsFilename(bucket, fileIdentifier),new GcsFileOptions.Builder().mimeType(type).acl("public-read").build());
                    copy(stream, Channels.newOutputStream(outputChannel));
                    fileIds.add(fileIdentifier);
                }
                else if (type.startsWith("application")){
                    InputStream inputStream = item.openStream();
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)jsonParser.parse(
                          new InputStreamReader(inputStream, "UTF-8"));
                    
                    org.json.JSONObject JSON = new org.json.JSONObject(jsonObject.toString());
                    creatorId = JSON.getString("creatorId");
                    //sensitiveContent = JSON.getBoolean("sensitiveContent");
                    lat = JSON.getDouble("lat");
                    lng = JSON.getDouble("lng");
                    googlePlaceId = JSON.getString("googlePlaceId");
                    description = JSON.getString("description");
                    placeName = JSON.getString("placeName");
                    
                    date = new Date(Long.parseLong(JSON.getString("date")));

                    themeIds = new ArrayList<>();     
                    JSONArray jsonArray = (JSONArray)JSON.getJSONArray("themesId"); 
                    
                    if (jsonArray != null) { 
                       int len = jsonArray.length();
                       for (int i=0;i<len;i++){ 
                        themeIds.add(jsonArray.get(i).toString());
                       } 
                    }
                    
                }
                
            } else {
                    json.put("not form field","not form field");
                    String type = item.getContentType();
                    json.put("imagen",type);
                    String name = item.getFieldName();
                    json.put("name",name);
                    InputStream stream = item.openStream();
                    Date today = Calendar.getInstance().getTime();
                    String fileIdentifier = String.valueOf(item.hashCode()) +  String.valueOf(today.getTime()) + ".mp4";
                    GcsOutputChannel outputChannel;
                    outputChannel = gcsService.createOrReplace(new GcsFilename(bucket, fileIdentifier + ".mp4"),new GcsFileOptions.Builder().mimeType("video/mp4").acl("public-read").build());
                    copy(stream, Channels.newOutputStream(outputChannel));
                    fileIds.add(fileIdentifier);
                    
                    
                    /*File file = new File("uri");
                    /*OutputStream os = new FileOutputStream(file);
                    IOUtils.copy(stream, os);
                    os.close();
                    FileChannelWrapper ch = null;
                    /*try {
                        ch = NIOUtils.readableFileChannel(file);
                        FrameGrab fg = new FrameGrab(ch);
                        BufferedImage image = fg.getFrame();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpeg", os);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        outputChannel = gcsService.createOrReplace(new GcsFilename(bucket, "prova.jpeg"),new GcsFileOptions.Builder().mimeType("image/jpeg").acl("public-read").build());
                        copy(is, Channels.newOutputStream(outputChannel));

                    } finally {
                        NIOUtils.closeQuietly(ch);
                    }*/
            }
        }
        json.put("status", "success");
        res.setContentType("application/json");
        
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        res.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        res.addHeader("Access-Control-Max-Age", "1728000");
        
        PrintWriter out = res.getWriter();
        out.print(json.toString());
        out.flush();
        out.close();
        
        c.createCrumb(creatorId, description, themeIds, placeName, googlePlaceId, lat, lng, date, fileIds, thumbnail,aspectRatio, sensitiveContent);
    } 
    catch(IllegalAccessException iae){
            org.json.JSONObject json = new org.json.JSONObject( "{\"status\": \"error\",\"error\": {\"errorTitle\": \"token authentication error\",\"errorMessage\": \"This token is invalid or has expired\",\"errorCode\": 403}}");
        
            res.setContentType("application/json");
            
            PrintWriter out = res.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }
        catch (NullPointerException e){
            org.json.JSONObject json = new org.json.JSONObject( "{\"status\": \"error\",\"error\": {\"errorTitle\": \"argument exception\",\"errorMessage\": \"algun argumento es null o no tiene el formato correcto\",\"errorCode\": 500}}");
        
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }  
        catch (Exception e){
            org.json.JSONObject json = new org.json.JSONObject( "{\"status\": \"error\",\"error\": {\"errorTitle\": \"internal error\",\"errorMessage\": \"unknown error has ocurred\",\"errorCode\": 500}}");
            json.put("exception", e);
            res.setContentType("application/json");
            PrintWriter out = res.getWriter();
            out.print(json.toString());
            out.flush();
            out.close();
        }     
        
    }
    
    private void copy(InputStream input, OutputStream output) throws IOException {
    try {
      byte[] buffer = new byte[BUFFER_SIZE];
      int bytesRead = input.read(buffer);
      while (bytesRead != -1) {
        output.write(buffer, 0, bytesRead);
        bytesRead = input.read(buffer);
      }
    } finally {
      input.close();
      output.close();
    }
}
     private byte[] downLoadFile(GcsFilename fileName) throws IOException {
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        copy(Channels.newInputStream(readChannel), output);
        return output.toByteArray();
    }
}
