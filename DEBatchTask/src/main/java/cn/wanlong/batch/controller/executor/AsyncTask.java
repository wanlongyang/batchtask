package cn.wanlong.batch.controller.executor;

import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Async("myExecutor")
public class AsyncTask {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public Future<String> doTask1() throws InterruptedException{
		logger.info("Task1 started.");
		long start = System.currentTimeMillis();
        Thread.sleep(5000);
        long end = System.currentTimeMillis();
        
        logger.info("Task1 finished, time elapsed: {} ms.", end-start);
        
        return new AsyncResult<>("Task1 accomplished!");
	}
	
	@Scheduled(cron = "0/5 * * * * *")
	public Future<String> doTask2() throws InterruptedException{
		logger.info("Task2 started.");
		long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        
        logger.info("Task2 finished, time elapsed: {} ms.", end-start);
        
        return new AsyncResult<>("Task2 accomplished!");
	}
}