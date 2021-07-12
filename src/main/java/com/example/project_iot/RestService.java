package com.example.project_iot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.TreeSet;

@Service
public class RestService {
    public class DurationInTraffic {
        public String text;
        public int value;
    }

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(Double[] m,String mode) {
        //mode = driving, walking
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+m[0]+","+m[1]+"&destination="+m[2]+","+m[3]+"&mode="+mode+"&departure_time=now&key=AIzaSyB-yZ8axLiHb1OCt44_fqfuqVZpL3jQYs4";

        System.out.println(url);
        String all;

        all = this.restTemplate.getForObject(url, String.class);

        String answer ="";
        try {
            answer = new JSONObject(all)
                    .getJSONArray("routes")
                    .getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0)
                    .getJSONObject("duration")
                    .getString("text");
            }
         catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(answer);

        return answer;
    }
}