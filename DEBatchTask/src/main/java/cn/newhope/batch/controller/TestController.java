package cn.newhope.batch.controller;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.newhope.batch.entity.JobEntity;
import cn.newhope.batch.service.DynamicJobService;

import javax.sql.DataSource;

@RestController
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);



	@Autowired
	private DynamicJobService jobService;


	@Autowired
	private DataSource dataSource;
	    
	@RequestMapping("/hello1")
	public String test() {

		System.out.println((DruidDataSource)dataSource);
		logger.error("数据库报错"+((DruidDataSource) dataSource).getMaxActive());
		return "hello,this is a springboot demo";
	}

	 



	 
}
