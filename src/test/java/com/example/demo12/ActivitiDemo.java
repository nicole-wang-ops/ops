
package com.example.demo12;
import cn.hutool.core.io.IoUtil;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LocaleID;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;


public class ActivitiDemo {


    @Test
    public void testDeployment(){
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
    }

    //启动流程实例
    @Test
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


    //解决assignee不能保存问题，但是没解决
    @Test
    public void deplomentProcessDefination(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment  deployment = processEngine
                .getRepositoryService()
                .createDeployment()
                .addClasspathResource("bpmn/file.bpmn")
                .name("the_bug")

                .deploy();

        System.out.println(deployment + " :: deployment");
        System.out.println(deployment.getName());
    }

    //查询当前个人待执行的任务
    @Test
    public void testFindPersonalTaskList(){
        //任务负责人
        String assignee = "nurse";//因为assignee保存不上，所以先注释掉了①
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //创建TaskService
        TaskService taskService = processEngine.getTaskService();
        //根据流程key和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("myFile")//流程Key
                .taskAssignee(assignee)//只查询该任务负责人的任务//因为assignee保存不上，所以先注释掉了②
                .list();

        for(Task task : list){
            System.out.println("流程实例id:" + task.getProcessInstanceId());
            System.out.println("任务id:" + task.getId());
            System.out.println("任务负责人:" + task.getAssignee());//因为assignee保存不上，所以先注释掉了③
            System.out.println("任务名称:" + task.getName());
        }
    }

    //完成任务
    @Test
    public void completTask(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
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
    }

