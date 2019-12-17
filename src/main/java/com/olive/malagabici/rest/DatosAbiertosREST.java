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
/**
 *
 * @author Trigi
 */
@RestController
@RequestMapping("datos")
public class DatosAbiertosREST {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private void addHeaders() {
        response.addHeader("Access-Control-Allow-Origin", "*");
    }

    @GetMapping("fuentes")
    public String fuentesREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("musculacion")
    public String musculacionREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/deportes/equipamientos/da_deportesZonasMusculacion-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("aparcamientos_bici")
    public String aparcamientosREST() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("carriles_bici")
    public String carrilesBici() throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/trafico/da_carrilesBici-4326.geojson");
        return getGeojson(url).toString();
    }

    @GetMapping("plazas_libres/{id}")
    public String aparcamientosLibresREST(@PathVariable("id") Integer id) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/api/3/action/datastore_search?resource_id=3bb304f9-9de3-4bac-943e-7acce7e8e8f9");
        return getFeature("NUM_LIBRES", getRecords(getGeojson(url)), id);
    }

    @GetMapping("fuente_cercana/{x}/{y}")
    public String fuente_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/ambiente/fuentesaguapotable/da_medioAmbiente_fuentes-4326.geojson");
        return masCercano(x, y, getFeatures(getGeojson(url))).toString();
    }

    @GetMapping("bici_cercana/{x}/{y}")
    public String bici_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
        URL url = new URL("https://datosabiertos.malaga.eu/recursos/transporte/EMT/EMTubicaparcbici/da_aparcamientosBiciEMT-4326.geojson");
        return masCercano(x, y, getFeatures(getGeojson(url))).toString();
    }

    @GetMapping("musculacion_cercana/{x}/{y}")
    public String musculacion_cercana(@PathVariable("x") Double x, @PathVariable("y") Double y) throws MalformedURLException, IOException, JSONException {
        installTrustManager();
        addHeaders();
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
        private JSONObject getGeojson(URL url) throws JSONException, IOException {
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.addRequestProperty("Access-Control-Allow-Origin", "localhost:8080");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
       
        while((inputInline = in.readLine())!= null){
            response.append(inputInline);
        }
        in.close();
        
        JSONObject json = new JSONObject(response.toString());
        return json;
    }
    
    private String getFeature( String record, JSONArray records, Integer id) throws JSONException {
        int i=0;
        while(i<records.length()){
            if(records.getJSONObject(i).get("ID_EXTERNO").toString().equals(Integer.toString(id))){
                return records.getJSONObject(i).get(record).toString();
            }
            i++;
        }
        return "-1";
    }

    private JSONObject masCercano(Double x, Double y, JSONArray records) throws JSONException {
        int i=0;
        JSONObject jo = null;
        JSONObject res = null;
        Double dist = Double.MAX_VALUE;
        while(i<records.length()){
            jo = records.getJSONObject(i);
            if(distancia(jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1) , x, y)< dist){
                dist = distancia(jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(0),jo.getJSONObject("geometry").getJSONArray("coordinates").getDouble(1) , x, y);
                res = jo;
            }
            i++;
        }
        return res;
    }
    
    
    private JSONArray getFeatures(JSONObject json) throws JSONException{
        return json.getJSONArray("features");
    }
    
    private JSONArray getRecords(JSONObject json) throws JSONException{
        return json.getJSONObject("result").getJSONArray("records");
    }
    
    private Double distancia(double latitud, double longitud, Double x, Double y) {
        return Math.sqrt(Math.pow(latitud-x, 2) + Math.pow(longitud-y, 2));
    }
}
