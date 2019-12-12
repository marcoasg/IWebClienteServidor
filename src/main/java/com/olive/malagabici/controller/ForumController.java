/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.olive.malagabici.service.LoginService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Trigi
 */

@Controller
public class ForumController {
    
    @Autowired
    LoginService loginService;
    
    @GetMapping("/foro")
    public String getForum(HttpServletRequest request) throws GeneralSecurityException, IOException{
        String idToken = (String)request.getSession().getAttribute("idToken");
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);
        
        if (idTokenObj != null) {
            return "foro";
        } else {
            return "login";
        }
        
    }
}
