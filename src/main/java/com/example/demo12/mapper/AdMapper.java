package com.example.demo12.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.dto.UserPasswordDTO;
import com.example.demo12.entity.Ad;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AdMapper extends BaseMapper<Ad> {
    @Update("update administrators set adpassword = #{newAdpassword} where adname = #{adname} and adpassword = #{adpassword}")
    int updateAdpassword(UserPasswordDTO userPasswordDTO);

}
