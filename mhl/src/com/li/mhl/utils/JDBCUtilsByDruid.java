package com.li.mhl.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 基于Druid数据库连接池的工具类
 */
public class JDBCUtilsByDruid {

    private static DataSource ds;

    //在静态代码块完成ds的初始化
    //静态代码块在加载类的时候只会执行一次，因此数据源也只会初始化一次
    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src\\druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //编写getConnection方法
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    //关闭连接（注意：在数据库连接池技术中，close不是真的关闭连接，而是将Connection对象放回连接池中）
    public static void close(ResultSet resultSet, Statement statemenat, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statemenat != null) {
                statemenat.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
