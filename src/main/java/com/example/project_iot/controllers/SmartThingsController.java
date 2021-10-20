package com.example.project_iot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController()
@RequestMapping("/home")
public class SmartThingsController {

    RestTemplate restTemplate;

    public SmartThingsController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    String sw = "on";

    @GetMapping()
    public String smartHome(){

        String url = "https://api.smartthings.com/v1/devices/76df1ddd-e5ae-4440-b8c4-1a5e7581583e/commands";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ef50ddce-49d7-4524-baeb-d1da43800b0b");
        HttpEntity<String> entity = new HttpEntity<>(String.format("[{\n" +
                "\"capability\": \"switch\",\n" +
                "\"command\": \"%s\"\n" +
                "}]", sw), headers);
        restTemplate.postForObject(url, entity, String.class);
        if (sw.equals("on")) {
            sw = "off";
        }
        else{
            sw = "on";
        }
        return "OK";
    }
}
