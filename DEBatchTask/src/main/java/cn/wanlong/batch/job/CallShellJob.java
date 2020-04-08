package cn.wanlong.batch.job;

import cn.wanlong.batch.util.ShellExcutor;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CallShellJob extends AbstractJob{

    private static Logger logger= LoggerFactory.getLogger(CallShellJob.class);
    /**
     * 特殊job 用于执行数仓的shell脚本
     * @param parameter
     */
    @Override
    public void executeJob(String parameter) {

        try {
            if (StringUtils.isNotBlank(parameter)) {
                JSONObject jsonParam = new JSONObject();
                if (StringUtils.isNotBlank(parameter)) {
                    JSONObject json = JSONObject.parseObject(parameter);
                    jsonParam.putAll(json);
                }
                callShell(jsonParam);
            }
        } catch (Exception e) {
            logger.error("异常原因:", e);
            logger.error("参数异常，parameter={},任务中止", parameter);
            throw new RuntimeException(e);
        }
    }

    void callShell(JSONObject param) throws Exception{
        ShellExcutor shellExcutor=new ShellExcutor();
        String filePath=param.getString("filePath");
        logger.info("脚本路径为filePath："+filePath);
        shellExcutor.callScript(filePath);
    }
}
