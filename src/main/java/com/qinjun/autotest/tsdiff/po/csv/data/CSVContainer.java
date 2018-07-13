package com.qinjun.autotest.tsdiff.po.csv.data;

import com.alibaba.fastjson.JSON;
import com.qinjun.autotest.tsdiff.exception.TSDiffException;
import com.qinjun.autotest.tsdiff.po.csv.info.CSVInfo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVContainer {
    private final static Logger logger = LoggerFactory.getLogger(CSVContainer.class);
    Map<String,CSV> csvMap;


    public CSVContainer(String csvInfoFile) throws TSDiffException {
        try {
            String csvInfoFileContent = FileUtils.readFileToString(new File(csvInfoFile));
            List<CSVInfo> csvInfoList = JSON.parseArray(csvInfoFileContent,CSVInfo.class);
            for (CSVInfo csvInfo : csvInfoList) {
                String name = csvInfo.getName();
                CSV csv = new CSV(csvInfo);
                csvMap.put(name,csv);
            }
        }
        catch (Exception e) {
            throw new TSDiffException("Get Exception:"+e);
        }
    }


    public Map<String, CSV> getCsvMap() {
        return csvMap;
    }

    public void setCsvMap(Map<String, CSV> csvMap) {
        this.csvMap = csvMap;
    }
}
