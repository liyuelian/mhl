package com.li.mhl.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        //测试工具类Utility
        System.out.println("请输入一个整数：");
        int i = Utility.readInt();//ok
        System.out.println(i);

        //测试JDBCUtilsByDruid
        Connection connection = JDBCUtilsByDruid.getConnection();
        System.out.println(connection);//ok


    }
}
