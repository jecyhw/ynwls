package com.cn.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SNNU on 2015/5/6.
 */
@ServerEndpoint(value="/ShowUserByRealTime")
public class ShowUserByRealTime {
    public static boolean isNeedNotify = false;//当用户更新位置时是否需要通知显示
    private static  final Set<ShowUserByRealTime> connections = new CopyOnWriteArraySet<ShowUserByRealTime>();
    private Session session;

    @OnMessage
    public void receive(String msg) {
    }

    @OnOpen
    public void start(Session session) {
        isNeedNotify = true;
        this.session = session;
        connections.add(this);
    }

    @OnClose
    public void end() {
        connections.remove(this);
        if (connections.isEmpty()) {
            isNeedNotify = false;
        }
    }

    public static void broadcast(String msg)
    {
        for (ShowUserByRealTime client : connections)
        {
            try {
                client.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
