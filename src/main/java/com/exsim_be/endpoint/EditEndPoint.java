package com.exsim_be.endpoint;

import com.exsim_be.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-12
 */
@Slf4j
@Component
@ServerEndpoint("/edit")
public class EditEndPoint {
    @Autowired
    RedisTemplate redisTemplate;

    private static Map<Long,Set<EditEndPoint>> fileSessions=new ConcurrentHashMap<>();

    private Session session;
    private User user;
    private Long fileId;

    @OnOpen()
    public void onOpen(Session session){

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String utoken=requestParameterMap.get("utoken").get(0);
        log.info("用户utoken:{} connected",utoken);
    }

    @OnClose
    public void onClose(Session session){
        try {
            //关闭WebSocket下的该Seesion会话
            session.close();
            log.info("断开连接力");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error",e);
        }
    }

    @OnMessage
    public void onMessage(Session session,String message){
        log.info("消息:{}",message);
        session.getAsyncRemote().sendText("收到消息力");
    }

    @OnError
    public void onError(Session session,Throwable throwable){
        try {
            //关闭WebSocket下的该Seesion会话
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onError Exception",e);
        }
        log.info("Throwable msg " + throwable.getMessage());
    }
}
