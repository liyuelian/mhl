package com.li.mhl.domain;

/**
 * @author 李
 * @version 1.0
 * 这是一个Javabean ，和表employee对应
 */
public class Employee {
    /**
     * +-------+-------------+------+-----+---------+----------------+
     * | Field | Type        | Null | Key | Default | Extra          |
     * +-------+-------------+------+-----+---------+----------------+
     * | id    | int(11)     | NO   | PRI | NULL    | auto_increment |
     * | empId | varchar(50) | NO   |     |         |                |
     * | pwd   | char(32)    | NO   |     |         |                |
     * | name  | varchar(50) | NO   |     |         |                |
     * | job   | varchar(50) | NO   |     |         |                |
     * +-------+-------------+------+-----+---------+----------------+
     */
    private Integer id;
    private String empId;
    private String pwd;
    private String name;
    private String job;

    public Employee() {//无参构造器，底层的Apache-DBUtils反射需要
    }

    public Employee(Integer id, String empId, String pwd, String name, String job) {
        this.id = id;
        this.empId = empId;
        this.pwd = pwd;
        this.name = name;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", empId='" + empId + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
