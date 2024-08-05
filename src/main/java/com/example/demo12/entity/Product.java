package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value ="product")
@ToString
public class Product implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String proname;

    public String proassname;

    public String proinsname;

    public String protag;

    public String proavatar;

    public String prodetail;

    public Integer proprice;

    public String prounit;

    public Integer prosort;




}
