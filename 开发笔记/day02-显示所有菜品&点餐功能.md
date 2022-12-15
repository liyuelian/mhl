# 满汉楼02

## 4.功能实现04

### 4.6显示所有菜品

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020170219429.png" alt="image-20221020170219429" style="zoom:67%;" />

#### 4.6.1思路分析

创建一个菜单表menu，在Domain层创建与菜单表对应的Javabean-Menu类，在DAO层创建MenuDAO，完成对menu表的增删改查，在Service层创建一个和menu表相关的service类，service类提供给界面层使用

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/%E6%BB%A1%E6%B1%89%E6%A5%BC%E6%A1%86%E6%9E%B6%E5%9B%BE-3%E9%98%B6%E6%AE%B5.png" style="zoom:25%;" />

#### 4.6.2代码实现

##### 1.创建menu表

```mysql
-- 创建menu表(id,name,type,price)
CREATE TABLE menu(
	id INT PRIMARY KEY AUTO_INCREMENT,#自增主键，作为菜谱编号（唯一）
	NAME VARCHAR(50) NOT NULL DEFAULT '',#菜品名称
	TYPE VARCHAR(50) NOT NULL DEFAULT '',#菜品种类
	price DOUBLE NOT NULL DEFAULT 0	#价格
)CHARSET=utf8

-- 添加测试数据
INSERT INTO menu VALUES(NULL,'八宝饭','主食',10);
INSERT INTO menu VALUES(NULL,'叉烧包','主食',20);
INSERT INTO menu VALUES(NULL,'宫保鸡丁','热菜',30);
INSERT INTO menu VALUES(NULL,'山药拨鱼','凉菜',14);
INSERT INTO menu VALUES(NULL,'银丝卷','甜食',9);
INSERT INTO menu VALUES(NULL,'水煮鱼','热菜',26);
INSERT INTO menu VALUES(NULL,'甲鱼汤','汤菜',100);
INSERT INTO menu VALUES(NULL,'鸡蛋汤','汤菜',16);
```

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020184529244.png" alt="image-20221020184529244" style="zoom:67%;" />

##### 2.创建Menu类

```java
package com.li.mhl.domain;

/**
 * @author 李
 * @version 1.0
 * 该类和menu表对应
 */
public class Menu {
    /**
     * Field   Type         Null    Key     Default  Extra
     * ------  -----------  ------  ------  -------  ----------------
     * id      int(11)      NO      PRI     (NULL)   auto_increment
     * name    varchar(50)  NO
     * type    varchar(50)  NO
     * price   double       NO              0
     */
    private Integer id;
    private String name;
    private String type;
    private Double price;

    public Menu() {
    }

    public Menu(Integer id, String name, String type, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id + "\t\t\t" + name + "\t\t" + type + "\t\t" + price;
    }

}
```

##### 3.创建MenuDAO类

```java
package com.li.mhl.dao;

import com.li.mhl.domain.Menu;

/**
 * @author 李
 * @version 1.0
 */
public class MenuDAO extends BasicDAO<Menu>{

}
```

##### 4.创建MenuService类

```java
package com.li.mhl.service;

import com.li.mhl.dao.MenuDAO;
import com.li.mhl.domain.Menu;

import java.util.List;

/**
 * @author 李
 * @version 1.0
 * 完成对menu表的各种操作（通过调用MenuDAO）
 */
public class MenuService {
    //定义MenuDAO属性
    private MenuDAO menuDAO = new MenuDAO();

    //编写方法，查询所有菜品（返回所有的菜品给界面使用）
    public List<Menu> list() {
        return menuDAO.queryMulti("select * from menu", Menu.class);
    }
}
```

5.修改MHLView

修改处1：定义属性

```java
//定义MenuService属性
private MenuService menuService = new MenuService();
```

修改处2：编写方法

```java
//显示所有菜品
public void listMenu() {
    List<Menu> list = menuService.list();
    System.out.println("\n菜品编号\t\t菜品名\t\t类别\t\t价格");
    for (Menu menu: list) {
        System.out.println(menu);
    }
    System.out.println("============显示完毕============");
}
```

修改处3：在内层循环的二级菜单中调用该方法

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020181117312.png" alt="image-20221020181117312" style="zoom:67%;" />

#### 4.6.3测试

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020181211609.png" alt="image-20221020181211609" style="zoom:64%;" />

测试通过



### 4.7点餐功能

#### 4.7.1功能说明

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020181452774.png" alt="image-20221020181452774" style="zoom:67%;" />

#### 4.7.2思路分析

功能如上图所示，分析如下：

1. 在输入点餐桌号和菜品编号时都需要对其进行合理性校验，如果不合理，给出提示信息
2. 当点餐成功之后，餐桌的状态state应该发生变化，由已经预定变为进餐中
3. 一旦点餐，就应该生成账单

