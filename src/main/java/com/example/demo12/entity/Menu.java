package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@TableName(value ="menu")
@ToString
public class Menu implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String menuname;

    public String menupath;

    public String menuicon;

    public String menudescription;

    @TableField(exist = false)  //表示在前台表格里面有,数据库中没有
    private List<Menu> children;

    private Integer menupid;

}
