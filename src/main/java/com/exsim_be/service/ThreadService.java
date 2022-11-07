package com.exsim_be.service;

import com.exsim_be.dao.FileBodyDao;
import com.exsim_be.vo.returnVo.FileInfoVo;
import com.exsim_be.vo.websocketVo.AlterSheetNameParam;
import com.exsim_be.vo.websocketVo.CellValVo;
import com.exsim_be.vo.websocketVo.CellVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-20
 */
@Component
public class ThreadService {

    @Autowired
    MailService mailService;

    @Autowired
    FileBodyDao fileBodyDao;
    @Async("taskExecutor")
    public void sendEmail(String email,String verifyCode){
        mailService.sendMail(email,verifyCode);
    }


    @Async("taskExecutor")
    public void storeInDB(long fileId,CellVo cellVo) {
        CellValVo cellValVo=new CellValVo(cellVo.getValue(), cellVo.getFormat());
        fileBodyDao.saveCell(fileId,cellVo.getSheetId(), cellValVo, cellVo.getRow(), cellVo.getCol());
    }
    @Async("taskExecutor")
    public void deleteSheet(long fileId, int sheetId, FileInfoVo fileInfo) {
        fileBodyDao.deleteSheet(fileId,sheetId,fileInfo);
    }


    @Async("taskExecutor")
    public void alterSheetName(long fileId,AlterSheetNameParam alterSheetNameParam) {
        fileBodyDao.alterSheetName(fileId,alterSheetNameParam.getSheetID(),alterSheetNameParam.getSheetName());
    }

}
