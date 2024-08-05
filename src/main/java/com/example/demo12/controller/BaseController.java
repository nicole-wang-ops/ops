package com.example.demo12.controller;

import com.auth0.jwt.JWT;
import com.example.demo12.entity.Ad;
import com.example.demo12.mapper.AdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {
    @Autowired
    private AdMapper adMapper;

    @Autowired
    private HttpServletRequest request;

    //根据token获取用户信息 return ad
    public Ad getAd(){
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        Integer adId = Integer.valueOf(aud);
        System.out.println("001001001");
        return adMapper.selectById(adId);
    }

}
