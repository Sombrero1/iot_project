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
import java.util.NoSuchElementException;

public class MyHandler extends TextWebSocketHandler {
    @Autowired
    Gson gson;
    @Autowired
    Notification notification;

    @Override
    public void handleTextMessage( WebSocketSession session, TextMessage message) throws InterruptedException {
        int userId = Integer.parseInt(session.getUri().getPath().replace("/websocket/", ""));
        System.out.println("Подключён пользователь с Id:" + userId);
        while (session.isOpen()) {
            try{
                try {
                    String res = gson.toJson(notification.responceForNucleo(3));//Проверка подключения
                    session.sendMessage(new TextMessage(res));
                    System.out.println(res);
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