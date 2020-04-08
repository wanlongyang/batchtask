package cn.newhope.batch.service;



import cn.newhope.batch.entity.JobEntity;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/**
 *
 */

public interface DynamicJobService {

    public JobEntity getJobEntityByName(String name);

    public List<JobEntity> loadJobs();
    
	public List<JobEntity> loadJobs(String group);

    public int updateJobStatus(String id);

    public JobDataMap getJobDataMap(JobEntity job);

    public JobKey getJobKey(JobEntity job);

    public Trigger getnowTrigger(JobEntity job);

    public Trigger getTrigger(JobEntity job);

    public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map);

    public String addJobEntity(JobEntity jobEntity) throws Exception;

    public int deleteJobEntity(String name);

    public int updateJobEntityByname(JobEntity jobEntity) throws Exception;

    public int openJob(String name) throws Exception;

    public int closeJob(String name) throws Exception;



}