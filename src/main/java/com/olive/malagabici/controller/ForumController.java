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
import org.springframework.ui.Model;
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
    public String getForum(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String idToken = (String) request.getSession().getAttribute("idToken");
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);

        if (idTokenObj != null) {
            return "foro";
        } else {
            return "login";
        }
    }

    @GetMapping("/tema")
    public String getHilos(HttpServletRequest request, Model model) throws GeneralSecurityException, IOException {
        String idToken = (String) request.getSession().getAttribute("idToken");
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);

        if (idTokenObj != null) {
            model.addAttribute("tema", (String) request.getParameter("tema"));
            model.addAttribute("descripcion", (String) request.getParameter("descripcion"));
            model.addAttribute("usuario", (String) request.getSession().getAttribute("email"));
            model.addAttribute("token", (String) request.getSession().getAttribute("idToken"));
            return "tema";
        } else {
            return "login";
        }
    }

    @GetMapping("/hilo")
    public String getMensajes(HttpServletRequest request, Model model) throws GeneralSecurityException, IOException {
        String idToken = (String) request.getSession().getAttribute("idToken");
        GoogleIdToken idTokenObj = loginService.verifyToken(idToken);

        if (idTokenObj != null) {
            model.addAttribute("hilo", (String) request.getParameter("hilo"));
            model.addAttribute("tituloHilo", (String) request.getParameter("tituloHilo"));
            return "hilo";
        } else {
            return "login";
        }
    }
}
