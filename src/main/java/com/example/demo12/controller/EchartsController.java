/*
package com.example.demo12.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.User;
import com.example.demo12.entity.User2;
import com.example.demo12.mapper.FileMapper;
import com.example.demo12.service.UserService;
import com.example.demo12.service.UserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserService userservice;
    @Autowired
    private UserService2 userservice2;


    @Resource
    private FileMapper fileMapper;
    @GetMapping("/example")
    public Result get(){
        Map<String ,Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }


    @GetMapping("/members")
    public Result members(){
        List<User> list = userservice.list();
        int q1 = 0;//第一季度
        int q2 = 0;//第二季度
        int q3 = 0;//第三季度
        int q4 = 0;//第四季度
        for (User user : list){
            Date creattime = user.getCreattime();
            Quarter quarter = DateUtil.quarterEnum(creattime);
            switch (quarter){
                case Q1:
                    q1++;
                    break;
                case Q2:
                    q2++;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
                default:
                    break;
            }
        }
        List userList = CollUtil.newArrayList(q1,q2,q3,q4);


        List<User2> list2 = userservice2.list();
        int q21 = 0;//第一季度
        int q22 = 0;//第二季度
        int q23 = 0;//第三季度
        int q24 = 0;//第四季度
        for (User2 user2 : list2){
            Date nurcreattime = user2.getNurcreattime();
            Quarter quarter = DateUtil.quarterEnum(nurcreattime);
            switch (quarter){
                case Q1:
                    q21++;
                    break;
                case Q2:
                    q22++;
                    break;
                case Q3:
                    q23++;
                    break;
                case Q4:
                    q24++;
                    break;
                default:
                    break;
            }
        }
        List userList2 = CollUtil.newArrayList(q21,q22,q23,q24);
        //List userList2 = CollUtil.newArrayList(21,2,13,4);
        Map valueMap = new HashMap();
        valueMap.put("user1", userList);
        valueMap.put("user2", userList2);
        Result result = Result.success(valueMap);
        return result;
    }

    @GetMapping("/file/front/all")
    private Result frontAll(){//新增或者更新
        return Result.success(fileMapper.selectList(null));
    }

}
*/


package com.example.demo12.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.User;
import com.example.demo12.entity.User2;
import com.example.demo12.mapper.FileMapper;
import com.example.demo12.service.AdService;
import com.example.demo12.service.UserService;
import com.example.demo12.service.UserService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private AdService adservice;

    @Autowired
    private UserService2 userservice2;

    @Resource
    private FileMapper fileMapper;
    @GetMapping("/example")
    public Result get(){
        Map<String ,Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members(){
        List<Ad> list = adservice.list();
        int q1 = 0;//第一季度
        int q2 = 0;//第二季度
        int q3 = 0;//第三季度
        int q4 = 0;//第四季度
        for (Ad ad : list){
            Date adcreattime = ad.getAdcreattime();
            Quarter quarter = DateUtil.quarterEnum(adcreattime);
            switch (quarter){
                case Q1:
                    q1++;
                    break;
                case Q2:
                    q2++;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
                default:
                    break;
            }
        }
        List adList = CollUtil.newArrayList(q1,q2,q3,q4);


        List<User2> list2 = userservice2.list();
        int q21 = 0;//第一季度
        int q22 = 0;//第二季度
        int q23 = 0;//第三季度
        int q24 = 0;//第四季度
        for (User2 user2 : list2){
            Date nurcreattime = user2.getNurcreattime();
            Quarter quarter = DateUtil.quarterEnum(nurcreattime);
            switch (quarter){
                case Q1:
                    q21++;
                    break;
                case Q2:
                    q22++;
                    break;
                case Q3:
                    q23++;
                    break;
                case Q4:
                    q24++;
                    break;
                default:
                    break;
            }
        }
        List userList2 = CollUtil.newArrayList(q21,q22,q23,q24);
        Map valueMap = new HashMap();
        valueMap.put("user1", adList);
        valueMap.put("user2", userList2);
        Result result = Result.success(valueMap);
        return result;
    }

    @GetMapping("/file/front/all")
    private Result frontAll(){//新增或者更新
        return Result.success(fileMapper.selectList(null));
    }


















    /*@Autowired
    private UserService userservice;
    @Autowired
    private UserService2 userservice2;


    @Resource
    private FileMapper fileMapper;
    @GetMapping("/example")
    public Result get(){
        Map<String ,Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }


    @GetMapping("/members")
    public Result members(){
        List<User> list = userservice.list();
        int q1 = 0;//第一季度
        int q2 = 0;//第二季度
        int q3 = 0;//第三季度
        int q4 = 0;//第四季度
        for (User user : list){
            Date creattime = user.getCreattime();
            Quarter quarter = DateUtil.quarterEnum(creattime);
            switch (quarter){
                case Q1:
                    q1++;
                    break;
                case Q2:
                    q2++;
                    break;
                case Q3:
                    q3++;
                    break;
                case Q4:
                    q4++;
                    break;
                default:
                    break;
            }
        }
        List userList = CollUtil.newArrayList(q1,q2,q3,q4);


        List<User2> list2 = userservice2.list();
        int q21 = 0;//第一季度
        int q22 = 0;//第二季度
        int q23 = 0;//第三季度
        int q24 = 0;//第四季度
        for (User2 user2 : list2){
            Date nurcreattime = user2.getNurcreattime();
            Quarter quarter = DateUtil.quarterEnum(nurcreattime);
            switch (quarter){
                case Q1:
                    q21++;
                    break;
                case Q2:
                    q22++;
                    break;
                case Q3:
                    q23++;
                    break;
                case Q4:
                    q24++;
                    break;
                default:
                    break;
            }
        }
        List userList2 = CollUtil.newArrayList(q21,q22,q23,q24);
        //List userList2 = CollUtil.newArrayList(21,2,13,4);
        Map valueMap = new HashMap();
        valueMap.put("user1", userList);
        valueMap.put("user2", userList2);
        Result result = Result.success(valueMap);
        return result;
    }

    @GetMapping("/file/front/all")
    private Result frontAll(){//新增或者更新
        return Result.success(fileMapper.selectList(null));
    }*/

}

