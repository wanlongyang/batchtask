package cn.wanlong.batch.controller;


import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;

import com.alibaba.druid.util.StringUtils;

import cn.wanlong.batch.entity.JobEntity;
import cn.wanlong.batch.service.DynamicJobService;
import cn.wanlong.batch.util.PropertyUtil;

import java.util.Set;

/**
 *
 */
@RestController
public class JobController {

    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private DynamicJobService jobService;

    
    //根据job名字重启某个Job
    @RequestMapping("/init")
    public void initJobs() {
      try {
          reStartAllJobs();
          logger.info("INIT SUCCESS");
      } catch (SchedulerException e) {
          logger.info("INIT EXCEPTION : " + e.getMessage());
          e.printStackTrace();
      }
  }

    //根据job名字重启某个Job
    @RequestMapping("/refresh/{name}")
    public String refresh(@PathVariable String name) throws SchedulerException {
        String result;
        JobEntity entity = jobService.getJobEntityByName(name);
        if (entity == null) return "error: id is not exist ";
        synchronized (logger) {
            JobKey jobKey = jobService.getJobKey(entity);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(jobKey);
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
            JobDataMap map = jobService.getJobDataMap(entity);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
            if (entity.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
                result = "Refresh Job : " + entity.getEntityName()  + " success !";
            } else {
                result = "Refresh Job : " + entity.getEntityName()  + " failed ! , " +
                        "Because the Job status is " + entity.getStatus();
            }
        }
        return result;
    }

    /**
     * 定时器立即执行
     * @param name
     * @return
     * @throws SchedulerException
     */
    @RequestMapping("/run/{name}")
    public String startNow(@PathVariable String name) throws SchedulerException {
        String result;
        JobEntity entity = jobService.getJobEntityByName(name);
        if (entity == null) return "error: id is not exist ";
        synchronized (logger) {
            JobKey jobKey = jobService.getJobKey(entity);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(jobKey);
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
            JobDataMap map = jobService.getJobDataMap(entity);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
            if (entity.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, jobService.getnowTrigger(entity));
                result = "run Job : " + entity.getEntityName() + " success !";
            } else {
                result = "run Job : " + entity.getEntityName() + " failed ! , " +
                        "Because the Job status is " + entity.getStatus();
            }
        }
        return result;
    }


    /**
     * 添加job
     * @param jobEntity
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object addJobEntity(@RequestBody JobEntity jobEntity){

        Object result="";
        try {
            result=jobService.addJobEntity(jobEntity);
        }catch (Exception e){
            result = "EXCEPTION : " + e.getMessage();
        }
        return result;
    }

    /**
     * 更新job
     * @param jobEntity
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Object updateJobEntity(@RequestBody  JobEntity jobEntity){
        Object result="";
        try {
            result=jobService.updateJobEntityByname(jobEntity);
        }catch (Exception e){
            result = "EXCEPTION : " + e.getMessage();
        }
        return result;
    }

    //重启数据库中所有的Job
    @RequestMapping("/refresh/all")
    public String refreshAll() {
        String result;
        try {
            reStartAllJobs();
            result = "SUCCESS";
        } catch (SchedulerException e) {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "refresh all jobs : " + result;
    }

    // 开启或者关闭某个job
    @RequestMapping("open/{name}")
    public String openJob(@PathVariable String name) {
        String result;
        try {
            int num=jobService.openJob(name);
            refresh(name);
            result= num==1?"success":"failure";
            logger.info("开启job："+result);
        } catch (Exception e) {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "update job : " + result;
    }

    @RequestMapping("close/{name}")
    public String closeJob(@PathVariable String name) {
        String result;
        try {
            synchronized (logger){
                Scheduler scheduler = schedulerFactoryBean.getScheduler();
                JobEntity jobEntity=jobService.getJobEntityByName(name);
                JobKey jobKey = jobService.getJobKey(jobEntity);
                scheduler.pauseJob(jobKey);
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                boolean flag=scheduler.deleteJob(jobKey);
                logger.info(" 删除job成功标志："+flag);
                int num=jobService.closeJob(name);
                result= num==1?"success":"failure";
            }
        } catch (Exception e) {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "update job : " + result;
    }

    /**
     * 重新启动所有的job
     */
    @SuppressWarnings("unchecked")
	private void reStartAllJobs() throws SchedulerException {
    	String jobGroup = PropertyUtil.getProperty("job.group");
    	if(StringUtils.isEmpty(jobGroup)) {
    		logger.error("job.group属性未配置");
    		return;
    	}
        synchronized (logger) {                                                         //只允许一个线程进入操作
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
             GroupMatcher groupMather = GroupMatcher.anyGroup();
            Set<JobKey> set = scheduler.getJobKeys(groupMather);
            scheduler.pauseJobs(groupMather);                               //暂停所有JOB
            for (JobKey jobKey : set) {                                                 //删除从数据库中注册的所有JOB
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
            }
            for (JobEntity job : jobService.loadJobs(jobGroup)) {                               //从数据库中注册的所有JOB
                logger.info("Job register name : {} , group : {} , cron : {}", job.getEntityName(), job.getEntityGroup(), job.getCron());
                JobDataMap map = jobService.getJobDataMap(job);
                JobKey jobKey = jobService.getJobKey(job);
                JobDetail jobDetail = jobService.geJobDetail(jobKey, job.getDescription(), map);
                if (job.getStatus().equals("OPEN")) {
                    scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
                }
                else{
                    logger.info("Job jump name : {} , Because {} status is {}", job.getEntityName(), job.getEntityName(), job.getStatus());
                }
            }
        }
    }
}