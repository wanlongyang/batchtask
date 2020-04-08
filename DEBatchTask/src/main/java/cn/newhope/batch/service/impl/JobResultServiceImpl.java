package cn.newhope.batch.service.impl;

import org.springframework.stereotype.Service;

import cn.newhope.batch.entity.JobResult;
import cn.newhope.batch.mapper.JobResultMapper;
import cn.newhope.batch.service.JobResultService;

import javax.annotation.Resource;

@Service("jobResultService")
public class JobResultServiceImpl implements JobResultService {

    @Resource
    JobResultMapper jobResultMapper;

    @Override
    public int insert(JobResult record) {
        return jobResultMapper.insertSelective(record);
    }

    @Override
    public int updateJobResultById(JobResult record) {
        return jobResultMapper.updateJobResultById(record);
    }
}
