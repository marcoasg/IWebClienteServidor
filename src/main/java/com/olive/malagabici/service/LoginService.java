/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

/**
 *
 * @author Trigi
 */
@Service
public class LoginService {

    final static String CLIENT_ID = "1019387256307-v6e72mosg75v9qq6bjb042pkbquq9hr5.apps.googleusercontent.com";

    private GoogleIdTokenVerifier getGoogleVerifier() throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier
                = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                        .setAudience(Collections.singletonList(CLIENT_ID))
                        .build();
        return verifier;
    }

    public void registerUser(String email, String name) {
        String URL_SERVER_BD = "https://malaga-bici.herokuapp.com/";
        String URL_REGISTER_USER = "entity.usuario/postByString";
        
        try {
            String url = new StringBuilder(URL_SERVER_BD + URL_REGISTER_USER)
                    .append("?email=")
                    .append(email)
                    .append("&name=")
                    .append(name)
                    .toString().replaceAll("\\s", "");

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpClient.execute(httpPost);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GoogleIdToken verifyToken(String idToken) throws GeneralSecurityException, IOException {
        if (idToken == null) {
            return null;
        }
        GoogleIdTokenVerifier verifier = getGoogleVerifier();
        GoogleIdToken idTokenObj = verifier.verify(idToken);
        return idTokenObj;
    }
}
