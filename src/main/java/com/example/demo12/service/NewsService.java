package com.example.demo12.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo12.entity.Ad;
import com.example.demo12.entity.News;
import com.example.demo12.entity.Product;
import com.example.demo12.entity.User;
import com.example.demo12.mapper.NewsMapper;
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
import com.example.demo12.utils.TokenUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.log4j.Log4j;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
@Log4j
public class NewsService extends ServiceImpl<NewsMapper,News>{

    @Resource
    AdService adService;



    public boolean saveNews(News news) {
        if(Objects.isNull(news.getId())) {
            start1();
            testStartProcess();
        }

        /**
         * 新增文章，积分 + 5
         */
        Ad ad = TokenUtils.getCurrentAd();
        ad.setCredit(ad.getCredit() + 5);
        adService.creditUpdate(ad);

        return saveOrUpdate(news);
    }

    public boolean saveNews2(News news) {
        /**
         * 兑换，积分 - 15
         */
        Ad ad = TokenUtils.getCurrentAd();
        ad.setCredit(ad.getCredit() - 15);
        adService.creditUpdate(ad);

        return saveOrUpdate(news);
    }

    public boolean updateStatus(News news) {
        if( "审核通过".equals(news.getStatue())) {
            log.info("审核通过。。。");
            //审核通过后流程结束
            end1();
        }

        return saveOrUpdate(news);
    }

    /**
     * 创建工作流
     * @return
     */
    private String start1(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.得到RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3.使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/file.bpmn") //添加bpmn资源
                .addClasspathResource("bpmn/file.bpmn.png")
                .name("文件申请流程")
                .deploy();
        //4.输出部署信息
        System.out.println("流程部署id:" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
        return "ok";
    }

    /**
     * 启动流程实例
     */
    public void testStartProcess(){
        //1.创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.根据流程定义ID启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myFile");
        //输出内容
        System.out.println("流程定义id:" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id:" + processInstance.getId());
        System.out.println("当前文章id:" + processInstance.getActivityId());
    }


    /**
     * 审核通过
     * @return
     */
    private String end1(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        //2.获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //3.根据流程定义ID启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myFile");

        //获取taskService
        TaskService taskService = processEngine.getTaskService();
        //根据流程Key 和 任务负责人 查询任务
        //返回一个任务对象
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("myFile")//流程Key
                .taskAssignee("nurse")//要查询的负责人//因为assignee保存不上，所以先注释掉了④
                .list();
        Task task = tasks.get(0);

        //完成任务，参数：任务id
        taskService.complete(task.getId());

        System.out.println("当前文章id审核通过:" + processInstance.getActivityId());

        return "ok";
    }

}
