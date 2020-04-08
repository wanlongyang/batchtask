package cn.newhope.batch.mapper;

import java.util.List;

import cn.newhope.batch.entity.JobEntity;

public interface JobEntityMapper {
    int deleteByPrimaryKey(String entityId);

    int insert(JobEntity record);

    int insertSelective(JobEntity record);

    JobEntity selectByPrimaryKey(String entityId);

    int updateByPrimaryKeySelective(JobEntity record);

    int updateByPrimaryKey(JobEntity record);

    List<JobEntity> findAll();
    
    List<JobEntity> findByGroup(String group);

    JobEntity selectByJobName(String name);


    int deleteByJobName(String name);

    int updateJobByName(JobEntity jobEntity);

}