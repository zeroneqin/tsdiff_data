package com.qinjun.autotest.tsdiff.po.csv.data;

import au.com.bytecode.opencsv.CSVReader;
import com.qinjun.autotest.tsdiff.exception.TSDiffException;
import com.qinjun.autotest.tsdiff.po.csv.info.CSVInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSV {
    private final static Logger logger = LoggerFactory.getLogger(CSV.class);
    String name;
    String path;
    String priKey;
    String column;

    CSVHeader csvHeaderLc;
    CSVHeader csvHeader;
    CSVBody csvBody;


    public CSV(CSVInfo csvInfo) throws TSDiffException {
         name = csvInfo.getName();
         path = csvInfo.getPath();
         priKey = csvInfo.getPriKey();
         column = csvInfo.getColumn();


        String[] priKeys=null;
        String[] columns=null;

        if (!StringUtils.isEmpty(priKey)) {
            priKeys = priKey.split(",");
        }

        if (!StringUtils.isEmpty(column)) {
            columns = column.split(",");
        }

        try {
            CSVReader  csvReader = new CSVReader(new FileReader(path));
            List<String[]> csvContent = csvReader.readAll();
            csvReader.close();
            csvHeader = new CSVHeader(csvContent.get(0));
            csvHeaderLc = getCSVHeaderLc(csvHeader);

            csvBody = new CSVBody(csvContent.subList(1,csvContent.size()), csvHeaderLc, priKeys, columns);
        }
        catch (Exception e) {
            throw new TSDiffException("Get exception when init csv body:"+e);
        }
    }

    private CSVHeader getCSVHeaderLc(CSVHeader csvHeader) {
        CSVHeader csvHeaderLc = null;
        csvHeaderLc = csvHeader.clone();

        Map<String,Integer> field2Index = csvHeaderLc.getField2Index();
        Map<String,Integer> field2IndexLc = new HashMap<String,Integer>();
        for (String key : field2Index.keySet())  {
            field2IndexLc.put(key.toLowerCase(),field2Index.get(key));
        }

        csvHeaderLc.setField2Index(field2IndexLc);

        Map<Integer,String> index2Field = csvHeaderLc.getIndex2Field();
        Map<Integer,String> index2FieldLc = new HashMap<Integer,String>();
        for (Integer key: index2Field.keySet()) {
            index2FieldLc.put(key,index2Field.get(key).toLowerCase());
        }

        csvHeaderLc.setIndex2Field(index2FieldLc);

        return csvHeaderLc;
    }
    public static Logger getLogger() {
        return logger;
    }

    public CSVHeader getCsvHeaderLc() {
        return csvHeaderLc;
    }

    public void setCsvHeaderLc(CSVHeader csvHeaderLc) {
        this.csvHeaderLc = csvHeaderLc;
    }

    public CSVHeader getCsvHeader() {
        return csvHeader;
    }

    public void setCsvHeader(CSVHeader csvHeader) {
        this.csvHeader = csvHeader;
    }

    public CSVBody getCsvBody() {
        return csvBody;
    }

    public void setCsvBody(CSVBody csvBody) {
        this.csvBody = csvBody;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
