package com.exsim_be.dao;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.util.List;

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

    public void saveCell(long fileId,int sheetId,String cellJson, int row, Integer col){
        String collectionName="F"+fileId;
        JSON.parse(cellJson);
        Query query=new Query(Criteria.where("row").is(row).and("sheet").is(sheetId));
        Update update=new Update().set(col.toString(), JSON.parse(cellJson));
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

    public void addSheet(String sheetName){

    }




}
