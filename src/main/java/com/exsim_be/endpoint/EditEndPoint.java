package com.exsim_be.endpoint;

import com.alibaba.fastjson.JSON;

import com.exsim_be.service.WebsocketService;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import com.exsim_be.vo.returnVo.FileInfoVo;
import com.exsim_be.vo.websocketVo.AlterSheetNameParam;
import com.exsim_be.vo.websocketVo.CellVo;
import com.exsim_be.vo.FilePermissionVo;
import com.exsim_be.vo.websocketVo.MessageVo;
import com.exsim_be.vo.websocketVo.ReceiveMessageVo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-12
 */
@Slf4j
@Component
@Controller
@ServerEndpoint("/api/edit")
public class EditEndPoint {


    private static final Map<Long, CopyOnWriteArraySet<Session>> fileSessions=new ConcurrentHashMap<>();

    private FilePermissionVo filePermissionVo;
    private CopyOnWriteArraySet<Session> sessionSet;



    private FileInfoVo fileInfo;


    private static WebsocketService websocketService;

    @Autowired
    public void setWebsocketService(WebsocketService websocketService) {
        EditEndPoint.websocketService = websocketService;
    }


    @OnOpen()
    public void onOpen(Session session) throws IOException{
        session.setMaxTextMessageBufferSize(10*1024*1024);
        session.setMaxBinaryMessageBufferSize(10*1024*1024);

        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        String utoken=requestParameterMap.get("utoken").get(0);
        String filePermissonVoJson= websocketService.getFilePermissionVoJSON(utoken);
        if(filePermissonVoJson==null){
            sendError(session, GlobalCodeEnum.UNAUTHORIZED.getCode(), GlobalCodeEnum.UNAUTHORIZED.getMsg());
            return;
        }
        this.filePermissionVo= JSON.parseObject(filePermissonVoJson,FilePermissionVo.class);
        fileSessions.putIfAbsent(filePermissionVo.getFileId(), new CopyOnWriteArraySet<>());
        sessionSet=fileSessions.get(filePermissionVo.getFileId());
        //notify others someone is coming
        //send messsage
        for(Session partner:sessionSet){
            if(partner!=session) {
                synchronized (partner) {
                    partner.getBasicRemote().sendText(JSON.toJSONString(MessageVo.succ(4, null, filePermissionVo.getUsername())));
                }
            }
        }
        //
        sessionSet.add(session);
        fileInfo=websocketService.getFileInfo(filePermissionVo.getFileId());
        session.getBasicRemote().sendText(JSON.toJSONString(MessageVo.succ(-1,null,null)));
    }

    @OnClose
    public void onClose(Session session) throws IOException{
        try {
            //关闭WebSocket下的该Seesion会话
            session.close();
            sessionSet.remove(session);
            if(sessionSet.size()==0){
                fileSessions.remove(filePermissionVo.getFileId());
            }
            log.info("断联");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("onClose error",e);
        }
    }

    @OnMessage
    public void onMessage(Session session,String message) throws IOException{
        //check permisson
        if(filePermissionVo.getPermission()==0){
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
        if(opcode==-1){
            session.getBasicRemote().sendText(JSON.toJSONString(MessageVo.succ(-1,null,null)));
            return;
        }else if(opcode==0) {//update cell
            //check cell
            CellVo cellVo = JSON.parseObject(receiveMessageVo.getData(), CellVo.class);
            if (cellVo == null) {
                sendError(session, GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData(), filePermissionVo.getUsername());
            //store in mongoDB
            websocketService.storeInDB(filePermissionVo.getFileId(),cellVo);

        }else if(opcode==1){//add sheet
            messageVo=MessageVo.succ(opcode
                    ,websocketService
                            .addSheet(filePermissionVo.getFileId()
                                    ,receiveMessageVo.getData()
                                    ,fileInfo), filePermissionVo.getUsername());
        }else if(opcode==2){//deleteSheet
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData(), filePermissionVo.getUsername());
            int sheetId;
            try {
                sheetId = Integer.parseInt(receiveMessageVo.getData());
            }catch (NumberFormatException e){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            websocketService.deleteSheet(filePermissionVo.getFileId(),sheetId,fileInfo);
        }else if(opcode==3){//alter sheet name
            AlterSheetNameParam alterSheetNameParam=JSON.parseObject(receiveMessageVo.getData(),AlterSheetNameParam.class);
            if(alterSheetNameParam==null){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }
            messageVo=MessageVo.succ(opcode,receiveMessageVo.getData(), filePermissionVo.getUsername());
            Map<Integer, String> sheets = fileInfo.getSheets();
            if(!sheets.containsKey(alterSheetNameParam.getSheetID())){
                sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
                return;
            }

            sheets.put(alterSheetNameParam.getSheetID(), alterSheetNameParam.getSheetName());
            //alter info in mongodb
            websocketService.alterSheetName(filePermissionVo.getFileId(), alterSheetNameParam);

        }else if(opcode==5){
            messageVo=MessageVo.succ(5,receiveMessageVo.getData(), filePermissionVo.getUsername());
        } else {
            sendError(session,GlobalCodeEnum.BAD_REQUEST.getCode(), GlobalCodeEnum.BAD_REQUEST.getMsg());
            return;
        }

        //send messsage
        for(Session partner:sessionSet){
            if(partner!=session) {
                synchronized (partner) {
                    partner.getBasicRemote().sendText(JSON.toJSONString(messageVo));
                }
            }
        }
        session.getBasicRemote().sendText(JSON.toJSONString(MessageVo.succ(-1,null,null)));
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

    private void sendError(Session session,int code,String message) throws IOException{
        MessageVo messageVo=MessageVo.fail(code,message);
        session.getBasicRemote().sendText(JSON.toJSONString(messageVo));
    }
}
