package cn.wanlong.batch;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.wanlong.batch.controller.executor.AsyncTask;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatchTaskApplication.class)
public class TaskTests {
	@Autowired
	private AsyncTask asyncTask;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Test
	public void AsyncTaskTest() throws InterruptedException, ExecutionException {
		Future<String> task1 = asyncTask.doTask1();
		Future<String> task2 = asyncTask.doTask2();
		
		while(true) {
			if(task1.isDone() && task2.isDone()) {
				logger.info("Task1 result: {}", task1.get());
				logger.info("Task2 result: {}", task2.get());
				break;
			}
			Thread.sleep(1000);
		}
		
		logger.info("All tasks finished.");
	}
}
