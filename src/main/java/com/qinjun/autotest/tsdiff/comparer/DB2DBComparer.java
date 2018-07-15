package com.qinjun.autotest.tsdiff.comparer;

public class DB2DBComparer {
    private final static Logger logger = LoggerFactory.getLogger(DB2DBComparer.class);
    DBContainer dbContainer;
    DBContainer dbContainer;

    public DB2DBComparer(String left, String right) throws TSDiffException {
        dbContainer = new DBContainer(left);
        dbContainer = new DBContainer(right);
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
            Map<String, DB> leftDbMap = dbContainer.getDbMap();
            Map<String, DB> rightDbMap = dbContainer.getDbMap();
            logger.info("Start to compare right data based on left");
            for (String name : leftDbMap.keySet()) {
                if (!rightDbMap.containsKey(name)) {
                    tableLeftMoreRightError.add(name);
                } else {
                    DB leftDb = leftDbMap.get(name);
                    DB rightDb = rightDbMap.get(name);
                    Map<String, Map<String, Object>> leftDbData = leftDb.getDbDataMap();
                    Map<String, Map<String, Object>> rightDbData = rightDb.getDbDataMap();
                    for (String pk : leftDbData.keySet()) {
                        if (!rightDbData.containsKey(pk)) {
                            String errorLine = name + "->" + pk;
                            pkLeftMoreRightError.add(errorLine);
                        } else {
                            Map<String, Object> leftDbDataRow = leftDbData.get(pk);
                            Map<String, Object> rightDbDataRow = rightDbData.get(pk);

                            for (String column : leftDbDataRow.keySet()) {
                                if (!rightDbDateRow.containsKey(column)) {
                                    String error = name + "->" + pk + "->" + column;
                                    columnLeftMoreRightError.add(error);
                                } else {
                                    String leftDbValue = (String) leftDbDataRow.get(column);
                                    String rightDbValue = (String) rightDbDataRow.get(column);

                                    if (rightDbValue.equalsIgnoreCase(leftDbValue)) {
                                        String error = name + "->" + pk + "->" + column + "->" + leftDbValue + "<>" + rightDbValue;
                                        dataDiffError.add(error);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (String name : rightDbMap.keySet()) {
                if (!leftDbMap.containsKey(name)) {
                    tableRightMoreLeftError.add(name);
                } else {
                    DB leftDb = leftDbMap.get(name);
                    DB rightDb = rightDbMap.get(name);
                    Map<String, Map<String, Object>> leftDbData = leftDb.getDbDataMap();
                    Map<String, Map<String, Object>> rightDbData = rightDb.getDbDataMap();

                    for (String pk : rightDbData.keySet()) {
                        if (!leftDbData.containsKey(pk)) {
                            String errorLine = name + "->" + pk;
                            pkRightMoreLeftError.add(errorLine);
                        } else {
                            Map<String, Object> leftDbDataRow = leftDbData.get(pk);
                            Map<String, Object> rightDbDataRow = rightDbData.get(pk);


                            for (String column : rightDbDataRow.keySet()) {
                                if (!leftDbDataRow.containsKey(column)) {
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
