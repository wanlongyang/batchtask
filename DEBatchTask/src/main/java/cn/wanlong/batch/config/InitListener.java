package cn.wanlong.batch.config;

import cn.wanlong.batch.controller.JobController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitListener implements CommandLineRunner {

    /**
     * 监听器 用于初始化项目
     */
    @Autowired
    JobController jobController;
    @Override
    public void run(String... args) throws Exception {
         jobController.initJobs();
    }
}
