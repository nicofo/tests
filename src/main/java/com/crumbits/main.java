package com.crumbits;


import com.crumbits.Info.fbUserInfo;
import com.crumbits.Utilities.Utilities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.images.Transform;
import com.google.appengine.repackaged.com.google.datastore.v1beta3.GqlQuery;
import com.google.appengine.repackaged.org.apache.commons.collections.Buffer;
import com.google.appengine.repackaged.org.apache.commons.io.FileUtils;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import java.util.ArrayList;

import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONString;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.User;
/*import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IContainerFormat;
import com.xuggle.xuggler.io.XugglerIO;*/
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.mail.internet.MimeBodyPart;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jcodec.api.JCodecException;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.FileChannelWrapper;
import org.jcodec.common.NIOUtils;


/**
 *
 * @author Miquel Ferriol
 */
public class main {
    private static final String appId = "AIzaSyCJSfKh2qznkK5wfx6feiwSLPjuIIMPuO4";
    private static final String englishMessage = "Crumbit rules!";
    private static final String spanishMessage = "Crumbit mola!";
    private static final String englishTitle = "Crumbit";
    private static final String spanishTitle = "Crumbit";
    
    
    
/*private static JSONObject convertInputStreamToJSONObject(InputStream inputStream)
        throws JSONException, IOException {
    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
    String line = "";
    String result = "";
    while((line = bufferedReader.readLine()) != null)
        result += line;

    inputStream.close();
    return new JSONObject(result); }*/

    /**
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    
   public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {  
        //double radioTierra = 3958.75;//en millas  
        double radioTierra = 6371;//en kilómetros  
        double dLat = Math.toRadians(lat2 - lat1);  
        double dLng = Math.toRadians(lng2 - lng1);  
        double sindLat = Math.sin(dLat / 2);  
        double sindLng = Math.sin(dLng / 2);  
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)  
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));  
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));  
        double distancia = radioTierra * va2;  
   
        return distancia;  
    } 
   
   private static final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
   
   final static String bucket = "crumbit";
   
   public static void  prova() throws IOException, JCodecException{
        String fileName   = "C:\\Users\\Miquel Ferriol\\Desktop\\Banderes\\video.mp4";
        FileChannelWrapper ch = null;
        try {
            ch = NIOUtils.readableFileChannel(new File(fileName));
            FrameGrab fg = new FrameGrab(ch);
            BufferedImage image = fg.getFrame();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "gif", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            
        } finally {
            NIOUtils.closeQuietly(ch);
        }
   }
   
    private static final String apiKey = "1316954461653611";
    private static final String secretKey = "9f6708cefb08d128aeb043fd1d2424d4"; 
  
    /**
     *
     * @param args
     * @throws Exception
     */
    @JsonIgnoreProperties
    public static void main(String[] args) throws Exception {
        
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://crumbit-1304.appspot.com/update");
        
        MultipartEntity reqEntity = new MultipartEntity((HttpMultipartMode.BROWSER_COMPATIBLE));

        reqEntity.addPart("hola", new StringBody("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZzV6Zm1OeWRXMWlhWFF0TVRNd05ISVJDeElFVlhObGNoaUFnSUNBX2V5QkNndyIsImV4cCI6MTUwMTkwMTgxN30.x9RlaPOoaUfJ664kdqnZ5BXmYSQIpIWFqeHXp3enC2SxQZA3Bd-7Hx2g0IR8m6gk-U71CnvYgMdx909wZ70O7A"));

        httppost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httppost);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(responseString);*/
        /*HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://crumbit-1304.appspot.com/createCrumb");
        


    FileBody fileBody  = new FileBody(new File("C:/Users/Miquel Ferriol Galmé/Downloads/MyFile.jpg"));
    MultipartEntity reqEntity = new MultipartEntity();

    reqEntity.addPart("file", fileBody);

    httppost.setEntity(reqEntity);
    
    httppost.addHeader("Content-length", reqEntity.getContentLength()+"");
    connection.addRequestProperty(mpEntity.getContentType().getName(), mpEntity.getContentType().getValue());
    HttpResponse response = httpclient.execute(httppost);
    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("RESPONSE:" + responseString);*/
        /*CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("https://crumbit-1304.appspot.com/createCrumb");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        uploadFile.addHeader("accessToken", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZzV6Zm1OeWRXMWlhWFF0TVRNd05ISVJDeElFVlhObGNoaUFnSUNBX2V5QkNndyIsImV4cCI6MTUwMTkwMTgxN30.x9RlaPOoaUfJ664kdqnZ5BXmYSQIpIWFqeHXp3enC2SxQZA3Bd-7Hx2g0IR8m6gk-U71CnvYgMdx909wZ70O7A");
        //uploadFile.addHeader("Content-Type", "multipart/form-data");
        String s = "{  \"creatorId\": \" ag5zfmNydW1iaXQtMTMwNHIRCxIEVXNlchiAgICA5ri_CQw \",  \"lat\": 0.256,  \"lng\": 3.255,  \"googlePlaceId\": \"sadfwef48dfas\",  \"description\": \"Un video mp4\",  \"date\": \"1470217488771\",  \"themesId\": [   \"ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgLT2hAgM\",   \"ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgJOCiAgM\"  ],\"placeName\":\"Un Place\" }";
        org.json.JSONObject JSON = new org.json.JSONObject(s);

        builder.addTextBody("json", s, ContentType.APPLICATION_JSON);
        builder.addBinaryBody("img", new FileInputStream("C:\\Users\\Miquel Ferriol\\Desktop\\Banderes\\video.mp4"),ContentType.MULTIPART_FORM_DATA,"myFile");
        HttpEntity multipart = builder.build();

        uploadFile.setEntity(multipart);

        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("RESPONSE:" + responseString);*/
        /*String s = "{  \"creatorId\": \" ag5zfmNydW1iaXQtMTMwNHIRCxIEVXNlchiAgICA5ri_CQw \",  \"lat\": 0.256,  \"lng\": 3.255,  \"googlePlaceId\": \"sadfwef48dfas\",  \"description\": \"Un crumb\",  \"date\": \"2001-07-04T12:08:56.235-0700\",  \"themesId\": [   \"ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgLT2hAgM\",   \"ag5zfmNydW1iaXQtMTMwNHISCxIFVGhlbWUYgICAgJOCiAgM\"  ],\"placeName\":\"Un Place\" }";
        org.json.JSONObject JSON = new org.json.JSONObject(s);*/
       /*System.out.println(JSON.toString());*/
        
