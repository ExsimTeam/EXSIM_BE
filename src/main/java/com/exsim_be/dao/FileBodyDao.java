package com.exsim_be.dao;


import com.exsim_be.vo.returnVo.FileInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-20
 */
@Component
public class FileBodyDao {
    @Autowired
    MongoTemplate mongoTemplate;

    private final static int pageSize=50;

    public FileInfoVo getFileInfo(long fileId) {
        String collectionName="F"+fileId;
        Query query=new Query(Criteria.where("info").is(1));
        FileInfoVo fileInfoVo=mongoTemplate.findOne(query,FileInfoVo.class,collectionName);
        return fileInfoVo;
    }

    public void saveCell(long fileId,int sheetId,Object cellJson, long row, long col){
        String collectionName="F"+fileId;
        Query query=new Query(Criteria.where("row").is(row).and("sheet").is(sheetId));
        Update update=new Update().set(Long.toString(col), cellJson);
        mongoTemplate.upsert(query,update,collectionName);
    }


    public List<Object> queryCell(long fileId,int sheetId,int page){
        String collectionName="F"+fileId;
        int startIndex=(page-1)*pageSize;
        Query query=new Query(Criteria.where("row").gt(startIndex).lt(startIndex+pageSize).and("sheet").is(sheetId));
        List<Object> jsonObjects = mongoTemplate.find(query, Object.class, collectionName);
        return jsonObjects;
    }

    public void deleteFile(long fileId){
        String collectionName="F"+fileId;
        mongoTemplate.dropCollection(collectionName);
    }


    public void addNewFile(long id) {
        String collectionName="F"+id;
        mongoTemplate.createCollection(collectionName);
        FileInfoVo fileInfoVo=new FileInfoVo();
        Map<Integer,String> sheets=new HashMap<>();
        sheets.put(0,"工作表1");
        fileInfoVo.setSheets(sheets);
        mongoTemplate.insert(fileInfoVo,collectionName);
    }


    public Object addSheet(long fileId, String sheetName,FileInfoVo fileInfo) {
        String collectionName="F"+fileId;
        int sheetId=fileInfo.getSheetPtr();
        fileInfo.addSheet(sheetName);
        Query query=new Query(Criteria.where("info").is(1));
        Update update=new Update();
        update.inc("sheetNum",1);
        update.set("sheet",fileInfo.getSheets());
        update.inc("sheetPtr",1);
        mongoTemplate.upsert(query,update,collectionName);
        return sheetId;
    }

    public void deleteSheet(long fileId,int sheetId,FileInfoVo fileInfo) {
        String collectionName="F"+fileId;
        Query query=new Query(Criteria.where("sheetId").is(sheetId));
        mongoTemplate.remove(query,collectionName);
        fileInfo.deleteSheet(sheetId);
        query=new Query(Criteria.where("info").is(1));
        Update update=new Update();
        update.inc("sheetNum",-1);
        update.set("sheet",fileInfo.getSheets());
        mongoTemplate.upsert(query,update,collectionName);

    }

    public void alterSheetName(long fileId, int sheetID, String sheetName) {

    }
}
