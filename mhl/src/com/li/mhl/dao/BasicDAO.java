package com.li.mhl.dao;

import com.li.mhl.utils.JDBCUtilsByDruid;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 开发BasicDAO，是其他DAO的父类
 */

public class BasicDAO<T> {//泛型指定具体的类型

    private QueryRunner qr = new QueryRunner();

    //开发通用的dml方法，针对任意的表

    /**
     * @param sql        传入的SQL语句,可以有占位符?
     * @param parameters 传入占位符?的具体的值，可以是多个
     * @return 返回的值是受影响的行数
     */
    public int update(String sql, Object... parameters) { //可变参数 Object… parameters

        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            int update = qr.update(connection, sql, parameters);
            return update;

        } catch (SQLException e) {
            throw new RuntimeException(e);//将一个编译异常转变为运行异常
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //返回多个对象（即查询的结果是多行），针对任意的表（多行多列）

    /**
     * @param sql        传入的SQL语句,可以有占位符?
     * @param clazz      传入一个类的Class对象，比如 Actor.class[底层需要通过反射来创建Javabean对象]
     * @param parameters 传入占位符?的具体的值，可以是多个
     * @return 根据传入的class对象 Xxx.class 返回对应的ArrayList集合
     */
    public List<T> queryMulti(String sql, Class<T> clazz, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection, sql, new BeanListHandler<T>(clazz), parameters);

        } catch (SQLException e) {
            throw new RuntimeException(e);//将一个编译异常转变为运行异常
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //查询单行结果 的通用方法（单行多列）
    public T querySingle(String sql, Class<T> clazz, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection, sql, new BeanHandler<T>(clazz), parameters);

        } catch (SQLException e) {
            throw new RuntimeException(e);//将一个编译异常转变为运行异常
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }

    //查询单行单列的方法，即返回单值的方法
    public Object queryScalar(String sql, Object... parameters) {
        Connection connection = null;
        try {
            connection = JDBCUtilsByDruid.getConnection();
            return qr.query(connection, sql, new ScalarHandler(), parameters);

        } catch (SQLException e) {
            throw new RuntimeException(e);//将一个编译异常转变为运行异常
        } finally {
            JDBCUtilsByDruid.close(null, null, connection);
        }
    }
}
