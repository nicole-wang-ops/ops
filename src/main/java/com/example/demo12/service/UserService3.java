package com.example.demo12.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.dto.UserDTO;
import com.example.demo12.entity.User2;
import com.example.demo12.entity.User3;
import com.example.demo12.exception.ServiceException;
import com.example.demo12.mapper.UserMapper3;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService3 extends ServiceImpl<UserMapper3,User3> {
    public boolean saveUser3(User3 user3) {
        return saveOrUpdate(user3);//根据id在数据库中查数据
    }//可能会出错


    public UserDTO getUserInfo(UserDTO userDTO) {
        QueryWrapper<User3> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("instname", userDTO.getAdname());
        queryWrapper.eq("instpassword", userDTO.getAdpassword());
        User3 one = getOne(queryWrapper);//从数据库查询用户信息
        if (Objects.nonNull(one)) {
            userDTO.setId(one.getId());
            userDTO.setAdname(one.getInstname());
            userDTO.setNickname(null);
            userDTO.setAdpassword(one.getInstpassword());
            userDTO.setAdemail(null);
            userDTO.setAdphone(one.getInstphone());
            userDTO.setAdavatar(one.getInstavatar());
            userDTO.setAdrole("ROLE_INSTITUTION");
        }
        return userDTO;
    }
}
