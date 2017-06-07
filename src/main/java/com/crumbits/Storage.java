/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Miquel Ferriol
 */
public class Storage {

    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    private final String bucket = "crumbit";
    
    private final String embedsBucket = "crumbit/embeds";
    /**
     *
     * @param imageBytes
     * @return 
     * @throws IOException
     */
    public String upLoadFile(byte[] imageBytes) throws IOException {
        /*FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size());
        fileChannel.read(byteBuffer);

        byte[] imageBytes = byteBuffer.array();*/
        Date today = Calendar.getInstance().getTime();
        String fileIdentifier = String.valueOf(imageBytes.hashCode()) +  String.valueOf(today.getTime())+".jpeg";
        gcsService.createOrReplace(
            new GcsFilename(bucket, fileIdentifier),
            new GcsFileOptions.Builder().acl("public-read").mimeType("image/jpeg").build(),
            ByteBuffer.wrap(imageBytes));
        
        return fileIdentifier;
    }
    
    public String uploadHtml(String crumbId) throws IOException {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<iframe id=\"crumb-"+crumbId+"\" src=\"http://test.crumbit.org/embed/"+crumbId+"\" frameborder=\"0\"></iframe><script>(function(d, t){var g=d.createElement(t), s=d.getElementsByTagName(t)[0]; g.src='http://test.crumbit.org/embed-script/"+crumbId+"'; s.parentNode.insertBefore(g, s);}(document, 'script'));</script>"+
                "</body>\n" +
                "</html>";

        InputStream stream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));

        String fileIdentifier = crumbId+".html";
        gcsService.createOrReplace(
            new GcsFilename(embedsBucket, fileIdentifier),
            new GcsFileOptions.Builder().acl("public-read").mimeType("text/html").build(),
            ByteBuffer.wrap(IOUtils.toByteArray(stream)));
        
        return fileIdentifier;
    }

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public byte[] downLoadFile(GcsFilename fileName) throws IOException {
        GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(fileName, 0, BUFFER_SIZE);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        copy(Channels.newInputStream(readChannel), output);
        return output.toByteArray();
    }

    private void copy(InputStream input, ByteArrayOutputStream output) throws IOException {
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
}
