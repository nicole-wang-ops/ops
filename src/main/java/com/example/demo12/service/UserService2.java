package com.example.demo12.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.dto.UserDTO;
import com.example.demo12.entity.User;
import com.example.demo12.entity.User2;
import com.example.demo12.exception.ServiceException;
import com.example.demo12.mapper.UserMapper2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService2 extends ServiceImpl<UserMapper2,User2> {
    public boolean saveUser2(User2 user2) {
        return saveOrUpdate(user2);//根据id在数据库中查数据
    }//可能会出错


    public UserDTO getUserInfo(UserDTO userDTO){
        QueryWrapper<User2> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("nurname",userDTO.getAdname());
        queryWrapper.eq("nurpassword",userDTO.getAdpassword());
        User2 one = getOne(queryWrapper);//从数据库查询用户信息
        if(Objects.nonNull(one)) {
            userDTO.setId(one.getId());
            userDTO.setAdname(one.getNurname());
            userDTO.setNickname(one.getNurnickname());
            userDTO.setAdpassword(one.getNurpassword());
            userDTO.setAdemail(null);
            userDTO.setAdphone(one.getNurphone());
            userDTO.setAdavatar(one.getNuravatar());
            userDTO.setAdrole("ROLE_NURSE");
        }
        return userDTO;
    }

}
