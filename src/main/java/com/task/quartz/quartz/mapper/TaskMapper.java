package com.task.quartz.quartz.mapper;

import com.task.quartz.quartz.entity.TaskDO;
import com.task.quartz.quartz.vo.TaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@description ${description}
 *@date 2019/10/24 15:04
 *@author syz
 */
public interface TaskMapper {

    TaskDO get(Long id);
    
    List<TaskDO> list();
    
    List<TaskVO> listTaskVoByDesc(@Param("desc") String desc);
    
    int save(TaskDO task);
    
    int update(TaskDO task);
    
    int remove(Long id);
    
    int removeBatch(Long[] ids);
}
