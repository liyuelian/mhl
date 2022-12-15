package com.li.mhl.service;

import com.li.mhl.dao.EmployeeDAO;
import com.li.mhl.domain.Employee;

/**
 * @author 李
 * @version 1.0
 * 该类完成对employee表的各种操作（通过调用EmployeeDAO对象完成）
 */
public class EmployeeService {

    //定义一个EmployeeDAO属性
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    //登录校验方法
    //根据empId和pwd返回一个Employee对象,如果查询不到，就返回null
    public Employee getEmployeeByIdAndPwd(String empId, String pwd) {
        //注意密码使用md5加密后再查询比较
        Employee employee =
                employeeDAO.querySingle("select * from employee where empId=? and pwd=md5(?) ", Employee.class, empId, pwd);
        return employee;
    }

}
