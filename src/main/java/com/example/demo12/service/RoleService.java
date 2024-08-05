package com.example.demo12.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.entity.Role;
import com.example.demo12.entity.RoleMenu;
import com.example.demo12.mapper.RoleMapper;
import com.example.demo12.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleService extends ServiceImpl<RoleMapper,Role> {
    public boolean saveRole(Role role) {
        return saveOrUpdate(role);//根据id在数据库中查数据
    }//可能会出错


    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Transactional
    public void setRoleMenu(Integer roleid, List<Integer> menuids) {
        //先删除当前角色id所有的绑定关系
        /*QueryWrapper<RoleMenu>queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("roleid",roleId);
        roleMenuMapper.delete(queryWrapper);*/

        //先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleid);

        //再把前端传过来的菜单id数组绑定到当前这个角色id上去
        for (Integer menuid : menuids) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleid(roleid);
            roleMenu.setMenuid(menuid);
            roleMenuMapper.insert(roleMenu);
        }
    }

    public List<Integer> getRoleMenu(Integer roleid) {
        return roleMenuMapper.selectByRoleId(roleid);
    }
}