    //查询流程定义
    @Test
    public void queryProcessDefinition(){
        //获取索引
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //得到ProcessDefinitionQuery
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        //查询出当前所有的流程定义
        //条件：processDefinitionKey = evection
        //orderByProcessDefinitionVersion 按照版本排序
        //desc倒序
        //list返回集合
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("myFile")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        //输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList){
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID="+processDefinition.getDeploymentId());
        }
    }

    //查询流程实例
    @Test
    public void queryProcessInstance(){
        //流程定义key
        String processDefinitionKey = "myFile";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
        for(ProcessInstance processInstance : list){
            System.out.println("---------------------------");
            System.out.println("流程实例id："+processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id："+processInstance.getProcessDefinitionId());
            System.out.println("是否执行完成："+processInstance.isEnded());
            System.out.println("是否暂停："+processInstance.isSuspended());
            System.out.println("当前活动标识："+processInstance.getActivityId());
            System.out.println("业务关键字："+processInstance.getBusinessKey());
        }
    }

    //删除
    @Test
    public void deleteDeployment(){
        //流程部署id
        String deploymentId = "30013";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除流程定义，如果该流程定义已有实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        //删除true级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        //repositoryService.deleteDeployment(deploymentId,true);


    }

    //查看.bpmn和.png文件并下载下来
    //做web时可以用，开发人员和业务人员可以实时看到业务的定义（但是我这里好像没传到数据）
    @Test
    public void queryBpmnFile() throws IOException{
        //1、得到引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2、获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //3、得到查询器：ProcessDefinitionQuery，设置查询条件，得到想要的流程定义

        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myFile")
                .list();
        for(ProcessDefinition processDefinition : definitionList){
            //4、通过流程定义信息，得到部署ID
            String deploymentId = processDefinition.getDeploymentId();
            //5、通过repositoryService的方法，实现读取图片信息和bpmn信息
            //png图片的流
            InputStream pngInput = repositoryService.getResourceAsStream(deploymentId,processDefinition.getDiagramResourceName());
            //bpmn文件的流
            InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId,processDefinition.getResourceName());
            //6、构造OutputSteam流
            File file_bpmn_png = new File("D:/myFile.png");//这个是你选择将这个图片下载到哪里去的位置
            File file_bpmn = new File("D:/myFile.bpmn");//同上
            FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
            FileOutputStream pngOut = new FileOutputStream(file_bpmn_png);
            //7、输入流、输出流的转换
            IOUtils.copy(pngInput,pngOut);
            IOUtils.copy(bpmnInput,bpmnOut);
            //关闭流
            pngOut.close();
            bpmnOut.close();
            pngInput.close();
            bpmnInput.close();


        }

        //单个
        /*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myFile")
                .singleResult();
        //4、通过流程定义信息，得到部署ID
        String deploymentId = processDefinition.getDeploymentId();
        //5、通过repositoryService的方法，实现读取图片信息和bpmn信息
        //png图片的流
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId,processDefinition.getDiagramResourceName());
        //bpmn文件的流
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId,processDefinition.getResourceName());
        //6、构造OutputSteam流
        File file_bpmn_png = new File("D:/myFile.png");
        File file_bpmn = new File("D:/file.bpmn");
        FileOutputStream bpmnOut = new FileOutputStream(file_bpmn);
        FileOutputStream pngOut = new FileOutputStream(file_bpmn_png);
        //7、输入流、输出流的转换
        IOUtils.copy(pngInput,pngOut);
        IOUtils.copy(bpmnInput,bpmnOut);
        //关闭流
        pngOut.close();
        bpmnOut.close();
        pngInput.close();
        bpmnInput.close();
*/
    }

    //查看历史信息
    @Test
    public void findHistoryInfo(){
        //获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        //获取actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        //查询actinst表，条件：根据InstanceId查询，查询一个流程的所有历史信息
        instanceQuery.processInstanceId("10001");
        //查询actinst表，条件：根据DefinitionId查询，查询一种流程的所有历史信息
        //instanceQuery.processDefinitionId("myFile:4:7505");
        //增加排序操作，orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        //查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
        //输出
        for (HistoricActivityInstance hi : activityInstanceList){
            System.out.println(hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<=================>");

        }

    }



//zip压缩文件上传方式

/* @Test
    public void deployProcessByZip(){
        //定义zip输入流
        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "bpmn/file.bpmn.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        //获取repositoryService
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //流程部署
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        System.out.println("流程部署id:" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }*/

    //流程任务
    /*@Test
    public void completTask(){
        //流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //获取askService
        TaskService taskService = processEngine.getTaskService();
        //根据流程key和 任务的负责人 查询任务
        //返回一个任务对象
        Task task =  taskService.createTaskQuery()
                .processDefinitionKey("myFile") //流程Key
                .taskAssignee("nurser") //要查询的负责人
                .singleResult();
        //完成任务，参数，任务id
        taskService.complete(task.getId());
    }*/





    //会默认按照Resources目录下的activiti.cfg.xml创建流程引擎

/*
ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void test() {
        //读对应的配置文件，初始化
        testCreateProcessEngineByCfgXml();
        //部署
        deployProcess();
        //启动
        startProcess();
        //查看
        queryTask();
        //处理
        handleTask();
    }

    //根据配置文件activiti.cfg.xml创建ProcessEngine
    @Test
    public void testCreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }

     //发布流程
     //RepositoryService
    @Test
    public void deployProcess() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment(); //创建部署对象
        builder.name("服务商文件上传审批流程"); //流程名称
        builder.addClasspathResource("bpmn/file.bpmn"); //加载资源文件    xml发布有问题
        builder.addClasspathResource("bpmn/file.myFile.png");
        Deployment deploy = builder.deploy();
        System.out.println("流程部署的ID: "+deploy.getId());
    }


     // 启动流程
     // RuntimeService
    @Test
    public void startProcess() {
        RuntimeService runtimeService = processEngine.getRuntimeService();

        ProcessInstance leaveInstance = runtimeService.startProcessInstanceByKey("myFile");

        System.out.println("流程id: "+leaveInstance.getId());
    }

     //查看任务
     //TaskService
    @Test
    public void queryTask() {
        TaskService taskService = processEngine.getTaskService();
        //根据assignee(代理人)查询任务
//        String assignee = "项目经理";
//        String assignee = "组长";
        String assignee = "张三";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee).list();
        System.out.println("任务数量："+tasks.size());
        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            Task task = tasks.get(i);

        }
        for (Task task : tasks) {

            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }
    }



    @Test
    public void handleTask() {
        TaskService taskService = processEngine.getTaskService();

        Map<String, Object> variables = new HashMap<>();
        variables.put("day", 2);  // 写申请流程变量
        variables.put("action","同意"); //审批流程变量  （驳回/同意）

        //根据上一步生成的taskId执行任务
        String taskId = "10005";//5005
        taskService.complete(taskId, variables);
        System.out.println("任务完成");
    }
*/

}

