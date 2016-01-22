/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_della;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

/**
 *
 * @author Chotu
 */
public class OnlineStatus {
     public static boolean checkStatus () 
    {
        try {
            
        final String authUser = "201585025";
	final String authPassword = "msit123";
	Authenticator.setDefault(
            new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    authUser, authPassword.toCharArray());
            }
            }
	);
	    System.setProperty("http.proxyUser", authUser);
	    System.setProperty("http.proxyPassword", authPassword);
	    System.setProperty("http.proxyHost", "10.10.10.3");
            System.setProperty("http.proxyPort", "3128");
            
        URL url = new URL("http://google.com");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int code = connection.getResponseCode();
//        System.out.println("Response code of the object is "+code);
        if (code==200) {
//            System.out.println("OK");
            return true;
        } else {
            return false;
        }
        }catch(Exception e){
            return false;
        }
    }
}
