package com.example.demo12.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.controller.dto.UserPasswordDTO;
import com.example.demo12.entity.User;
import org.apache.ibatis.annotations.Update;


public interface UserMapper extends BaseMapper<User> {
    /*@Update("update the_aged set opassword = #{newOpassword} where name = #{name} and opassword = #{opassword}")
    int updateOpassword(UserPasswordDTO userPasswordDTO);*/
}
