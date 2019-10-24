package com.task.quartz.quartz.quartz.listenner;

import com.task.quartz.quartz.quartz.utils.QuartzManager;
import com.task.quartz.quartz.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *@description 1、启动项目，启动task监听 首先项目启动时进入监听器初始化定时任务。
 * 使用ScheduleJobInitListener来实现实时更新，
 * 使用QuartzManager来实现动态维护。
 * ps:（动态维护的本质是更改trigger ）
 *@param
 *@return
 */
@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {

    @Autowired
    TaskService scheduleJobService;
    @Autowired
    QuartzManager quartzManager;

    @Override
    public void run(String... arg0) throws Exception {
        try {
            //这里进数据库获取所有状态可用的任务。然后用quartzManager.addJob()方法添加
            scheduleJobService.initSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}