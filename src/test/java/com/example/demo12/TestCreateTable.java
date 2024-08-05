package com.example.demo12;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.jupiter.api.Test;

public class TestCreateTable {
    @Test
    public void textCreateTable(){
//*
//默认创建方式
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        System.out.println(processEngine);

        //通用的创建方式，指定配置文件名和Bean名称
        ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        ProcessEngine processEngine1 = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine1);

    }

}
