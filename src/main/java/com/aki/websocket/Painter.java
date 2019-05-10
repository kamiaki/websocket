package com.aki.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * webSocket连接，实现实时画画功能。
 * 当画家画完之后，接收画家画布消息，发送给每一个画家。
 * by Aki 2019 05 10
 */
@ServerEndpoint(value = "/paint")
@Component
public class Painter {
    private static String canvasMsg = "initCanvas";  //画板信息
    private static CopyOnWriteArraySet<Painter> painters = new CopyOnWriteArraySet<>();  //用户集合（线程安全）
    private Session painterSession;//某用户的会话

    @OnOpen
    public void onOpen(Session session) {
        try {
            this.painterSession = session;
            painters.add(this);             //加入画家们中
            sendMessage(canvasMsg);        //发送画板信息
        }catch (Exception e){
            System.out.println("建立连接异常");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        try {
            painters.remove(this);      //从画家们中删除
        }catch (Exception e){
            System.out.println("关闭连接异常");
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            canvasMsg = message;           //写入画板数据
            sendEveryBody(message);
        }catch (Exception e){
            System.out.println("接收消息异常");
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("服务器异常");
        error.printStackTrace();
    }

    //发送消息
    public void sendMessage(String message) {
        try {
            this.painterSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            System.out.println("发送消息异常");
            e.printStackTrace();
        }
    }

    //群发消息
    public static void sendEveryBody(String message) {
        for (Painter painter : painters) {
            try {
                painter.sendMessage(message);
            } catch (Exception e) {
                System.out.println("群发消息异常");
                e.printStackTrace();
                continue;
            }
        }
    }
}