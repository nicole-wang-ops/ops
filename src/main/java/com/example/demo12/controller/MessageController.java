package com.example.demo12.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Constants;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.*;
import com.example.demo12.mapper.AdMapper;
import com.example.demo12.mapper.MessageMapper;
import com.example.demo12.service.AdService;
import com.example.demo12.service.MessageService;
import com.example.demo12.utils.TokenUtils;
import io.swagger.models.auth.In;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{

    @Resource
    private MessageService messageService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private AdMapper adMapper;
    @Resource
    private AdService adService;
    @Resource
    HttpServletRequest request;

    @GetMapping("/adname/{adname}")
    public Result findByAdname(@PathVariable String adname){
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("adname",adname);
        Ad one = adService.getOne(queryWrapper);
        return Result.success(one);
    }
    @GetMapping("/content/{content}")
    public Result findByContent(@PathVariable String content){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content",content);
        Message one = messageService.getOne(queryWrapper);
        return Result.success(one);
    }

    //正常用的messageMapper


    //保存评论
    @PostMapping
    public Result save(@RequestBody Message Message){
//        messageMapper.save(message);
        Message.setAdname(getAd().getAdname());
        Message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageMapper.insert(Message));
    }

    @PostMapping("/saveinteg")
    private boolean saveMessage1(@RequestBody Message message){//新增或者更新
        //news.setTime(DateUtil.now());//加时间的，没加进去，报错了
        if(message.getId() == null){
            //新增
            message.setTime(DateUtil.today());
            message.setAdname(TokenUtils.getCurrentAd().getAdname());
        }
        return messageService.saveMessage1(message);
    }

    @PostMapping("/foreignId/foreignId")
    public boolean buyFore(@RequestBody JSONObject jo) {
        messageService.saveProMessage(jo.getInteger("id"));
        return true;
    }

    //评分
    @GetMapping("/rate/{foreignId}")
    public Map<String,Object> list(@PathVariable Integer foreignId){
        Map<String,Object> map = new HashMap<>();
        //所有评论
        List<Message> messages = messageMapper.findAllByForeignId(foreignId);

        //先给map.put()一个默认值
        map.put("rate",BigDecimal.ZERO);
        //只有一级评论可以评分
        List<Message> messageList = messages.stream().filter(message -> message.getRate() != null).collect(Collectors.toList());
        //所以评分的总和、rate为均值
        messageList.stream().map(Message::getRate).reduce(BigDecimal::add).ifPresent(res -> {
            map.put("rate",res.divide(BigDecimal.valueOf(messageList.size()),1, RoundingMode.HALF_UP));//HALF_UP向上取整
        });
        map.put("messages",messages);
        return map;
    }





    /*@PostMapping
    private Result save2(@RequestBody Message message){//新增或者更新
        return Result.success(messageService.saveOrUpdate(message));
    }*/

    @PutMapping
    public Result update(@RequestBody Message Message){
        return Result.success(messageMapper.updateById(Message));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        messageMapper.deleteById(id);
        return  Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        return Result.success(messageMapper.selectById(id));
    }

    @GetMapping
    public Result findAll(){
        return Result.success(messageMapper.selectList(null));
    }

    //查询所有数据
    @GetMapping("/foreign/{foreignId}")

    public Result foreign(@PathVariable Integer foreignId){
        return Result.success(findByForeign(foreignId));
    }
    @PostMapping("/foreign/{foreignId}")

    public boolean foreign(@RequestBody JSONObject jo){
        messageService.saveProMessage(jo.getInteger("id"));
        return true;
        //return Result.success(findByForeign(foreignId));
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam(required = false, defaultValue = "") String adname,
                           @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().like(Message::getContent,adname).orderByDesc(Message::getId);
        return Result.success(messageMapper.selectPage(new Page<>(pageNum,pageSize),query));
    }

//    @GetMapping("/")
    private List<Message2> findByForeign(Integer foreignId){
        //根据foreignId 0 查询所有的留言数据
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getForeignId,foreignId).orderByDesc(Message::getId);
        List<Message> list = messageMapper.selectList(queryWrapper);

        List<Message2> resultList = Lists.newArrayList();
//        BeanUtils.copyProperties(list, resultList);
//        BeanUtil.copyProperties(list, resultList);



        Map<Integer, Message> map2 = list.stream().collect(Collectors.toMap(Message::getId, each->each, (value1, value2) -> value1));
