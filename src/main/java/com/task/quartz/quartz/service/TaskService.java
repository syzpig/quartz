package com.task.quartz.quartz.service;

import com.github.pagehelper.PageHelper;

import com.github.pagehelper.PageInfo;
import com.task.quartz.quartz.entity.TaskDO;
import com.task.quartz.quartz.enums.JobStatusEnum;
import com.task.quartz.quartz.mapper.TaskMapper;
import com.task.quartz.quartz.quartz.utils.QuartzManager;
import com.task.quartz.quartz.vo.TaskVO;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TaskService  {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    QuartzManager quartzManager;

    public TaskDO get(Long id) {
        return taskMapper.get(id);
    }

    public List<TaskDO> list( ) {
        return   taskMapper.list();
    }

    public int save(TaskDO task) {
       /* task.setJobStatus(JobStatusEnum.STOP.getCode());
        task.setCreateUser(((UserDO)SecurityUtils.getSubject().getPrincipal()).getUsername());
        task.setCreateTime(new Date());
        task.setUpdateUser(((UserDO)SecurityUtils.getSubject().getPrincipal()).getUsername());
        task.setUpdateTime(new Date());*/
        return taskMapper.save(task);
    }

    public int update(TaskDO task) {
        return taskMapper.update(task);
    }

    public int remove(Long id) {
        try {
            TaskDO task = get(id);
            quartzManager.deleteJob(task);
            return taskMapper.remove(id);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return 0;
        }

    }

    public int removeBatch(Long[] ids) {
        for (Long id : ids) {
            try {
                TaskDO task = get(id);
                quartzManager.deleteJob(task);
            } catch (SchedulerException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return taskMapper.removeBatch(ids);
    }

    public void initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<TaskDO> jobList = taskMapper.list();
        for (TaskDO task : jobList) {
            if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus())) {
                quartzManager.addJob(task);
            }
        }
    }

    public void changeStatus(Long jobId, String jobStatus) throws SchedulerException {
        TaskDO task = get(jobId);
        if (task == null) {
            return;
        }
        if (JobStatusEnum.STOP.getCode().equals(jobStatus)) {
            quartzManager.deleteJob(task);
            task.setJobStatus(JobStatusEnum.STOP.getCode());
        } else {
            task.setJobStatus(JobStatusEnum.RUNNING.getCode());
            quartzManager.addJob(task);
        }
        update(task);
    }

    public void updateCron(Long jobId) throws SchedulerException {
        TaskDO task = get(jobId);
        if (task == null) {
            return;
        }
        if (JobStatusEnum.RUNNING.getCode().equals(task.getJobStatus())) {
            quartzManager.updateJobCron(task);
        }
        update(task);
    }

    public void run(TaskDO task) throws SchedulerException {
        quartzManager.runJobNow(task);
    }

}
