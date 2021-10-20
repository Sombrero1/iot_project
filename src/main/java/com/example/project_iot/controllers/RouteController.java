package com.example.project_iot.controllers;

import com.example.project_iot.models.ResponceRoute;
import com.example.project_iot.service.CalculateRouterService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController()
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    CalculateRouterService calculateRouterService;

    @Autowired
    Gson gson;

    @GetMapping
    public String calculateRouter(){
        String res = "";
        try{
            ResponceRoute responceRoute = calculateRouterService.responceForNucleo(3);
            res = gson.toJson(responceRoute);
        }
        catch (NoSuchElementException e){
            res = "{\"walking\":-1,\"transit\":-1,\"driving\":-1, \"desc\":\"1\"}";
        }
        return res;
    }

}
