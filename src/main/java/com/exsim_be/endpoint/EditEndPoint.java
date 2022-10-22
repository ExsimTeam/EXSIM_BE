package com.exsim_be.endpoint;

import com.alibaba.fastjson.JSON;
import com.exsim_be.entity.User;
import com.exsim_be.vo.FilePermissionVo;
import com.exsim_be.vo.returnVo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
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
    RedisTemplate<String,String> redisTemplate;

    private static final Map<Long,Set<Session>> fileSessions=new ConcurrentHashMap<>();

    private FilePermissionVo filePermissionVo;
    private Set<Session> sessionSet;

    @OnOpen()
    public void onOpen(Session session){

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String utoken=requestParameterMap.get("utoken").get(0);
        String filePermissonVoJson=redisTemplate.opsForValue().get(utoken);
        if(filePermissonVoJson==null){
            session.getAsyncRemote().sendText("fail to establish connection!");
            onClose(session);
            return;
        }
        this.filePermissionVo= JSON.parseObject(filePermissonVoJson,FilePermissionVo.class);
        fileSessions.putIfAbsent(filePermissionVo.getFileId(), new HashSet<>());
        sessionSet=fileSessions.get(filePermissionVo.getFileId());
        sessionSet.add(session);
        session.getAsyncRemote().sendText("connect success!");
    }

    @OnClose
    public void onClose(Session session){
        try {
            //关闭WebSocket下的该Seesion会话
            session.close();
            sessionSet.remove(session);
            if(sessionSet.size()==0){
                fileSessions.remove(filePermissionVo.getFileId());
            }
            log.info("断开连接力");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error",e);
        }
    }

    @OnMessage
    public void onMessage(Session session,String message){
        if(filePermissionVo.getPermission()==0){
            session.getAsyncRemote().sendText("no authorization to write");
            return;
        }
        //message check

        //send message to other member
        MessageVo messageVo=new MessageVo(message, filePermissionVo.getUsername());
        for(Session partner:sessionSet){
            partner.getAsyncRemote().sendObject(messageVo);
        }
        //后台保存

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
