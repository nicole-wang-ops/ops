package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value ="nurproorder")
@ToString
public class Nurproorder implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public Integer pid;

    public Integer buyId;

    public Date createTime;



}
