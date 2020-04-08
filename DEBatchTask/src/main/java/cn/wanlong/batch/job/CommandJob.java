package cn.wanlong.batch.job;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/********
 * 
 * @ClassName: UploadFileJob
 * @Description: sftp上传文件
 * @author LTC
 * @date 20190927
 *
 */
@Component
public class CommandJob extends AbstractJob {
	private static Logger logger = LoggerFactory.getLogger(CommandJob.class);


	@Override
	public void executeJob(String parameter) {
		try {
			if (StringUtils.isNotBlank(parameter)) {
				ExcuteCmd(parameter);
			}
		} catch (Exception e) {
			logger.error("异常原因:", e);
			logger.error("参数异常，parameter={},上传任务中止", parameter);
			return;
		}
	}

	public static void ExcuteCmd(String cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int exitValue = process.waitFor();
			if (0 != exitValue) {
				logger.error("call shell failed. error code is :" + exitValue);
			}
		} catch (Throwable e) {
			logger.error("call shell failed. " + e);
		}
	}

}
