package com.example.demo12.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo12.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role>{
    @Select("select id from role where roleflag = #{roleflag}")
    Integer selectByRoleflag(@Param("roleflag") String roleflag);
}
