package com.example.project_iot.controllers;

import com.example.project_iot.Notification;
import com.example.project_iot.RestService;
import com.example.project_iot.models.ResponceRoute;
import com.example.project_iot.repo.FakeDB;
import com.google.gson.Gson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.net.http.WebSocketHandshakeException;
import java.util.NoSuchElementException;

public class MyHandler extends TextWebSocketHandler {
    @Autowired
    Gson gson;
    @Autowired
    Notification notification;
    private int count(ResponceRoute responceRoute){
        int c = 0;
        if (responceRoute.getDriving() == 0) c++;
        if (responceRoute.getTransit() == 0) c++;
        if (responceRoute.getWalking() == 0) c++;
        return c;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException {
        int userId = Integer.parseInt(session.getUri().getPath().replace("/websocket/", ""));
        int lastId = 0;
        int lastCountBeeps = 0;
        boolean beep = false;
        System.out.println("Подключён пользователь с Id:" + userId);
        while (session.isOpen()) {
            try{
                try {
                    ResponceRoute responceRoute = notification.responceForNucleo(3);
                    if ( lastId != responceRoute.getIdAlarm()) {// и не был изменён
                        lastCountBeeps = 0;
                        beep = false;
                    }

                    int t = count(responceRoute);
                    if (!beep && t>lastCountBeeps) {
                        lastCountBeeps = t;
                        responceRoute.setBeep(1);
                        beep = true;
                    }
                    //
                    String res = gson.toJson(responceRoute);
                    session.sendMessage(new TextMessage(res));
                    System.out.println(res);
                    //
                    lastId = responceRoute.getIdAlarm();

                }
                catch (NoSuchElementException e) {
                    System.out.println("Нет будильников " + e.getMessage());
                    String res = "{\"walking\":-1,\"transit\":-1,\"driving\":-1}";//В случае если нет будильников
                    session.sendMessage(new TextMessage(res));
                }

            }
            catch (IOException e) {
                System.out.println("Не удалось " + e.getMessage());
                break;
            }
            Thread.sleep(20000);
        }
    }

}