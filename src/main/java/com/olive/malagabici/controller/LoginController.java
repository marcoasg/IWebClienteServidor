/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.olive.malagabici.service.LoginService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Trigi
 */
@Controller
public class LoginController {
    
    @Autowired
    LoginService loginService;

    @GetMapping("/")
    public String doWelcome() {
        return "login";
    }

    @PostMapping("/")
    public String doLogin(@RequestParam(value = "id_token", required = true) String idToken, Model model) throws GeneralSecurityException, IOException {
        System.out.println(idToken);
        return loginService.doOAuth2Login(idToken, model);
    }
}
