package com.example.demo12.entity;

import java.io.Serializable;
import java.util.Date;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.ToString;

@Data
@TableName(value ="the_aged")
@ToString
public class User implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String name;

    public String nickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String opassword;

    public int age;

    public String ability;

    public String phone;

    public String oaddress;

    public Date creattime;



    public String avatar;

    public String agerole;

    public Integer credit;


}
