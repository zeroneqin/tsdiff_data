package com.qinjun.autotest.tsdiff.comparer;

import com.qinjun.autotest.tsdiff.exception.TSDiffException;
import com.qinjun.autotest.tsdiff.po.csv.data.CSV;
import com.qinjun.autotest.tsdiff.po.csv.data.CSVBody;
import com.qinjun.autotest.tsdiff.po.csv.data.CSVContainer;
import com.qinjun.autotest.tsdiff.po.csv.data.CSVRow;
import com.qinjun.autotest.tsdiff.po.db.data.DB;
import com.qinjun.autotest.tsdiff.po.db.data.DBContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DB2CSVComparer implements Comparable {
    private final static Logger logger = LoggerFactory.getLogger(DB2CSVComparer.class);
    DBContainer dbContainer;
    CSVContainer csvContainer;

    public DB2CSVComparer(String left, String right) throws TSDiffException {
        dbContainer = new DBContainer(left);
        csvContainer = new CSVContainer(right);
    }

    @Override
    public boolean compare() {
        boolean result = true;
        logger.info("Start to compare data");
        List<String> tableLeftMoreRightError = new ArrayList<String>();
        List<String> tableRightMoreLeftError = new ArrayList<String>();
        List<String> pkLeftMoreRightError = new ArrayList<String>();
        List<String> pkRightMoreLeftError = new ArrayList<String>();
        List<String> columnLeftMoreRightError = new ArrayList<String>();
        List<String> columnRightMoreLeftError = new ArrayList<String>();
        List<String> dataDiffError = new ArrayList<String>();

        try {
            Map<String, DB> dbMap = dbContainer.getDbMap();
            Map<String, CSV> csvMap = csvContainer.getCsvMap();
            logger.info("Start to compare right data based on left");
            for (String name : dbMap.keySet()) {
                if (!csvMap.containsKey(name)) {
                    tableLeftMoreRightError.add(name);
                } else {
                    DB db = dbMap.get(name);
                    CSV csv = csvMap.get(name);
                    Map<String, Map<String, Object>> dbData = db.getDbDataMap();
                    CSVBody csvBody = csv.getCsvBody();
                    Map<String, CSVRow> csvData = csvBody.getCsvBodyMap();
                    for (String pk : dbData.keySet()) {
                        if (!csvData.containsKey(pk)) {
                            String errorLine = name + "->" + pk;
                            pkLeftMoreRightError.add(errorLine);
                        } else {
                            Map<String, Object> dbDataRow = dbData.get(pk);
                            CSVRow csvRow = csvData.get(pk);

                            for (String column : dbDataRow.keySet()) {
                                if (!csvRow.getCellMap().containsKey(column)) {
                                    String error = name + "->" + pk + "->" + column;
                                    columnLeftMoreRightError.add(error);
                                } else {
                                    String dbValue = (String) dbDataRow.get(column);
                                    String csvValue = csvRow.getCell(column);

                                    if (csvValue.equalsIgnoreCase(dbValue)) {
                                        String error = name + "->" + pk + "->" + column + "->" + dbValue + "<>" + csvValue;
                                        dataDiffError.add(error);
                                    }
                                }
                            }
                        }
                    }

                }
            }

            for (String name : csvMap.keySet()) {
                if (!dbMap.containsKey(name)) {
                    tableRightMoreLeftError.add(name);
                } else {
                    DB db = dbMap.get(name);
                    CSV csv = csvMap.get(name);
                    Map<String, Map<String, Object>> dbData = db.getDbDataMap();
                    CSVBody csvBody = csv.getCsvBody();
                    Map<String, CSVRow> csvData = csvBody.getCsvBodyMap();
                    for (String pk : csvData.keySet()) {
                        if (!dbData.containsKey(pk)) {
                            String errorLine = name + "->" + pk;
                            pkRightMoreLeftError.add(errorLine);
                        } else {
                            Map<String, Object> dbDataRow = dbData.get(pk);
                            CSVRow csvRow = csvData.get(pk);

                            for (String column : csvRow.getCellMap().keySet()) {
                                if (!dbDataRow.containsKey(column)) {
                                    String error = name + "->" + pk + "->" + column;
                                    columnRightMoreLeftError.add(error);
                                } else {
                                }
                            }
                        }
                    }

                }
            }
        }
        catch (Exception e) {
            logger.error("Get exception :"+e);
            result = false;
        }

        return result;
    }


}
