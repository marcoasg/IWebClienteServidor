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
import com.olive.malagabici.model.Usuario;
import com.olive.malagabici.repo.IUsuarioRepo;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author Trigi
 */
@Service
public class LoginService {
    
    @Autowired
    private IUsuarioRepo usuarioRepo;

    final static String CLIENT_ID = "1019387256307-v6e72mosg75v9qq6bjb042pkbquq9hr5.apps.googleusercontent.com";

    private GoogleIdTokenVerifier getGoogleVerifier() throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier
                = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                        .setAudience(Collections.singletonList(CLIENT_ID))
                        .build();
        return verifier;
    }

    public void registerUser(String email, String name) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setToken(name);
        usuarioRepo.save(u);
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
