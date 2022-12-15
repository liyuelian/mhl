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

    //根据菜品id，返回Menu对象
    public Menu getMenuById(int id) {
        return menuDAO.querySingle("select * from menu where id=?", Menu.class, id);
    }


}
