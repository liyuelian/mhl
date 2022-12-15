package com.li.mhl.service;

import com.li.mhl.dao.BillDAO;
import com.li.mhl.dao.MultiTableDAO;
import com.li.mhl.domain.Bill;
import com.li.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

/**
 * @author 李
 * @version 1.0
 * 处理和账单表bill相关的业务逻辑
 */
public class BillService {
    //定义BillDAO属性
    private BillDAO billDAO = new BillDAO();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义DiningTableService属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MultiTableDAO属性
    private MultiTableDAO multiTableDAO = new MultiTableDAO();

    //编写点餐的方法
    /**
     * 1.生成账单
     * 2.需要更新对应的餐桌的状态
     * 3.如成功返回true，失败返回false
     */
    public boolean orderMenu(int menuId, int nums, int diningTableId) {
        //使用UUID生成一个账单号
        String billID = UUID.randomUUID().toString();

        //将账单生成到bill表()
        //这里的金额money = 由menuId（菜品编号）查询出来的单价 * nums
        //因此，在MenuService类中编写方法getMenuById（）查询菜品单价
        int update = billDAO.update("insert into bill values(null,?,?,?,?,?,now(),'未结账')",
                billID, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);
        if (update <= 0) {
            return false;
        }

        //需要更新对应的餐桌的状态
        //在DiningTableService类中编写方法updateDiningTableState（）更新对应的餐桌的状态
        return diningTableService.updateDiningTableState(diningTableId, "就餐中");
    }

    //返回所有的账单，提供给View使用
    public List<Bill> list() {
        return billDAO.queryMulti("select * from bill", Bill.class);
    }

    //改进后的方法--返回所有的账单，提供给View使用
    public List<MultiTableBean> list2() {
        return multiTableDAO.queryMulti("SELECT " +
                        "bill.id,menuId,NAME,price,nums,money,diningTableId,state,billDate " +
                        "FROM bill,menu WHERE menuId=menu.id",
                MultiTableBean.class);
    }


    //查看某个餐桌是否有未结账的账单--老师写的
    public boolean hasPayBillByDiningTable(int diningTableId) {
        Bill bill = billDAO.querySingle("select * from bill where diningTableId=? and state='未结账' limit 0,1", Bill.class, diningTableId);
        return bill != null;//bill非空，就代表有未结账的账单
    }


    /**
     * 查看某张餐桌是否有未结账的账单  --自己写的
     *
     * @param diningTableId 结账的餐桌编号
     * @return 返回的某餐桌需要支付的总金额
     */
    public Double allOfMoney(int diningTableId) {
        //计算该餐桌需要支付的总金额
        Double countMoney = Double.valueOf("0");
        List<Bill> bills = billDAO.queryMulti("select * from bill where diningTableId=? and state='未结账'", Bill.class, diningTableId);
        for (Bill bill1 : bills) {
            countMoney += bill1.getMoney();
        }
        //返回总金额
        return countMoney;
    }

    //完成结账 [前提：1.餐桌编号存在 2.该餐桌有未结账的账单]
    public boolean payBill(int diningTableId, String payMode) {
        //这里其实应该开启事务--防止上面成功，下面失败的情况
        //如果使用事务的话，需要使用ThreadLocal来解决，框架中比如 mybatis就提供了事务支持

        //1.修改bill表的state
        int update = billDAO.update("update bill set state=? where diningTableId=? and state='未结账'", payMode, diningTableId);
        if (update <= 0) {//如果更新没有成功，则表示失败
            return false;
        }
        //2.修改diningTable的state
        if (!diningTableService.updateDiningTableToFree(diningTableId, "空")) {
            return false;
        }
        return true;
    }
}
