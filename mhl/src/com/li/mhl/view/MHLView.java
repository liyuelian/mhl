package com.li.mhl.view;

import com.li.mhl.domain.*;
import com.li.mhl.service.BillService;
import com.li.mhl.service.DiningTableService;
import com.li.mhl.service.EmployeeService;
import com.li.mhl.service.MenuService;
import com.li.mhl.utils.Utility;

import java.util.List;
import java.util.Objects;

/**
 * @author 李
 * @version 1.0
 * 这是主界面
 */
public class MHLView {

    //控制是否退出菜单
    private boolean loop = true;
    private String key = "";//接收用户的输入
    //定义EmployeeService属性
    private EmployeeService employeeService = new EmployeeService();
    //定义DiningTableService属性
    private DiningTableService diningTableService = new DiningTableService();
    //定义MenuService属性
    private MenuService menuService = new MenuService();
    //定义BillService属性
    private BillService billService = new BillService();


    public static void main(String[] args) {
        new MHLView().mainMenu();
    }

    //完成结账
    public void payBill() {
        System.out.println("============结账服务============");
        System.out.print("请输入要结账的餐桌编号(-1退出): ");
        int diningTableId = Utility.readInt();
        if (diningTableId == -1) {
            System.out.println("============取消结账============");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if (diningTable == null) {
            System.out.println("==========餐桌号不存在===========");
            return;
        }
        //验证餐桌是否有需要结账的账单
        if (!billService.hasPayBillByDiningTable(diningTableId)) {
            System.out.println("==========该餐桌不存在未结账账单===========");
            return;
        }
        System.out.print("请输入结账的方式(现金/支付宝/微信)回车表示退出: ");
        String payMode = Utility.readString(3, "");//如果回车，返回的就是空串""
        if ("".equals(payMode)) {
            System.out.println("============取消结账============");
            return;
        }
        if (!(payMode.equals("现金") || payMode.equals("支付宝") || payMode.equals("微信"))) {
            System.out.println("============输入有误============");
            return;
        }

        char key = Utility.readConfirmSelection();
        if (key == 'Y') {
            //计算总金额
            System.out.println("==========" + diningTableId + "号餐桌消费总金额为: " + billService.allOfMoney(diningTableId) + " 元===========");
            //调用结账方法
            if (billService.payBill(diningTableId, payMode)) {
                System.out.println("==========结账完成===========");
            } else {
                System.out.println("==========结账失败===========");
            }
        } else {
            System.out.println("============取消结账============");
        }
    }

    //显示账单信息
    public void listBill() {
        List<Bill> bills = billService.list();
        System.out.println("\n编号\t\t菜品号\t\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
        for (Bill bill : bills) {
            System.out.println(bill);
        }
        System.out.println("============显示完毕============");
    }

    //显示账单信息-改进
    public void listBill2() {
        List<MultiTableBean> multiTableBeans = billService.list2();
        System.out.println("\n编号\t\t菜品号\t\t菜品名\t\t单价\t\t\t菜品量\t\t金额\t\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
        for (MultiTableBean multiTableBean : multiTableBeans) {
            System.out.println(multiTableBean);
        }
        System.out.println("============显示完毕============");
    }


    //完成点餐操作
    public void orderMenu() {
        System.out.println("============点餐服务============");
        System.out.print("请输入点餐的桌号(-1退出): ");
        int orderDiningTableId = Utility.readInt();
        if (orderDiningTableId == -1) {
            System.out.println("============取消点餐============");
            return;
        }
        //验证餐桌号是否存在
        DiningTable diningTableById = diningTableService.getDiningTableById(orderDiningTableId);
        if (diningTableById == null) {
            System.out.println("============餐桌号不存在============");
            return;
        }

        System.out.print("请输入菜品编号(-1退出): ");
        int orderMenuId = Utility.readInt();
        if (orderMenuId == -1) {
            System.out.println("============取消点餐============");
            return;
        }
        //验证菜品编号是否存在
        Menu menuById = menuService.getMenuById(orderMenuId);
        if (menuById == null) {
            System.out.println("============菜品不存在============");
            return;
        }

        System.out.print("请输入菜品数量（0~99）(-1退出): ");
        int orderNums = Utility.readInt();
        if (orderNums == -1) {
            System.out.println("============取消点餐============");
            return;
        }
        if (orderNums <= 0) {
            System.out.println("============菜品数量不能小于等于0============");
            return;
        }

        //点餐
        if (billService.orderMenu(orderMenuId, orderNums, orderDiningTableId)) {
            System.out.println("============点餐成功============");
        } else {
            System.out.println("============点餐失败============");
        }

    }

    //显示所有菜品
    public void listMenu() {
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜品名\t\t类别\t\t价格");
        for (Menu menu : list) {
            System.out.println(menu);
        }
        System.out.println("============显示完毕============");
    }

    //显示餐桌状态
    public void listDiningTable() {
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌编号\t\t餐桌状态");
        for (DiningTable diningTable : list) {
            System.out.println(diningTable);
        }
        System.out.println("============显示完毕============");
    }

    //完成订座功能
    public void orderDiningTable() {
        System.out.println("============预定餐桌============");
        System.out.println("请选择要预定的餐桌编号(-1退出): ");
        int orderId = Utility.readInt();
        if (orderId == -1) {
            System.out.println("============取消预定餐桌============");
            return;
        }
        //readConfirmSelection方法得到 Y 或者 N
        char key = Utility.readConfirmSelection();
        if (key == 'Y') {//要预定
            //根据orderId返回对应的DiningTable,如果为null，说明该编号的餐桌不存在
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if (diningTable == null) {
                System.out.println("============预定餐桌的餐桌不存在============");
                return;
            }

            //判断餐桌的状态是否为 “空”
            if (!"空".equals(diningTable.getState())) {//如果餐桌状态不为空
                //不能预定
                System.out.println("============该餐桌已经预定或者处于就餐中============");
                return;
            }

            //如果前两个判断都不满足，说明餐桌存在且可以预定
            //接收预定信息
            System.out.print("预订人名字: ");
            String orderName = Utility.readString(50);
            System.out.print("预订人电话: ");
            String orderTel = Utility.readString(50);
            //更新餐桌状态
            if (diningTableService.orderDiningTable(orderId, orderName, orderTel)) {
                System.out.println("============预定餐桌成功============");
            } else {
                System.out.println("============预定餐桌失败============");
            }
        } else {
            System.out.println("============取消预定餐桌============");
            return;
        }
    }

    //显示主菜单
    public void mainMenu() {
        while (loop) {
            System.out.println("==================满汉楼==================");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.print("请输入你的选择：");
            key = Utility.readString(1);//接收用户选择
            switch (key) {
                case "1":
                    System.out.print("请输入员工号: ");
                    String empId = Utility.readString(50);
                    System.out.print("请输入密  码: ");
                    String pwd = Utility.readString(50);
                    //到数据库去判断
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId, pwd);
                    if (employee != null) {//说明存在该用户,登录成功
                        System.out.println("==================登录成功[" + employee.getName() + "]=================\n");
                        //显示二级菜单，这里也和一级菜单一样是循环操作，也放在循环语句中
                        while (loop) {
                            System.out.println("\n==================满汉楼(二级菜单)==================");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 参看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.print("请输入你的选择：");
                            key = Utility.readString(1);//接收用户的选择
                            switch (key) {
                                case "1":
                                    listDiningTable();//调用方法，显示餐桌状态
                                    break;
                                case "2":
                                    orderDiningTable();//预定餐桌
                                    break;
                                case "3":
                                    listMenu();//显示所有菜品
                                    break;
                                case "4":
                                    orderMenu();//点餐服务
                                    break;
                                case "5":
                                    listBill2();//参看账单
                                    break;
                                case "6":
                                    payBill();//结账
                                    break;
                                case "9":
                                    //因为loop同时控制两层循环，因此当loop设为false时，
                                    // 里层循环退出之后，外层循环也会随之退出
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("输入有误，请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("==================登录失败==================");
                    }
                    break;
                case "2":
                    loop = false;//退出一级菜单的循环
                    break;
                default:
                    System.out.println("输入有误，重新输入..");
            }
        }
        System.out.println("你退出了满汉楼系统~");
    }
}
