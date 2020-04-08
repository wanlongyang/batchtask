package cn.wanlong.batch.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    public synchronized static void loadProps(String propertyFile){
        logger.info("开始加载properties文件内容.......");
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyFile);
            //尝试从文件系统读取文件
            if(in == null) {
            	in = new FileInputStream(propertyFile);
            }
            props.load(in);
        } catch (FileNotFoundException e) {
        	logger.error("没有找到配置文件【{}】", propertyFile);
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
            	logger.error("关闭配置文件【{}】异常", propertyFile);
            }
        }
        logger.info("加载配置文件【{}】完成", propertyFile);
        logger.info("{}文件内容：" + props, propertyFile);
    }
    
    public static void loadApiProps() {
		// String apiFile = "DeBatchTaskApi.properties";
		//String apiFile = "/app/conf/batchTask/api.properties";
       String apiFile =ApplicationContextUtils.getContext().getEnvironment().getProperty("api.file");
        loadProps(apiFile);
    }

    public static String getProperty(String key){
        if(null == props) {
        	loadApiProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
        	loadApiProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
