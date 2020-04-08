package cn.newhope.batch.mapper;

import cn.newhope.batch.entity.JobResult;

public interface JobResultMapper {
    int deleteByPrimaryKey(String resultId);

    int insert(JobResult record);

    int insertSelective(JobResult record);

    JobResult selectByPrimaryKey(String resultId);

    int updateByPrimaryKeySelective(JobResult record);

    int updateByPrimaryKeyWithBLOBs(JobResult record);

    int updateByPrimaryKey(JobResult record);

    int updateJobResultById(JobResult record);
}