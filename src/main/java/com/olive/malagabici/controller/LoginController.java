/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.olive.malagabici.service.LoginService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletRequest;
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
    public String doWelcome(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String idToken = (String) request.getSession().getAttribute("idToken");
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);
        if (idTokenObj != null) {
            return "index";
        } else {
            return "login";
        }
    }

    @PostMapping("/")
    public String doLogin(@RequestParam(value = "id_token", required = true) String idToken, Model model, HttpServletRequest request) throws GeneralSecurityException, IOException {
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);
        if (idTokenObj != null) {
            GoogleIdToken.Payload payload = idTokenObj.getPayload();

            // Get profile information from payload
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            loginService.registerUser(email, name);
            request.getSession(true).setAttribute("email", email);
            request.getSession().setAttribute("idToken", idToken);

            model.addAttribute("id_token", idToken);
            return "index";
        } else {
            model.addAttribute("error", "Error en la autenticación, intente de nuevo.");
            return "login";
        }
    }
}
