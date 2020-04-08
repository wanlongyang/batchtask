package cn.newhope.batch.job;

import com.alibaba.fastjson.JSONObject;

import cn.newhope.batch.entity.JobResult;
import cn.newhope.batch.service.JobResultService;
import cn.newhope.batch.util.CommUtil;
import cn.newhope.batch.util.RedisLock;
import cn.newhope.batch.util.RedisUtils;
import cn.newhope.batch.util.ServiceHelper;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.UUID;
@DisallowConcurrentExecution
@Component
public abstract class AbstractJob implements Job {

    private Logger logger = LoggerFactory.getLogger(AbstractJob.class);

    private String requestId= UUID.randomUUID().toString();

    @Autowired
    RedisUtils redisUtil;

    @Autowired
    private JobResultService jobResultService;

    /**
     *  获取锁方法
     * @param executorContext
     * @return
     */
    public boolean getLock(JobExecutionContext executorContext){

        JobDataMap map = executorContext.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        logger.info("Running Job name : {} ", map.getString("name"));
        logger.info("Running Job description : " + map.getString("JobDescription"));
        logger.info("Running Job group: {} ", map.getString("group"));
        logger.info("Running Job cron : " + map.getString("cronExpression"));
        logger.info("Running Job jar path : {} ", jarPath);
        logger.info("Running Job parameter : {} ", parameter);
        logger.info("Running Job vmParam : {} ", vmParam);
        String lockKey=map.getString("group")+"_"+map.getString("name");
        boolean flag=false;
        Jedis jedis= ServiceHelper.getJedisPool().getResource();
        try {
            logger.info("lockKey:"+lockKey+"requestId ："+requestId);
            flag= RedisLock.tryGetDistributedLock(jedis,lockKey,requestId,100000);
        }catch (Exception e){
            logger.info("锁获取异常",e);
        }finally {
            jedis.close();
        }
        return flag;
    }

    public boolean releaseLock(JobExecutionContext executorContext){

        JobDataMap map = executorContext.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        logger.info("Running Job name : {} ", map.getString("name"));
        logger.info("Running Job description : " + map.getString("JobDescription"));
        logger.info("Running Job group: {} ", map.getString("group"));
        logger.info("Running Job cron : " + map.getString("cronExpression"));
        logger.info("Running Job jar path : {} ", jarPath);
        logger.info("Running Job parameter : {} ", parameter);
        logger.info("Running Job vmParam : {} ", vmParam);
        String lockKey=map.getString("group")+"_"+map.getString("name");
        Jedis jedis= ServiceHelper.getJedisPool().getResource();
        boolean flag=false;
        try {
             flag= RedisLock.releaseDistributedLock(jedis,lockKey,requestId);
        }catch (Exception e){
            logger.info("锁获取异常",e);
        }finally {
            jedis.close();
        }
        return flag;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobResult jobResult=new JobResult();
        String uuid=CommUtil.get32UUID();
        String jobKey=jobExecutionContext.getJobDetail().getKey().getGroup()+"_"+jobExecutionContext.getJobDetail().getKey().getName();
        jobResult.setJobKey(jobKey);
        jobResult.setResultId(uuid);
        try {
            if(getLock(jobExecutionContext)){
                logger.info("任务调度拿到锁成功"+jobExecutionContext.getJobDetail().getKey());
                jobResult.setStartTime(new Date());
                jobResult.setStatus("0");
                jobResultService.insert(jobResult);
                //执行job
                JobDataMap map = jobExecutionContext.getMergedJobDataMap();
                executeJob(map.getString("parameter"));
                jobResult.setEndTime(new Date());
                jobResult.setStatus("1");
                jobResultService.updateJobResultById(jobResult);
            }else{
                logger.info("任务调度拿到锁失败"+jobExecutionContext.getJobDetail().getKey());
            }
            releaseLock(jobExecutionContext);
        }catch (Exception e){
            jobResult.setEndTime(new Date());
            jobResult.setStatus("2");
            jobResult.setExceptionInfo(JSONObject.toJSONString(e));
            jobResultService.updateJobResultById(jobResult);
            logger.info("定时器执行异常",e);
        }

    }

    //job的业务方法
    public abstract void executeJob(String parameter);

}
