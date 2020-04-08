package cn.newhope.batch.service;

import cn.newhope.batch.entity.JobResult;

public interface JobResultService {

    int insert(JobResult record);

    int updateJobResultById(JobResult record);

}
