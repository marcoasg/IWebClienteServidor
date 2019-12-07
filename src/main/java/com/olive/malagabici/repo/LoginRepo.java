/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.repo;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Repository;
import org.apache.http.client.methods.HttpPost;

/**
 *
 * @author Trigi
 */
@Repository
public class LoginRepo {

    private final static String URL_REGISTER_USER = "http://localhost:8081/IngWebServiciosBD/webresources/entity.usuario/postByString";

    public void registerUser(String email, String name) {
        try {
            String url = new StringBuilder(URL_REGISTER_USER)
                    .append("?email=")
                    .append(email)
                    .append("&name=")
                    .append(name)
                    .toString();

            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpClient.execute(httpPost);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
