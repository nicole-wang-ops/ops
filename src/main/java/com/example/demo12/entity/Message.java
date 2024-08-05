package com.example.demo12.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value ="message")
@ToString
public class Message implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer id;

    public String content;

    public String adname;

    //@TableField("adcreattime")
    @TableField("time")
    public String time;

    @TableField("parentId")
    public Long parentId;

    @TableField("foreignId")
    public Integer foreignId;

    public BigDecimal rate;

    public Integer adid;

    public String target;

//    public Message parentMessage;

//    public void setAdavatar(String adavatar) {
//    }


//    public void setParentMessage(Message message) {
//    }
}
