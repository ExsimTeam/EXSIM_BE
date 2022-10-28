package com.exsim_be.endpoint;

import com.alibaba.fastjson.JSON;
import com.exsim_be.dao.FileBodyDao;
import com.exsim_be.service.ThreadService;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import com.exsim_be.vo.returnVo.FileInfoVo;
import com.exsim_be.vo.websocketVo.AlterSheetNameParam;
import com.exsim_be.vo.websocketVo.CellVo;
import com.exsim_be.vo.FilePermissionVo;
import com.exsim_be.vo.websocketVo.MessageVo;
import com.exsim_be.vo.websocketVo.ReceiveMessageVo;
import io.netty.handler.codec.MessageAggregationException;
import io.netty.handler.ssl.OpenSslCertificateCompressionAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
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

    @Autowired
    ThreadService threadService;

    @Autowired
    FileBodyDao fileBodyDao;

    private FileInfoVo fileInfo;


    @OnOpen()
    public void onOpen(Session session){

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String utoken=requestParameterMap.get("utoken").get(0);
        String filePermissonVoJson=redisTemplate.opsForValue().get("UTOKEN:"+utoken);
        if(filePermissonVoJson==null){
            sendError(session, GlobalCodeEnum.UNAUTHORIZED.getCode(), GlobalCodeEnum.UNAUTHORIZED.getMsg());
            return;
        }
        this.filePermissionVo= JSON.parseObject(filePermissonVoJson,FilePermissionVo.class);
        fileSessions.putIfAbsent(filePermissionVo.getFileId(), new HashSet<>());
        sessionSet=fileSessions.get(filePermissionVo.getFileId());
        sessionSet.add(session);
        fileInfo=fileBodyDao.getFileInfo(filePermissionVo.getFileId());
        session.getAsyncRemote().sendText(JSON.toJSONString(MessageVo.succ(-1,null)));
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
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error",e);
        }
    }

    @OnMessage
    public void onMessage(Session session,String message){
        //check permisson
        if(filePermissionVo.getPermission()==0){
            sendError(session, GlobalCodeEnum.UNAUTHORIZED.getCode(), GlobalCodeEnum.UNAUTHORIZED.getMsg());
            return;
        }
        ReceiveMessageVo receiveMessageVo= JSON.parseObject(message,ReceiveMessageVo.class);
        //check message
        if(receiveMessageVo==null){
            sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
            return;
        }
        int opcode= receiveMessageVo.getOpcode();
        MessageVo messageVo;

        if(opcode==0) {//update cell
            //check cell
            CellVo cellVo = JSON.parseObject(receiveMessageVo.getData(), CellVo.class);
            if (cellVo == null) {
                sendError(session, GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData());
            //store in mongoDB
            threadService.storeInDB(filePermissionVo.getFileId(),cellVo);
        }else if(opcode==1){//add sheet
            messageVo=MessageVo.succ(opcode,fileBodyDao.addSheet(filePermissionVo.getFileId(),receiveMessageVo.getData(),fileInfo));
        }else if(opcode==2){//deleteSheet
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData());
            int sheetId;
            try {
                sheetId = Integer.parseInt(receiveMessageVo.getData());
            }catch (NumberFormatException e){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            threadService.deleteSheet(filePermissionVo.getFileId(),sheetId,fileInfo);
        }else if(opcode==3){//alter sheet name
            AlterSheetNameParam alterSheetNameParam=JSON.parseObject(receiveMessageVo.getData(),AlterSheetNameParam.class);
            if(alterSheetNameParam==null){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData());
            //alter info in mongodb
            threadService.alterSheetName(filePermissionVo.getFileId(), alterSheetNameParam);
            Map<Integer, String> sheets = fileInfo.getSheets();
            if(!sheets.containsKey(alterSheetNameParam.getSheetID())){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            sheets.put(alterSheetNameParam.getSheetID(), alterSheetNameParam.getSheetName());
        }
        else {
            sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
            return;
        }

        //send messsage
        for(Session partner:sessionSet){
            if(partner!=session) {
                partner.getAsyncRemote().sendText(JSON.toJSONString(messageVo));
            }
        }
        session.getAsyncRemote().sendText(JSON.toJSONString(MessageVo.succ(-1,null)));
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

    private void sendError(Session session,int code,String message){
        MessageVo messageVo=MessageVo.fail(code,message);
        session.getAsyncRemote().sendText(JSON.toJSONString(messageVo));
    }
}
