package com.mwz.wesocket.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.Session;

/**
 * @author wzm
 * @date 2019年08月21日 17:13
 *
 * https://github.com/hua1995116/webchat/ 参考项目地址
 *
 */
public class WebSocketUtils {

    /**
     * 简单实用map来存储在线session
     */
    private static final Map<String,Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * onOpen时增加session
     */
    public static void addSession(String userNick, Session session) {
        /**putIfAbsent 添加键—值对的时候，先判断该键值对是否已经存在
         * 不存在：新增，并返回null
         * 存在：不覆盖，直接返回已存在的值
        */
        ONLINE_SESSION.putIfAbsent(userNick,session);

    }

    /**
     * onClose时删除session
     * @param userNick
     */
    public static void remoteSession(String userNick) {
        ONLINE_SESSION.remove(userNick);
    }

    /**
     * 为所有session发送message
     * @param message
     */
    public static void sendMessageForAll(String message) {
        ONLINE_SESSION.forEach((sessionId,session)->{sendMessage(session,message);});
    }

    /**
     *为session发送message
     */
    public static void sendMessage(Session session, String message) {
        if(session == null) {
            return;
        }
        // getAsyncRemote()和getBasicRemote()异步与同步
        Async async = session.getAsyncRemote();
        //发送消息
        async.sendText(message);
    }




}
