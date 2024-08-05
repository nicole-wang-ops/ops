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
@TableName(value ="nursing_assistant")
@ToString
public class User2 implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String nurname;

    public String instname;

    public String nurnickname;

    public String nurphone;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String nurpassword;

    public String nuremail;

    public String nurcredential;

    public int nurworkingyears;

    public String nuraddress;

    public String nuravatar;

    public Date nurcreattime;

    public String nurrole;
}