        /*int frameNumber = 150;
        File f = new File("C:\\Users\\Miquel Ferriol\\Desktop\\Banderes\\video.mp4");
        BufferedImage frame = FrameGrab.getFrame(f, 5);
        ImageIO.write(frame, "jpeg", new File("frame_150.jpeg"));*/
        //TODO saving the buffImg
        //File f = new File("Descargas\\video.mp4");
       //DecodeAndCaptureFrames frame = new DecodeAndCaptureFrames("C:\\Users\\Miquel Ferriol\\Descargas\\video.mp4");
        
                
        /*IContainer iContainer = IContainer.make();
        IContainerFormat inputFormat = IContainerFormat.make();
        inputFormat.setInputFormat("mp4");
        iContainer.open(XugglerIO.map(is),IContainer.Type.READ, null);*/
   /*if (iContainer.open(is,inputFormat) >= 0) {
        IMediaReader mediaReader = ToolFactory.makeReader(iContainer);
        System.out.println("UEAH");
    }
    else{
        System.out.println("BAHH");
    }*/
        /*String fbToken = "EAACEdEose0cBAOpnBCUmvTvAbxUpZCZBNWfnrGF862gwbZCPJsOZBT1H0pGCecxaTFvqgRh6JzQGq5v5Dznlpg7dMQkyBpFSQJTpwXcQee68ZB4ysZC3zftc8xfrBNga6xIhhxFQRf9ZAEcgJSkv1b1OIR6uNuqhR2S84VPsVfghgZDZD";
        HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("https://graph.facebook.com/v2.1/me?fields=email&access_token="+fbToken);


            HttpResponse response = httpclient.execute(httppost);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            org.json.JSONObject jsonObj = new org.json.JSONObject(responseString);
            String email = (String)jsonObj.get("email");
            System.out.println(email);
            FacebookClient.AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(apiKey, secretKey);
            System.out.println(accessToken.getAccessToken());*/
            
            /*GcsFilename gcsFilename = new GcsFilename("crumbit", "10086297911472666831561.jpg");
            String key = "/gs/crumbit/10086297911472666831561.jpg"; // Such as /gs/example-bucket/categories/animals.png"
        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions options = ServingUrlOptions.Builder
                        .withGoogleStorageFileName(key);
        String servingUrl = imagesService.getServingUrl(options);*/
        /*String key = "/gs/crumbit/10086297911472666831561.jpg"; // Such as /gs/example-bucket/categories/animals.png"
        ImagesService images = ImagesServiceFactory.getImagesService();
        GcsFilename gcsFilename = new GcsFilename("crumbit", "10086297911472666831561.jpg");
        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
        BlobKey blobKey = blobstore.createGsBlobKey(key);
        ServingUrlOptions opts = ServingUrlOptions.Builder.
                withBlobKey(blobKey).
                secureUrl(true);
        images.getServingUrl(opts);*/
        
        
                       /* FileInputStream fileInputStream = new FileInputStream(new File("WEB-INF/image.jpg"));
                        FileChannel fileChannel = stream.getChannel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size());
                        fileChannel.read(byteBuffer);*/
                    /*ImagesService imagesService = ImagesServiceFactory.getImagesService();
                    GcsFilename gcsFilename = new GcsFilename(bucket, "10086297911472666831561.jpg");
                    Image image = ImagesServiceFactory.makeImageFromFilename("/gs/crumbit/10086297911472666831561.jpg");
                    Transform resize = ImagesServiceFactory.makeResize(100, 50);
                    Image resizedImage = imagesService.applyTransform(resize, image);*/
        
        /*ArrayList<String> names1 = new ArrayList<>();
        ArrayList<String> names2 = new ArrayList<>();
        ArrayList<String> res = new ArrayList<>();
        names1.add("a");
        names1.add("b");
        names1.add("c");
        names1.add("d");
        
        names2.add("b");
        names2.add("d");
        names2.add("e");
        
        res.addAll(names1);
        res.addAll(names2);
        names1.retainAll(names2);
        res.remove(names1);
        System.out.println(res);*/
        /*Utilities u = new Utilities();
        System.out.println(u.sha_512("Rosa1234"));
        String crumbId = "ag5zfmNydW1iaXQtMTMwNHISCxIFQ3J1bWIYgICAoJPOgAoM";
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<iframe id=\"crumb-"+crumbId+"\" src=\"http://test.crumbit.org/embed/"+crumbId+"\" frameborder=\"0\"></iframe><script>(function(d, t){var g=d.createElement(t), s=d.getElementsByTagName(t)[0]; g.src='http://test.crumbit.org/embed-script/"+crumbId+"'; s.parentNode.insertBefore(g, s);}(document, 'script'));</script>"+
                "</body>\n" +
                "</html>";
        */
        int a = 100;
        int b = 25;
System.out.println(a / b);
System.out.println(new ArrayList<>());
    }
  
        


    }
 
