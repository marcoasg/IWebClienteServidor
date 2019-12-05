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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 *
 * @author Trigi
 */

@Service
public class LoginService {
    
    final static String CLIENT_ID = "1019387256307-v6e72mosg75v9qq6bjb042pkbquq9hr5.apps.googleusercontent.com";
    
    public String doOAuth2Login(String idToken, Model model) throws GeneralSecurityException, IOException{
        GoogleIdTokenVerifier verifier = 
                new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

    // (Receive idToken by HTTPS POST)
        GoogleIdToken idTokenObj = verifier.verify(idToken);
        if (idTokenObj != null) {
            GoogleIdToken.Payload payload = idTokenObj.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = payload.getEmailVerified();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...
            model.addAttribute("id_token", idToken);
            return "index";
        } else {
            model.addAttribute("error", "Error en la autenticación, intente de nuevo.");
            return "login";
        }
    }
}
