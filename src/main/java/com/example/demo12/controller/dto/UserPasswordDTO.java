package com.example.demo12.controller.dto;

import lombok.Data;

@Data
public class UserPasswordDTO {

    //the_aged
    private String name;
    private String phone;
    private String opassword;
    private String newOpassword;


    //ad
    private String adname;
    private String adphone;
    private String adpassword;
    private String newAdpassword;
}
