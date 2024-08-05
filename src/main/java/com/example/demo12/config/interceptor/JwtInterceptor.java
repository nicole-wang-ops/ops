package com.example.demo12.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.entity.Ad;
import com.example.demo12.exception.ServiceException;
import com.example.demo12.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



//拦截器
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private AdService adService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        //执行认证
        if(StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"无token，请重新登录");
        }
        String adId;
        try {
            adId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException j){
            throw new ServiceException(Constants.CODE_401, "token验证失败");
        }
        //根据token中的adid查询数据库
        Ad ad = adService.getById(adId);
        if(ad == null){
            throw new ServiceException(Constants.CODE_401, "用户不存在，请重新登录");
        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(ad.getAdpassword())).build();
        try {
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }
}
