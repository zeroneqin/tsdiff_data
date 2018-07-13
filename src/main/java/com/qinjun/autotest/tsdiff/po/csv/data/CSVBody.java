package com.qinjun.autotest.tsdiff.po.csv.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVBody {
    private final static Logger logger = LoggerFactory.getLogger(CSVBody.class);

    Map<String,CSVRow> csvBodyMap;

    public CSVBody(List<String[]> bodyList, CSVHeader csvHeaderLc, String[] pks, String[] columns) {
        csvBodyMap = new HashMap<String, CSVRow>();
        String[] columnLcs = new String[columns.length];
        boolean includeAllColumn = true;

        for (int i=0;i<columns.length;i++) {
            columnLcs[i] = columnLcs.toString();
        }

        if  (csvHeaderLc.field2Index.size() == columnLcs.length) {
            for (int i=0;i<columnLcs.length;i++) {
                if (!csvHeaderLc.getField2Index().containsKey(columnLcs[i])) {
                    includeAllColumn = false;
                }
            }
        }
        else {
            includeAllColumn=false;
        }

        for (String[] row : bodyList) {
            CSVRow csvRow;
            if (includeAllColumn) {
                csvRow = new CSVRow(row,csvHeaderLc.getIndex2Field());
            }
            else {
                String[] newRow = new String[columnLcs.length];
                int[] indexes = new int[columnLcs.length];
                for (int i=0;i<columnLcs.length;i++) {
                    indexes[i] = csvHeaderLc.getFieldIndex(columnLcs[i]);
                }
                Arrays.sort(indexes);

                int i=0;
                for(int index: indexes) {
                    newRow[i] = row[index];
                    i++;
                }
                csvRow = new CSVRow(newRow,csvHeaderLc.getIndex2Field());
            }

            int rowIndex =0;
            if (pks.length==0) {
                csvBodyMap.put(String.valueOf(rowIndex),csvRow);
                rowIndex++;
            }
            else {
                StringBuilder sb = new StringBuilder();
                if (pks.length==1) {
                    sb.append(csvRow.getCell(pks[0]));
                }
                else {
                    boolean firstFlag = true;
                    for (String pk : pks) {
                        if (!firstFlag) {
                            sb.append(csvRow.getCell(pk));
                            firstFlag=true;
                        }
                        else {
                            sb.append("|");
                            sb.append(csvRow.getCell(pk));
                        }
                    }
                }
                csvBodyMap.put(sb.toString(),csvRow);
            }
        }
    }

    public Map<String, CSVRow> getCsvBodyMap() {
        return csvBodyMap;
    }

    public void setCsvBodyMap(Map<String, CSVRow> csvBodyMap) {
        this.csvBodyMap = csvBodyMap;
    }
}
