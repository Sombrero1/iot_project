package com.example.project_iot.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ParsingAddressByGeocoder {

    private final RestTemplate restTemplate;

    public ParsingAddressByGeocoder(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Double[] parseAddress(String fromAddress) throws JSONException {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+fromAddress+"&key=AIzaSyB-yZ8axLiHb1OCt44_fqfuqVZpL3jQYs4";
        String response = restTemplate.getForObject(url, String.class);
        JSONObject geo = new JSONObject(response)
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONObject("geometry")
                .getJSONObject("location");
        Double [] geoAns = new Double[2];
        geoAns[0] = geo.getDouble("lat");
        geoAns[1] = geo.getDouble("lng");
        return geoAns;
    }
}
