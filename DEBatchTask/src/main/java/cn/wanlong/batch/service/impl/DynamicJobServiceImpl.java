package cn.wanlong.batch.service.impl;


import cn.wanlong.batch.entity.JobEntity;
import cn.wanlong.batch.mapper.JobEntityMapper;
import cn.wanlong.batch.service.DynamicJobService;
import cn.wanlong.batch.util.CommUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DynamicJobServiceImpl implements DynamicJobService {


    private static Logger logger= LoggerFactory.getLogger(DynamicJobServiceImpl.class);

    public static final String jobStatusClose="CLOSE";
    public static final String jobStatusOpen="OPEN";

    @Autowired
    private JobEntityMapper jobEntityMapper;

    //根据job名称获取某个job
    @Override
    public JobEntity getJobEntityByName(String name) {
        return jobEntityMapper.selectByJobName(name);
    }


    //从数据库中加载获取到所有Job
    @Override
    public List<JobEntity> loadJobs() {
        List<JobEntity> list = new ArrayList<>();
        jobEntityMapper.findAll().forEach(list::add);
        return list;
    }
    

    //从数据库中加载获取到分组的所有Job
    @Override
    public List<JobEntity> loadJobs(String group) {
        List<JobEntity> list = new ArrayList<>();
        jobEntityMapper.findByGroup(group).forEach(list::add);
        return list;
    }
    
    //更新锁的状态
    @Override
    public int updateJobStatus(String id){

        int result=0;
        JobEntity jobEntity=jobEntityMapper.selectByPrimaryKey(id);
        if(jobEntity!=null){
            String status=jobEntity.getStatus();
            if(jobStatusClose.equals(status)){
                jobEntity.setStatus(jobStatusOpen);
            }else {
                jobEntity.setStatus(jobStatusClose);
            }
            result=jobEntityMapper.updateByPrimaryKey(jobEntity);
        }
        return result;
    }

    //获取JobDataMap.(Job参数对象)
    @Override
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getEntityName());
        map.put("group", job.getEntityGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("JobDescription", job.getDescription());
        map.put("jobClass", job.getJobClass());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }
    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    @Override
    public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) {
        try {
            return JobBuilder.newJob((Class<? extends Job>)Class.forName(map.getString("jobClass")).newInstance().getClass())
                    .withIdentity(jobKey)
                    .withDescription(description)
                    .setJobData(map)
                    .storeDurably()
                    .build();
        }catch (Exception e){

        }
        return null;
    }
    //获取Trigger (Job的触发器,执行规则)
    @Override
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getEntityName(), job.getEntityGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    //获取Trigger (Job的触发器,执行规则) 立即执行
    @Override
    public Trigger getnowTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getEntityName(), job.getEntityGroup())
                .startNow()
                .build();
    }

    //获取JobKey,包含Name和Group
    @Override
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getEntityName(), job.getEntityGroup());
    }


    @Override
    public String addJobEntity(JobEntity jobEntity) throws Exception{
        String uuid= CommUtil.get32UUID();
        jobEntity.setEntityId(uuid);
        String result="";
        if(!CronExpression.isValidExpression(jobEntity.getCron())){
            logger.info("定时器表达式错误");
            throw new Exception("定时器表达式错误");
        }else {
            int num=jobEntityMapper.insertSelective(jobEntity);
            result=num==1?"添加成功":"添加失败";
        }
        return result;
    }

    @Override
    public int deleteJobEntity(String name) {
        return jobEntityMapper.deleteByJobName(name);
        //return 0;
    }

    @Override
    public int updateJobEntityByname(JobEntity jobEntity) throws Exception{

        if(!CronExpression.isValidExpression(jobEntity.getCron())){
            logger.info("定时器表达式错误");
            throw new Exception("定时器表达式错误");
        }else {
            return jobEntityMapper.updateJobByName(jobEntity);
        }
        //return 0;
    }

    @Override
    public int openJob(String name) throws Exception{

        int result=0;
        JobEntity jobEntity=jobEntityMapper.selectByJobName(name);
        if(jobEntity!=null){
            jobEntity.setStatus(jobStatusOpen);
            result=jobEntityMapper.updateByPrimaryKey(jobEntity);
        }else {
            logger.info("job不存在，请查看数据库t_job_entity表");
            throw new Exception("job不存在，请查看数据库t_job_entity表");
        }
        return result;

    }

    @Override
    public int closeJob(String name) throws Exception {
        int result=0;
        JobEntity jobEntity=jobEntityMapper.selectByJobName(name);
        if(jobEntity!=null){
            jobEntity.setStatus(jobStatusClose);
            result=jobEntityMapper.updateByPrimaryKey(jobEntity);
        }else {
            logger.info("job不存在，请查看数据库t_job_entity表");
            throw new Exception("job不存在，请查看数据库t_job_entity表");
        }
        return result;
    }
}