根据上述的分析，整体的框架应该如下：

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/%E6%BB%A1%E6%B1%89%E6%A5%BC%E6%A1%86%E6%9E%B6%E5%9B%BE-4%E9%98%B6%E6%AE%B5.png" style="zoom:25%;" />

#### 4.7.3代码实现

##### 1.创建bill表

```mysql
-- 创建bill账单表(id,billId,menuId,nums,billDate,money,state,diningTableId)
#账单流水, 考虑可以分开结账, 并考虑将来分别统计各个不同菜品的销售情况
CREATE TABLE bill (
	id INT PRIMARY KEY AUTO_INCREMENT, #自增主键
	billId VARCHAR(50) NOT NULL DEFAULT '',#账单号可以按照自己规则生成 UUID
	menuId INT NOT NULL DEFAULT 0,#菜品的编号, 也可以使用外键
	nums SMALLINT NOT NULL DEFAULT 0,#份数
	money DOUBLE NOT NULL DEFAULT 0, #金额
	diningTableId INT NOT NULL DEFAULT 0, #餐桌
	billDate DATETIME NOT NULL ,#订单日期
	state VARCHAR(50) NOT NULL DEFAULT '' # 状态 '未结账' , '已经结账-现金/支付宝', '挂单'
)CHARSET=utf8;
```

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020184552894.png" alt="image-20221020184552894" style="zoom:80%;" />

##### 2.创建Bill类

```java
package com.li.mhl.domain;

import java.util.Date;

/**
 * @author 李
 * @version 1.0
 * 该类和 bill表对应
 */
public class Bill {
    /**
     * Field          Type         Null    Key     Default  Extra
     * -------------  -----------  ------  ------  -------  ----------------
     * id             int(11)      NO      PRI     (NULL)   auto_increment
     * billId         varchar(50)  NO
     * menuId         int(11)      NO              0
     * nums           smallint(6)  NO              0
     * money          double       NO              0
     * diningTableId  int(11)      NO              0
     * billDate       datetime     NO              (NULL)
     * state          varchar(50)  NO
     */
    private Integer id;
    private String billId;
    private Integer menuId;
    private Integer nums;
    private Double money;
    private Integer diningTableId;
    private Date billDate;
    private String state;

    public Bill() {
    }

    public Bill(Integer id, String billId, Integer menuId, Integer nums, Double money, Integer diningTableId, Date billDate, String state) {
        this.id = id;
        this.billId = billId;
        this.menuId = menuId;
        this.nums = nums;
        this.money = money;
        this.diningTableId = diningTableId;
        this.billDate = billDate;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getNums() {
        return nums;
    }

    public void setNums(Integer nums) {
        this.nums = nums;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getDiningTableId() {
        return diningTableId;
    }

    public void setDiningTableId(Integer diningTableId) {
        this.diningTableId = diningTableId;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
```

##### 3.创建BillDAO类

```java
package com.li.mhl.dao;

import com.li.mhl.domain.Bill;

/**
 * @author 李
 * @version 1.0
 */
public class BillDAO extends BasicDAO<Bill>{
}
```

##### 4.创建BillService类

```java
package com.li.mhl.service;

import com.li.mhl.dao.BillDAO;

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
}
```

##### 5.修改MenuService类

在该类中增加方法getMenuById()

```java
//根据菜品id，返回Menu对象
public Menu getMenuById(int id) {
    return menuDAO.querySingle("select * from menu where id=?", Menu.class, id);
}
```

##### 6.修改DiningTableService类

在该类中增加方法updateDiningTableState()

```java
//需要提供一个更新 餐桌状态的方法
public boolean updateDiningTableState(int id, String state) {
    int update =
            diningTableDAO.update("update diningTable set state=? where id=?", state, id);
    return update > 0;
}
```

##### 7.修改MHLView

修改处1：在该类中定义BillService属性

```java
//定义BillService属性
private BillService billService = new BillService();
```

修改处2：在该类中增加方法

```java
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
```

修改处3：在二级菜单中调用该方法

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020202052360.png" alt="image-20221020202052360" style="zoom:67%;" />

#### 4.7.4测试

- 正常数值输入

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020202159695.png" alt="image-20221020202159695" style="zoom:67%;" />

- 非法数值判断

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020202307252.png" alt="image-20221020202307252" style="zoom:67%;" />

***

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020202332174.png" alt="image-20221020202332174" style="zoom:67%;" />

***

<img src="https://liyuelian.oss-cn-shenzhen.aliyuncs.com/imgs/image-20221020202515296.png" alt="image-20221020202515296" style="zoom:67%;" />

测试通过

