package com.exsim_be.service;

import com.exsim_be.dao.FileBodyDao;
import com.exsim_be.vo.returnVo.FileInfoVo;
import com.exsim_be.vo.websocketVo.AlterSheetNameParam;
import com.exsim_be.vo.websocketVo.CellVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-11-01
 */
@Component
public class WebsocketService {
    @Autowired
    ThreadService threadService;

    @Autowired
    FileBodyDao fileBodyDao;
    @Autowired
    RedisTemplate<String,String> redisTemplate;


    public String getFilePermissionVoJSON(String utoken){
        return redisTemplate.opsForValue().get("UTOKEN:"+utoken);
    }


    public FileInfoVo getFileInfo(long fileId) {
        return fileBodyDao.getFileInfo(fileId);
    }

    public void storeInDB(long fileId, CellVo cellVo) {
        threadService.storeInDB(fileId,cellVo);
    }


    public Object addSheet(long fileId, String data, FileInfoVo fileInfo) {
        return fileBodyDao.addSheet(fileId,data,fileInfo);
    }

    public void deleteSheet(long fileId, int sheetId, FileInfoVo fileInfo) {
        threadService.deleteSheet(fileId,sheetId,fileInfo);
    }

    public void alterSheetName(long fileId, AlterSheetNameParam alterSheetNameParam) {
        threadService.alterSheetName(fileId, alterSheetNameParam);
    }
}
