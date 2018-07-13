package com.qinjun.autotest.tsdiff.po.csv.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class CSVHeader implements Cloneable {
    private final static Logger logger = LoggerFactory.getLogger(CSVHeader.class);

    Map<String,Integer> field2Index;
    Map<Integer,String> index2Field;

    public CSVHeader(final String[] headers) {
        field2Index = new LinkedHashMap<String,Integer>();
        index2Field = new LinkedHashMap<Integer,String>();

        for (int i=0;i< headers.length; i++) {
            field2Index.put(headers[i].toLowerCase(),i);
            index2Field.put(i,headers[i].toLowerCase());
        }
    }

    public int getFieldIndex(String field) {
        int index = -1;
        if (field2Index.containsKey(field)) {
            index = field2Index.get(field);
        }
        return index;
    }

    public String getIndexField(int index) {
        String field=null;
        if (index2Field.containsKey(index)) {
            field = index2Field.get(index);
        }
        return field;
    }

    @Override
    public CSVHeader clone() {
        CSVHeader csvHeader = null;
        try {
            csvHeader = (CSVHeader) super.clone();
        }
        catch (CloneNotSupportedException cnse) {
            logger.error("Clone CSVHeader fail");
        }

        csvHeader.field2Index = new LinkedHashMap<String,Integer>();
        for (String key: field2Index.keySet()) {
            csvHeader.field2Index.put(new String(key),new Integer(field2Index.get(key)));
        }

        csvHeader.index2Field = new LinkedHashMap<Integer,String>();
        for (Integer index:index2Field.keySet()) {
            csvHeader.index2Field.put(new Integer(index),new String(index2Field.get(index)));
        }
        return csvHeader;
    }

    public Map<String, Integer> getField2Index() {
        return field2Index;
    }

    public void setField2Index(Map<String, Integer> field2Index) {
        this.field2Index = field2Index;
    }

    public Map<Integer, String> getIndex2Field() {
        return index2Field;
    }

    public void setIndex2Field(Map<Integer, String> index2Field) {
        this.index2Field = index2Field;
    }
}
