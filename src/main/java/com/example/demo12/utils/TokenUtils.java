package com.example.demo12.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo12.entity.Ad;
import com.example.demo12.service.AdService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Log4j
@Component
public class TokenUtils {

    private static AdService staticAdService;
    @Resource
    public AdService adService;

    @PostConstruct
    public void setAdService(){
        staticAdService = adService;
    }

    /*生成Token*/
    public static String genToken(String adId,String sign){
        return JWT.create().withAudience(adId)   //将user id 保存到token里面，作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(),2))   //2小时候token过期
                .sign(Algorithm.HMAC256(sign));  //以password作为token的密钥
    }

    //获取当前登录的用户信息
    public static Ad getCurrentAd() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String adId = JWT.decode(token).getAudience().get(0);
                return staticAdService.getById(Integer.valueOf(adId));
            }
        }catch (Exception e) {
            log.error(e);
            return null;
        }
        return null;
    }
}
