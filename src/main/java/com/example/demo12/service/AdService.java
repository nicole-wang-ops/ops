package com.example.demo12.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.common.Result;
import com.example.demo12.controller.dto.UserDTO;
import com.example.demo12.controller.dto.UserPasswordDTO;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.Menu;
import com.example.demo12.entity.User;
import com.example.demo12.exception.ServiceException;
import com.example.demo12.mapper.AdMapper;
import com.example.demo12.mapper.RoleMapper;
import com.example.demo12.mapper.RoleMenuMapper;
import com.example.demo12.utils.TokenUtils;
import io.swagger.models.auth.In;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AdService extends ServiceImpl<AdMapper,Ad> {
    public boolean saveAd(Ad ad) {

        return saveOrUpdate(ad);
    }


    private static final Log LOG = Log.get();
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private MenuService menuService;


    public UserDTO login(UserDTO userDTO) {

        Ad one = getUserInfo(userDTO);
        if(one != null){
            BeanUtils.copyProperties(one,userDTO);
            //设置Token
            String token = TokenUtils.genToken(one.getId().toString(),one.getAdpassword());
            userDTO.setToken(token);

            String adrole = one.getAdrole();//ROLE_ADMIN
            //设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(adrole);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }


    public Ad register(UserDTO userDTO) {
        Ad one = getUserInfo(userDTO);
        if(one == null){
            one = new Ad();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);  //把copy完之后的用户对象存储到数据库
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    @Resource
    private AdMapper adMapper;

    public void updateAdpassword(UserPasswordDTO userPasswordDTO) {
        int update = adMapper.updateAdpassword(userPasswordDTO);
        if(update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }

    private Ad getUserInfo(UserDTO userDTO){
        QueryWrapper<Ad> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("adname",userDTO.getAdname());
        queryWrapper.eq("adpassword",userDTO.getAdpassword());
        Ad one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    //获取当前角色的菜单列表
    private List<Menu> getRoleMenus(String roleFlag){
        Integer roleid = roleMapper.selectByRoleflag(roleFlag);
        //当前角色的所有菜单id集合
        List<Integer> menuids = roleMenuMapper.selectByRoleId(roleid);

        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenus("");
        //new 一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        //筛选当前用户角色的菜单
        for (Menu menu : menus){
            //menu.setChildren(children);
            if(menuids.contains(menu.getId())){
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            //removeIf 移除 children 里面不在menuids集合中的元素
            children.removeIf(child -> !menuids.contains(child.getId()));
            System.out.println("children :  " + children.toString());
        }
        return roleMenus;
    }

    /**
     * 积分修改
     */
    public void creditUpdate(Ad ad) {
        adMapper.updateById(ad);
    }

}

