package com.example.demo12.entity;

import java.io.Serializable;
import java.util.Date;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@TableName(value ="administrators")
@ToString
public class Ad implements Serializable {

    @TableId(type = IdType.AUTO)    //指定名字为id的规范写法。或者写成@TableId(value="id")
    public Integer id;

    public String adname;

    public String adnickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String adpassword;

    public String ademail;

    public String adphone;

    public Date adcreattime;

    public String adavatar;

    public String adrole;

    public Integer credit;

    public String content;

//    public Integer parentId;

    //public Integer foreignId;

    public Integer age;

    public String ability;

    public String oaddress;

    public String instname;

    public String nurcredential;

    public Integer nurworkingyears;

    public String instcertify;

    public Integer insthistorytime;

    public String instculture;
}
