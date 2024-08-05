package com.example.demo12.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
    @Delete("delete from rolemenu where roleid = #{roleid}")
    int deleteByRoleId(@Param("roleid") Integer roleid);

    @Select("select menuid from rolemenu where roleid = #{roleid}")
    List<Integer> selectByRoleId(@Param("roleid") Integer roleid);
}
