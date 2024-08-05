package com.example.demo12.entity;

import java.io.Serializable;


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
@TableName(value ="institution")
@ToString
public class User3 implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String instname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String instpassword;

    public String instcertify;

    public String instemail;

    public String instphone;

    public int insthistorytime;

    public String instaddress;

    public String instculture;

    public String instavatar;

    public String instcreattime;

    public String instrole;
}
