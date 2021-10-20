package com.example.project_iot.controllers;

import com.example.project_iot.service.CalculateRouterService;
import com.example.project_iot.models.CalendarEvent;
import com.example.project_iot.repo.DevelopDB;
import com.example.project_iot.service.ParsingAddressByGeocoder;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AliceController {
    @Autowired
    CalculateRouterService calculateRouterService;

    @Autowired
    ParsingAddressByGeocoder parsingAddress;

    private Double[]geoFrom = new Double[]{ 55.6629927,37.4815};

    public class Response {
        @JsonProperty("response")
        public Info info;
        public String version;
    }

    public class Info {
        public String text;
        public String tts;
        public Boolean endSession;
    }

    private static String getExistIntent(Iterator<String> iterator){
        String intent = null;
        while (iterator.hasNext()){
            intent = iterator.next();
            if(intent.equals("route") | intent.equals("simple")) break;
        }
        return intent;
    }

    static {
//        DevelopDB.calendarEvents.add(new CalendarEvent("23:58",10,
//                new Double[]{55.0, 37.0, 55.0, 37.1}
//                ,new boolean[]{true,true,true,true, true,true,true}
//                , true)); //for test
    }

    private static String processStringMinutes(long g){
        long last_num = g%10;
        if(g == 0) return "";
        if(last_num == 1 && g != 11) return " "+ g + " минуту";
        if((last_num == 2 || last_num == 3 || last_num == 4) && g != 12) return " "+ g + " минуты";
        return " "+ g + " минут";
    }

    private static String processStringHours(long g){
        long hours = g/60;
        if (hours == 0) return processStringMinutes(g%60);
        if(hours == 1) return ""+hours + " час " + processStringMinutes(g%60);
        if(hours == 2 || hours == 3 || hours == 4) return "" + hours+ " часа " + processStringMinutes(g%60) ;
        return "" + hours + " часов" + processStringMinutes(g%60);
    }
    private String getAddressFromJson(JSONObject t) throws JSONException {
        String address = "";
        Iterator<String> iterator = t.keys();
        while(iterator.hasNext()) address += t.getString(iterator.next()) +" ";
        return address;
    }

    @PostMapping("/api/getInfo")
    public Response addAlarm(@RequestBody String json){
        Response response = new Response();
        response.info = new Info();
        try{
            JSONObject t = new JSONObject(json)
                    .getJSONObject("request")
                    .getJSONObject("nlu")
                    .getJSONObject("intents");
            String intent = getExistIntent(t.keys());
            switch (intent) {
                case "simple", "route":
                    String mode = t.getJSONObject(intent)
                            .getJSONObject("slots")
                            .getJSONObject("mode")
                            .getString("value");

                    long min = calculateRouterService.responceForAlice(mode);

                    if (min != 0) {
                        String format = processStringHours(min);
                        response.info.text = "Время через которое нужно выходить " + format;
                        response.info.tts = "Время через которое нужно выход+ить " + format;
                        response.info.endSession = true;
                    } else { //маршрут ещё актуален, но нет времени
                        response.info.text = mode + " вы не успеваете, воспользуйтесь другим транспортным средством";
                        response.info.tts = mode + " вы не успеваете воспользуйтесь другим транспортным средством";
                        response.info.endSession = false;
                    }
                    break;
                case "set_from":
                    String fromAddress = getAddressFromJson(t.getJSONObject(intent)
                            .getJSONObject("slots")
                            .getJSONObject("from")
                            .getJSONObject("value")
                    );

                    Double fromGeo[] = parsingAddress.parseAddress(fromAddress);

                    geoFrom = fromGeo;
                    response.info.text = " точка начала маршрута по умолчанию установлена";
                    response.info.tts = " точка начала маршрута по умолчанию установлена";
                    response.info.endSession = false;

                    break;

                case "create_route": //создать маршрут + задать время
                    String toAddress = getAddressFromJson(t.getJSONObject(intent)
                            .getJSONObject("slots")
                            .getJSONObject("from")
                            .getJSONObject("value")
                    );
                    JSONObject timeJson = t.getJSONObject(intent)
                            .getJSONObject("slots")
                            .getJSONObject("time")
                            .getJSONObject("value");
                    String time = String.format("%d:%d",timeJson.getInt("hour"), timeJson.getInt("minute"));
                    Double geo[] = parsingAddress.parseAddress(toAddress);

                    response.info.text = " установлен маршрут на " + time;
                    response.info.tts = " установлен маршрут на " + time;
                    response.info.endSession = true     ;


                    DevelopDB.calendarEvents.add(
                            new CalendarEvent(
                                    time,
                                    1000 + DevelopDB.id++,
                                    new Double[]{geoFrom[0],geoFrom[1],geo[0],geo[1]},
                                    new boolean[]{true,true,true,true,true,true,true},
                                    true,
                                    "Безымянное событие"
                            )
                    );
                    break;


                case "all_transport"://скажи оставшееся ближайшее время до выхода
            }
        }
        catch (JSONException | NullPointerException e){ //не распознали ответ
            response.info.text = "Скажите что хотите узнать";
            response.info.tts = response.info.text;
            response.info.endSession = false;
        }
        catch (NoSuchElementException e){//нет маршрутов
            response.info.text = "Нет установленных маршрутов на текущий день";
            response.info.tts = response.info.text;
            response.info.endSession = false;
        }
        response.version = "1.0";
        System.out.println(response.info.text);
        return response;
    }
}
