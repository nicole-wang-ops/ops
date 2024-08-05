package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@TableName(value ="rolemenu")
public class RoleMenu{
    public Integer roleid;

    public Integer menuid;



}
