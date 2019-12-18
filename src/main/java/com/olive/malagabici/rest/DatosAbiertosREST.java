/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.google.gson.*;
/**
 *
 * @author Trigi
 */
@RestController
@RequestMapping("datos")
public class DatosAbiertosREST {




    @GetMapping("fuentes")
    public String fuentesREST() throws MalformedURLException, IOException {
        installTrustManager();

        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("musculacion")
    public String musculacionREST() throws MalformedURLException, IOException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/deportes/equipamientos/da_deportesZonasMusculacion-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("aparcamientos_bici")
    public String aparcamientosREST() throws MalformedURLException, IOException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("carriles_bici")
    public String carrilesBici() throws MalformedURLException, IOException {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/trafico/da_carrilesBici-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("plazas_libres/{id}")
    public String aparcamientosLibresREST(@PathVariable("id") Integer id) throws MalformedURLException, IOException  {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=3bb304f9-9de3-4bac-943e-7acce7e8e8f9");
        return getFeature("NUM_LIBRES", getRecords(getGeojson(url)), id);
    }

    @GetMapping("fuente_cercana/{x}/{y}")
    public String fuente_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException  {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return masCercano(x, y, getFeatures(getGeojson(url))).toString();
    }

    @GetMapping("bici_cercana/{x}/{y}")
    public String bici_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException  {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return masCercano(x, y, getFeatures(getGeojson(url))).toString();
    }

    @GetMapping("musculacion_cercana/{x}/{y}")
    public String musculacion_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException  {
        installTrustManager();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/deportes/equipamientos/da_deportesZonasMusculacion-4326.geojson");
        return masCercano(x, y, getFeatures(getGeojson(url))).toString();
    }

    public void installTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
        private JsonObject getGeojson(URL url) throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
       
        while((inputInline = in.readLine())!= null){
            response.append(inputInline);
        }
        in.close();
        
        JsonParser parser = new JsonParser();   
        return parser.parse(response.toString()).getAsJsonObject();
    }
    
    private String getFeature( String record, JsonArray records, Integer id)   {
        int i=0;
        JsonObject json  = null;
        while(i<records.size()){
            json = records.get(i).getAsJsonObject();
            if(!json.get("ID_EXTERNO").isJsonNull()&&Integer.parseInt(json.get("ID_EXTERNO").getAsString())==id){
                return json.get(record).getAsString();
            }
            i++;
        }
        return "-1";
    }

    private JsonObject masCercano(Double x, Double y, JsonArray records)   {
        int i=0;
        JsonObject jo = null;
        JsonObject res = null;
        Double dist = Double.MAX_VALUE;
        while(i<records.size()){
            jo = records.get(i).getAsJsonObject();
            if(distancia(jo.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble(), jo.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble() , x, y)< dist){
                dist = distancia(jo.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(0).getAsDouble(), jo.get("geometry").getAsJsonObject().get("coordinates").getAsJsonArray().get(1).getAsDouble(), x, y);
                res = jo;
            }
            i++;
        }
        return res;
    }
    
    
    private JsonArray getFeatures(JsonObject json) {
        return json.get("features").getAsJsonArray();
    }
    
    private JsonArray getRecords(JsonObject json) {
        return json.get("result").getAsJsonObject().get("records").getAsJsonArray();
    }
    
    private Double distancia(double latitud, double longitud, Double x, Double y) {
        return Math.sqrt(Math.pow(latitud-x, 2) + Math.pow(longitud-y, 2));
    }
}
