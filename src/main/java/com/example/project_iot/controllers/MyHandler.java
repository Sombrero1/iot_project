package com.example.project_iot.controllers;

import com.example.project_iot.Notification;
import com.example.project_iot.RestService;
import com.example.project_iot.repo.FakeDB;
import com.google.gson.Gson;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.net.http.WebSocketHandshakeException;

public class MyHandler extends TextWebSocketHandler {
    @Autowired
    Gson gson;
    @Autowired
    Notification notification;


    @Override
    public void handleTextMessage( WebSocketSession session, TextMessage message) throws IOException, InterruptedException {
        int userId = Integer.parseInt(session.getUri().getPath().replace("/websocket/", ""));
        System.out.println("Подключён пользователь с Id:" + userId);
        while (true) {
            try {
                String res = gson.toJson(notification.responceForNucleo(3));
                session.sendMessage(new TextMessage(res));
                System.out.println(res);
            }
            catch (IOException e) {
                session.close();
                System.out.println("Не удалось " + e.getMessage());
                break;
            }
            Thread.sleep(20000);
        }
    }

}