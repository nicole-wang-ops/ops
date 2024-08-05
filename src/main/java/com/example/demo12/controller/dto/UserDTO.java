package com.example.demo12.controller.dto;

import com.example.demo12.entity.Menu;
import lombok.Data;

import java.util.List;

//接收前端登录请求的参数
//假设你数据库中定义了User类，包含用户名、密码、邮箱、手机号等等；
//当用户登录时一般只需要输入用户名和密码，那么传入服务端的用户名和密码就可以在controller层封装到UserDto实体类中
@Data
public class UserDTO {
    public Integer id;

    //the_aged
    public String name;
    public String opassword;
    public String nickname;
    public String avatar;

    //nur
    public String nurname;
    public String nurpassword;
    public String nurnickname;
    public String nuravatar;

    //inst
    public String instname;
    public String instpassword;
    public String instavatar;

    //ad
    private String adname;
    private String adpassword;
    private String adnickname;
    private String adavatar;
    private String ademail;
    private String adphone;
    private String token;
    private String adrole;
    private List<Menu> menus;



//    public String adname;
//    public String adnickname;
//    public String adpassword;
//    public String ademail;
//    public String adphone;
//    public String adavatar;
//    public String adrole;


    /**
     * 1:超管
     * 2:老年用戶
     * 3:服务商用戶
     * 4:机构用户
     */
    private Integer userType;

}
