/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crumbits.pushNotification;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Miquel Ferriol
 */
public class pushNotification {
    private final String appId = "6da54726-a383-4d36-9eba-933200b1e132";
    
    /**
     *
     * @param userPlayerId
     * @throws MalformedURLException
     * @throws ProtocolException
     * @throws IOException
     */
    public void sendPushNotification(ArrayList<String> userPlayerId, String englishTitle, String spanishTitle, String englishMessage, String spanishMessage) throws MalformedURLException, ProtocolException, IOException{
        String url="https://onesignal.com/api/v1/notifications";
        URL object=new URL(url);

        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");
        ArrayList<String> usersPlayerIdJson = new ArrayList<>();
        
        String apId = "6da54726-a383-4d36-9eba-933200b1e132";
        
        for(int i = 0; i < userPlayerId.size(); ++i){
            usersPlayerIdJson.add("\""+userPlayerId.get(i)+"\"");
        }
        
        String json = " { \"app_id\": \""+appId+"\",\"headings\": {\"en\": \""+englishTitle+"\",\"es\": \""+spanishTitle+"\"},\"contents\":{\"en\":\""+englishMessage+"\",\"es\":\""+spanishMessage+"\"},\"include_player_ids\": " + usersPlayerIdJson.toString() +"}";

        System.out.println(json);
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(json);
        wr.flush();
        StringBuilder sb = new StringBuilder();  
        con.getResponseCode(); 
    }
}
