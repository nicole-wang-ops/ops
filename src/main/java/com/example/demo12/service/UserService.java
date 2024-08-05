package com.example.demo12.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.dto.UserDTO;
import com.example.demo12.controller.dto.UserPasswordDTO;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.User;
import com.example.demo12.exception.ServiceException;
import com.example.demo12.mapper.AdMapper;
import com.example.demo12.mapper.UserMapper;
import com.example.demo12.utils.TokenUtils;
import lombok.extern.log4j.Log4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class UserService extends ServiceImpl<UserMapper,User> {


    //redis存取user中的数据
    @Resource
    public RedisTemplate redisTemplate;

    public boolean saveUser(User user) {
        /*if(useUserServiceUserServicer.getId() == null){
            return save(user);//mybitis-plus提供的方法，表示插入数据
        }else{
            return updateById(user);
        }*/


        //↓Redis
        boolean b = saveOrUpdate(user);
        if(b){
            System.out.println("user: "+ user.toString());

            redisTemplate.opsForValue().set("user:info:"+user.getId(), JSONObject.toJSONString(user) );
        }   //存数据

        Object o = redisTemplate.opsForValue().get("user:info:94");      //取数据
        System.out.println("0: "+ o.toString());

        return saveOrUpdate(user);//根据id在数据库中查数据
    }

    public UserDTO getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("name",userDTO.getAdname());
        queryWrapper.eq("opassword",userDTO.getAdpassword());
        User one = getOne(queryWrapper);//从数据库查询用户信息
        if(Objects.nonNull(one)) {
            userDTO.setId(one.getId());
            userDTO.setAdname(one.getName());
            userDTO.setNickname(one.nickname);
            userDTO.setAdpassword(one.getOpassword());
            userDTO.setAdemail(null);
            userDTO.setAdphone(one.getPhone());
            userDTO.setAdavatar(one.getAvatar());
            userDTO.setAdrole("ROLE_AGED");
        }
        return userDTO;
    }

    //↑Redis


    /*private static final Log LOG = Log.get();

    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one != null){
            BeanUtils.copyProperties(one,userDTO);
            //BeanUtil.copyProperties(one,userDTO,true);

            //设置Token
            String token = TokenUtils.genToken(one.getId().toString(),one.getOpassword());
            userDTO.setToken(token);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }

    }

    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one == null){
            one = new User();
            BeanUtil.copyProperties(userDTO,one,true);
            save(one);  //把copy完之后的用户对象存储到数据库
        }else{
            throw new ServiceException(Constants.CODE_600,"用户已存在");
        }
        return one;
    }

    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper= new QueryWrapper<>();
        queryWrapper.eq("name",userDTO.getName());
        queryWrapper.eq("opassword",userDTO.getOpassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
        }catch (Exception e){
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    @Resource
    private UserMapper userMapper;

    public void updateOpassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updateOpassword(userPasswordDTO);
        if(update < 1) {
            throw new ServiceException(Constants.CODE_600, "密码错误");
        }
    }*/

}
