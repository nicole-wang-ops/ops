package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value ="dict")
public class Dict implements Serializable {


    public String dictname;

    public String dictvalue;

    public String dicttype;

}
