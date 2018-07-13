package com.qinjun.autotest.tsdiff.po.db.data;

import com.alibaba.fastjson.JSON;
import com.qinjun.autotest.tsdiff.exception.TSDiffException;
import com.qinjun.autotest.tsdiff.po.csv.data.CSV;
import com.qinjun.autotest.tsdiff.po.csv.info.CSVInfo;
import com.qinjun.autotest.tsdiff.po.db.info.DBInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DBContainer {
    private final static Logger logger = LoggerFactory.getLogger(DBContainer.class);
    Map<String,DB> dbMap;


    public DBContainer(String dbInfoFile) throws TSDiffException {
        try {
            String csvInfoFileContent = FileUtils.readFileToString(new File(dbInfoFile));
            List<DBInfo> dbInfoList = JSON.parseArray(csvInfoFileContent,DBInfo.class);
            for (DBInfo dbInfo : dbInfoList) {
                String name = dbInfo.getName();
                DB db = new DB(dbInfo);
                dbMap.put(name,db);
            }
        }
        catch (Exception e) {
            throw new TSDiffException("Get Exception:"+e);
        }
    }

    public Map<String, DB> getDbMap() {
        return dbMap;
    }

    public void setDbMap(Map<String, DB> dbMap) {
        this.dbMap = dbMap;
    }
}
