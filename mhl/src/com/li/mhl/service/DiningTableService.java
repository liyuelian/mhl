package com.li.mhl.service;

import com.li.mhl.dao.DiningTableDAO;
import com.li.mhl.domain.DiningTable;

import java.util.List;

/**
 * @author 李
 * @version 1.0
 * 该类完成对 diningTable表的各种操作（通过调用DiningTableDAO对象完成）
 */
public class DiningTableService {//业务层
    //定义一个DiningTableDAO对象
    private DiningTableDAO diningTableDAO = new DiningTableDAO();

    //返回所有餐桌的信息
    public List<DiningTable> list() {
        List<DiningTable> diningTables =
                diningTableDAO.queryMulti("select id,state from diningTable", DiningTable.class);
        return diningTables;
    }

    //根据id，查询对应的餐桌DiningTable对象
    //如果返回null，则表示对应id编号的餐桌不存在
    public DiningTable getDiningTableById(int id) {
        //把写完的sql语句放在查询分析其去测试一下
        DiningTable diningTable =
                diningTableDAO.querySingle("select * from diningTable where id=?", DiningTable.class, id);
        return diningTable;
    }

    //如果餐桌可以预定，调用方法，对其状态进行更新（包括预定人的名字和电话）
    public boolean orderDiningTable(int id, String orderName, String orderTel) {
        int update = diningTableDAO.update("update diningTable set state='已经预定',orderName=?,orderTel=? where id=?", orderName, orderTel, id);
        return update > 0;
    }

    //需要提供一个更新 餐桌状态的方法
    public boolean updateDiningTableState(int id, String state) {
        int update =
                diningTableDAO.update("update diningTable set state=? where id=?", state, id);
        return update > 0;
    }

    //提供方法，将指定餐桌修改为空闲状态
    public boolean updateDiningTableToFree(int id, String state) {
        int update = diningTableDAO.update("update diningTable set state=?,orderName='',orderTel='' where id=?", state, id);
        return update > 0;
    }

}
