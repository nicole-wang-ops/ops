package com.example.demo12.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.Menu;
import com.example.demo12.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService extends ServiceImpl<MenuMapper,Menu> {
    public boolean saveRole(Menu menu) {
        return saveOrUpdate(menu);//根据id在数据库中查数据
    }//可能会出错

    public List<Menu> findMenus(String menuname) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(menuname)){//进行一个判断，如果不为空再进行查询
            queryWrapper.like("menuname",menuname);
        }
        //查找所有数据
        List<Menu> list = list(queryWrapper);
        //找出menupid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getMenupid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            //筛选所有数据中menupid = 父级id的数据就是二级菜单
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getMenupid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
