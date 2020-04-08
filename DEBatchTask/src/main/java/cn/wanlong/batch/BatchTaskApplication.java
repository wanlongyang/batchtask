package cn.wanlong.batch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import cn.wanlong.batch.util.ApplicationContextUtils;
import cn.wanlong.batch.util.PropertyUtil;

@SpringBootApplication
@MapperScan("cn.newhope.batch.mapper")
//@EnableEurekaClient
//@EnableFeignClients
@EnableConfigurationProperties
public class BatchTaskApplication {

	public static void main(String[] args)  {
        SpringApplication sa = new SpringApplication(BatchTaskApplication.class);
        sa.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent event) {
                ApplicationContextUtils.setContext(event.getApplicationContext());
                    initApplication();

            }

        });
        sa.run(args);
	}

    /**
     * 加载配置文件到内存
     * @return
     */
    public static void initApplication() {
        PropertyUtil.loadApiProps();
    }
	
}

