package com.example.demo12.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo12.controller.common.Result;
import com.example.demo12.controller.common.RoleEnum;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.News;
import com.example.demo12.entity.Product;
import com.example.demo12.entity.User;
import com.example.demo12.service.NewsService;
import com.example.demo12.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.web.bind.annotation.*;
//import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    private NewsService newsService;

    @GetMapping
    public List<News> show(){
        return newsService.list();
    }

    @PostMapping   //数据添加、修改路径
    private boolean save(@RequestBody News news){//新增或者更新
        //news.setTime(DateUtil.now());//加时间的，没加进去，报错了
        if(news.getId() == null){
            //新增
            news.setTime(DateUtil.today());

            //加入身份识别，只能看自己的①setNurname错误
            news.setAdname(TokenUtils.getCurrentAd().getAdname());
        }
//        end1();
        return newsService.saveNews(news);
    }

    /*//积分兑换加保存
    @PostMapping("/changecredit")    //数据添加、修改路径
    private boolean chancecredit(@RequestBody News news){//新增或者更新
        *//*if(news.getId() == null){
            //新增
            news.setTime(DateUtil.today());
            //加入身份识别，只能看自己的①setNurname错误
            news.setAdname(TokenUtils.getCurrentAd().getAdname());
        }*//*
        return newsService.saveNews2(news);
    }*/

    @PostMapping("/statusUpdate")   //数据添加、修改路径
    private boolean statusUpdate(@RequestBody News news){//新增或者更新
        return newsService.updateStatus(news);
    }



    /*@GetMapping("/descr/{descr}")
    public Result findByDescr(@PathVariable String descr){
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("descr",descr);
        News one = newsService.getOne(queryWrapper);
        return Result.success(one);
    }*/


    //删除
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return newsService.removeById(id);
    }

    //批量删除接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return newsService.removeByIds(ids);
    }



    @GetMapping("/page")
    public IPage<News> findPage(@RequestParam Integer pageNum,
                              @RequestParam Integer pageSize,
                              @RequestParam(defaultValue = "") String descr

                                ){
        //@RequestParam String statue
        //(defaultValue = "")是为了and查询时，一些可以不查。
        IPage<News> page = new Page<>(pageNum,pageSize);
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(descr)){//进行一个判断，如果不为空再进行查询
            queryWrapper.like("descr",descr);
        }


        //加入身份识别的②
        Ad currentAd = TokenUtils.getCurrentAd();
        if(RoleEnum.ROLE_NURSE.toString().equals(currentAd.getAdrole())){
            queryWrapper.eq("adname",currentAd.getAdname()).or().eq("statue","审核通过");
        }

//        if(statue.equals("审核通过")){
//            queryWrapper.eq("statue",statue);
//        }

        //
        return newsService.page(page,queryWrapper);

    }

}
