package cn.wanlong.batch.service;

import cn.wanlong.batch.entity.JobResult;

public interface JobResultService {

    int insert(JobResult record);

    int updateJobResultById(JobResult record);

}
