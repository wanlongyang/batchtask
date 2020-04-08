package cn.newhope.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import cn.newhope.batch.BatchTaskApplication;
import cn.newhope.batch.redis.RedisTemplateService;
import cn.newhope.batch.util.RedisUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatchTaskApplication.class)
public class RedisTest {
	
	  @Autowired
	    RedisTemplateService redisTemplateService;
	  
	   @Autowired
	    StringRedisTemplate stringRedisTemplate;
	   
	   @Autowired
	   RedisUtils redisUtil;
	      
	    @Test
	    public void redisTest(){
	    	
	    	long begin=System.currentTimeMillis();
	    	redisUtil.set("aaa", "asasa");
	    	Object value=redisUtil.get("aaa");
	    	long end=System.currentTimeMillis();
	    	//System.out.print(redisUtil.get("aaa")+"耗时"+(end-begin));
	    	
	    	
	    }

}
