/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.repo;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Trigi
 */

@Repository
public class LoginRepo {
    
    private final static String URL_REGISTER_USER = "http://localhost:8081/webresources/entity.usuario/postByString";
    
    public void registerUser(String email, String name) {
        try {
            List<String> params = new ArrayList<>();
            params.add(email);
            params.add(name);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(URL_REGISTER_USER, params, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
