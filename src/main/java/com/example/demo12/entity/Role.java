package com.example.demo12.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@TableName(value ="role")
@ToString
public class Role implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String rolename;

    public String roledescription;

    public String roleflag;
}
