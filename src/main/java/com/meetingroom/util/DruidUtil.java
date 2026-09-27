package com.meetingroom.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidUtil {

    private static final DataSource dataSource;

    static {
        try {
            Properties properties = new Properties();
            InputStream is = DruidUtil.class.getClassLoader()
                    .getResourceAsStream("druid.properties");
            properties.load(is);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("Druid连接池初始化失败", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
