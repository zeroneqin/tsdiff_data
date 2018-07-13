package com.qinjun.autotest.tsdiff.po.csv.data;

import java.util.Map;

public class CSVRow {
    Map<String,String> cellMap;

    public CSVRow(String[] rows, Map<Integer, String> index2Field) {
        for (int i=0;i<rows.length;i++) {
            cellMap.put(index2Field.get(i),rows[i]);
        }
    }

    public Map<String, String> getCellMap() {
        return cellMap;
    }

    public void setCellMap(Map<String, String> cellMap) {
        this.cellMap = cellMap;
    }

    public String getCell(String field) {
        return cellMap.get(field);
    }
}