//        Map<Integer, Message2> map2 = resultList.stream().collect(Collectors.toMap(Message2::getId, each->each, (value1, value2) -> value1));


        //循环所有留言数据
        for (Message msg : list){

            Message2 msg2 = new Message2();
            BeanUtils.copyProperties(msg, msg2);

            Ad one = adMapper.selectOne(Wrappers.<Ad>lambdaQuery().eq(Ad::getAdname,msg.getAdname()));
            if(StrUtil.isNotBlank(one.getAdavatar())){
                msg2.setAdavatar(one.getAdavatar());
            }else{
                //默认头像
                msg2.setAdavatar("https://pics0.baidu.com/feed/3bf33a87e950352ae8acd52954f19efeb3118b04.jpeg?token=e298da5c375b4d07ca3c4f18369cd393");
            }
            if(Objects.nonNull(msg.getParentId())) {
                continue;
            }
            LambdaQueryWrapper<Message> querySonWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getParentId,msg.getId()).orderByDesc(Message::getId);
            List<Message> sonList = messageMapper.selectList(querySonWrapper);
            if(Objects.nonNull(sonList)) {
                List<Message2> sonList2 = Lists.newArrayList();
                for (Message sonMsg : sonList){
                    Message2 sonMsg2 = new Message2();
                    BeanUtils.copyProperties(sonMsg, sonMsg2);

                    Ad one2 = adMapper.selectOne(Wrappers.<Ad>lambdaQuery().eq(Ad::getAdname,msg.getAdname()));
                    if(StrUtil.isNotBlank(one2.getAdavatar())){
                        sonMsg2.setAdavatar(one2.getAdavatar());
                    }else{
                        sonMsg2.setAdavatar("https://pics0.baidu.com/feed/3bf33a87e950352ae8acd52954f19efeb3118b04.jpeg?token=e298da5c375b4d07ca3c4f18369cd393");
                    }
                    sonList2.add(sonMsg2);
                }
                msg2.setSonMessages(sonList2);
            }

//            if(Objects.nonNull(msg.getParentId())) {
//                Integer pid = Integer.valueOf(msg.getParentId().toString());
//                if (map2.containsKey(pid)) {
//                    msg2.setParentMessage(map2.get(pid));
//                }
//            }
            //判断当前是留言是否有父级，如果有，则返回父级留言的信息
            //查询所有留言数据，如果id跟当前的parentId相等，则将其设置为父级评论信息
//            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(Message2::setParentMessage);

            resultList.add(msg2);

        }
        return resultList;
    }

}

































/*
package com.example.demo12.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Result;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.Message;
import com.example.demo12.entity.Message2;
import com.example.demo12.mapper.AdMapper;
import com.example.demo12.mapper.MessageMapper;
import com.example.demo12.service.AdService;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
public class MessageController extends BaseController{
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private AdMapper adMapper;
    @Resource
    private AdService adService;
    @Resource
    HttpServletRequest request;

    @GetMapping("/adname/{adname}")
    public Result findByAdname(@PathVariable String adname){
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("adname",adname);
        Ad one = adService.getOne(queryWrapper);
        return Result.success(one);
    }


    @PostMapping
    public Result save(@RequestBody Message Message){
        Message.setAdname(getAd().getAdname());
        Message.setTime(DateUtil.formatDateTime(new Date()));
        return Result.success(messageMapper.insert(Message));
    }

    @PutMapping
    public Result update(@RequestBody Message Message){
        return Result.success(messageMapper.updateById(Message));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        messageMapper.deleteById(id);
        return  Result.success();
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        return Result.success(messageMapper.selectById(id));
    }

    @GetMapping
    public Result findAll(){return Result.success(messageMapper.selectList(null));}

    //查询所有数据
    @GetMapping("/foreign/{foreignId}")
    public Result foreign(@PathVariable Integer foreignId){
        return Result.success(findByForeign(foreignId));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(required = false, defaultValue = "") String adname,
                              @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        LambdaQueryWrapper<Message> query = Wrappers.<Message>lambdaQuery().like(Message::getContent,adname).orderByDesc(Message::getId);
        return Result.success(messageMapper.selectPage(new Page<>(pageNum,pageSize),query));
    }

    private List<Message> findByForeign(Integer foreignId){
        //根据foreignId 0 查询所有的留言数据
        LambdaQueryWrapper<Message> queryWrapper = Wrappers.<Message>lambdaQuery().eq(Message::getForeignId,0).orderByDesc(Message::getId);
        List<Message> list = messageMapper.selectList(queryWrapper);

        List<Message2> resultList = Lists.newArrayList();
        BeanUtils.copyProperties(list, resultList);

        Map<Integer, Message2> map2 = resultList.stream().collect(Collectors.toMap(Message2::getId, each->each, (value1, value2) -> value1));

        //循环所有留言数据
        for (Message2 msg : resultList){
            Ad one = adMapper.selectOne(Wrappers.<Ad>lambdaQuery().eq(Ad::getAdname,msg.getAdname()));
            if(StrUtil.isNotBlank(one.getAdavatar())){
                msg.setAdavatar(one.getAdavatar());
            }else{
                //默认头像
                msg.setAdavatar("https://pics0.baidu.com/feed/3bf33a87e950352ae8acd52954f19efeb3118b04.jpeg?token=e298da5c375b4d07ca3c4f18369cd393");
            }

            if(map2.containsKey(msg.getParentId())) {
                msg.setParentMessage(map2.get(msg.getParentId()));
            }
            //判断当前是留言是否有父级，如果有，则返回父级留言的信息
            //查询所有留言数据，如果id跟当前的parentId相等，则将其设置为父级评论信息
//            list.stream().filter(c -> c.getId().equals(parentId)).findFirst().ifPresent(Message2::setParentMessage);

        }
        return list;
    }

}
*/
