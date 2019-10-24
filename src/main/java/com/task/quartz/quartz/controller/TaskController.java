package com.task.quartz.quartz.controller;


import com.task.quartz.quartz.entity.TaskDO;
import com.task.quartz.quartz.enums.JobStatusEnum;
import com.task.quartz.quartz.service.TaskService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *@description ${description}
 *@date 2019/10/24 14:55
 *@author syz
 */
@Controller
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskService taskService;
    

    @GetMapping
    public String taskScheduleJob() {
        return "management/task";
    }
    
    @GetMapping("/add")
    public String taskAddScheduleJob() {
        return "management/task_add";
    }

/*    @ResponseBody
    @PostMapping("/list")
    public DataGridResult list(TaskQuery query) {
        // 查询列表数据
        DataGridResult result = taskService.list(query);
        return result;
    }*/

    @PostMapping("/edit")
    @ResponseBody
    public void edit(TaskDO task) {
        TaskDO taskServer = taskService.get(task.getId());
        if (JobStatusEnum.RUNNING.getCode().equals(taskServer.getJobStatus())) {
        }
        taskService.update(task);
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public void changeStatus(@RequestParam("id") Long id) {
        //改变通知规则时间
        try {
            taskService.updateCron(id);
           // return YYBlogResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return YYBlogResult.build(403, "任务状态修改失败");
    }

    /**
     * 删除
     */
    @PostMapping("/remove/{id}")
    @ResponseBody
    public void remove(@PathVariable("id") Long id) {
        TaskDO taskServer = taskService.get(id);
        if (JobStatusEnum.RUNNING.getCode().equals(taskServer.getJobStatus())) {
            //return YYBlogResult.build(403, "删除前请先停止任务！");
        }
        if (taskService.remove(id) > 0) {
           // return YYBlogResult.ok();
        }
       // return YYBlogResult.build(403, "删除任务失败！");
    }
    
    /**
     * 立即运行
     */
    @PostMapping("/run/{id}")
    @ResponseBody
    public void run(@PathVariable("id") Long id) {
        TaskDO taskServer = taskService.get(id);
        try {
            if (JobStatusEnum.STOP.getCode().equals(taskServer.getJobStatus())) {
            //    return YYBlogResult.build(403, "立即执行请先开启任务！");
            }
            taskService.run(taskServer);
           // return YYBlogResult.ok();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        //return YYBlogResult.build(403, "立即运行任务失败！");
    }

    /**
     * 删除
     */
    @PostMapping("/removeBatch")
    @ResponseBody
    public void removeBatch(@RequestParam("ids[]") Long[] ids) {
        for (Long id : ids) {
            TaskDO taskServer = taskService.get(id);
            if (JobStatusEnum.RUNNING.getCode().equals(taskServer.getJobStatus())) {
             //   return YYBlogResult.build(403, "删除前请先停止任务！");
            }
        }
        taskService.removeBatch(ids);
        //return YYBlogResult.ok();
    }
    
    /**
     * 新增保存
     */
    @ResponseBody
    @PostMapping("/save")
    public void save(TaskDO task) {
        if (taskService.save(task) > 0) {
           // return YYBlogResult.ok();
        }
        //return YYBlogResult.build(403, "新增任务失败！");
    }
}
