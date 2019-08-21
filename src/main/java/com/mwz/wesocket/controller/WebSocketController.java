package com.mwz.wesocket.controller;

import com.mwz.wesocket.util.WebSocketUtils;

import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wzm
 * @date 2019年08月21日 17:04
 */

@ServerEndpoint(value = "/websocket/chat/{user_nick}")
@Slf4j
@Component
public class WebSocketController {

    @OnOpen
    public void onOpen(@PathParam(value = "user_nick") String userNick, Session session) {
        String message = "有新游客["+userNick+"]加入聊天室";
        log.info("新的连接消息{}",message);
        WebSocketUtils.addSession(userNick,session);
        //此时可向所有在线用户通知message
        WebSocketUtils.sendMessageForAll(message);
    }

    @OnClose
    public void onClose(@PathParam(value = "user_nick") String userNick,Session session) {
        String message = "游客[" + userNick + "]退出聊天室!";
        log.info(message);
        WebSocketUtils.remoteSession(userNick);
        //此时可向所有的在线通知 message
        WebSocketUtils.sendMessageForAll(message);
    }

    @OnMessage
    public void OnMessage(@PathParam(value = "user_nick") String userNick, String message) {
        //类似群发
        String info = "游客[" + userNick + "]：" + message;
        log.info(info);
        WebSocketUtils.sendMessageForAll(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("异常:", throwable);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }
}
