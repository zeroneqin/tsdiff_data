package com.qinjun.autotest.tsdiff.po.db.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qinjun.autotest.tsdiff.exception.TSDiffException;
import com.qinjun.autotest.tsdiff.po.db.info.DBInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DB {
    private static final Logger logger = LoggerFactory.getLogger(DB.class);
    public static final String myBatisConfigFile = "mybatis-config.xml";

    private String name;
    private String driver;
    private String url;
    private String username;
    private String password;
    private String sql;
    private String pk;

    Map<String, Map<String, Object>> dbDataMap;

    public DB(DBInfo dbInfo) {
        driver = dbInfo.getDriver();
        url = dbInfo.getUrl();
        username = dbInfo.getUserName();
        password = dbInfo.getPassword();
        sql = dbInfo.getSql();


        try {
            Properties myBatisProps = prepareMyBatisProps();
            InputStream inputStream = DB.class.getResourceAsStream("/" + myBatisConfigFile);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, myBatisProps);
            SqlSession sqlSesion = sqlSessionFactory.openSession();
            List<Map<String, Object>> queryResultList = sqlSesion.selectList("Query");
            Map<String, Map<String, Object>> queryResultWithPk = transformQueryResult(queryResultList, dbInfo.getPk());
            dbDataMap.putAll(queryResultWithPk);
        } catch (Exception e) {
            logger.error("Get exception:" + e);
        }
    }


    Properties prepareMyBatisProps() {
        Properties properties = new Properties();
        properties.put("driver", driver);
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        properties.put("sql", sql);
        return properties;
    }


    Map<String, Map<String, Object>> transformQueryResult(List<Map<String, Object>> queryResults, String pkStr) {
        String[] pks = null;
        Map<String, Map<String, Object>> queryResultWithPk = new HashMap<String, Map<String, Object>>();
        if (StringUtils.isEmpty(pkStr) || pkStr.equalsIgnoreCase("*")) {
            Map<String, Object> queryResultRow = queryResults.get(0);
            pks = queryResultRow.keySet().toArray(new String[0]);
        } else {
            pks = pkStr.split(",");
        }

        int rowIndex = 0;
        for (Map<String, Object> queryResult : queryResults) {
            if (pks.length == 0) {
                queryResultWithPk.put(String.valueOf(rowIndex), queryResult);
                rowIndex++;
            } else {
                StringBuilder sb = new StringBuilder();
                if (pks.length == 1) {
                    sb.append(queryResult.get(pks[0]).toString());
                } else {
                    boolean firstFlag = true;
                    for (String pk : pks) {
                        Object pkPart = queryResult.get(pk);
                        if (firstFlag) {
                            if (pkPart != null) {
                                sb.append(queryResult.get(pk).toString());
                            } else {

                            }
                            firstFlag = false;
                        } else {
                            if (pkPart != null) {
                                sb.append("|");
                                sb.append(queryResult.get(pk).toString());
                            } else {
                                sb.append("|");
                            }
                        }
                    }
                }
                queryResultWithPk.put(sb.toString(), queryResult);
            }
        }
        return queryResultWithPk;
    }

    public Map<String, Map<String, Object>> getDbDataMap() {
        return dbDataMap;
    }

    public void setDbDataMap(Map<String, Map<String, Object>> dbDataMap) {
        this.dbDataMap = dbDataMap;
    }
}
