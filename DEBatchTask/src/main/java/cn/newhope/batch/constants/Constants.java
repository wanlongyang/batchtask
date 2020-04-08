package cn.newhope.batch.constants;

/**
 * 对外实时接口常量类
 * 
 */
public class Constants {
	public static String ROOT_PATH = "";// 项目根目录
	public static String MSG_TEMPLATE_PATH = "/msgTemplate";// 数据解析入库配置文件路径
	
    //数据解析入库状态
	public static String EtlStatus_LOCK = "1";//锁定
	public static String EtlStatus_SUCCESS = "2";//成功
	
	//批量任务时间范围参数
	public static String START_DATE = "startDate";//开始日期
	public static String END_DATE = "endDate";//结束日期
	
	//产品代码
	public static String GF_RULE = "GfRule";//股份决策
	public static String GF_SALES = "GfSales";//股份业务员

	// 特殊任务参数
	public static final String PARAM_START_DATE = "startDate";// 开始日期
	public static final String PARAM_END_DATE = "endDate";// 结束日期
	public static final String PARAM_OUT_DATE = "outDate";// 输出文件日期
	public static final String PARAM_DELETE_DAYS = "deleteDays";// 删除天数
	public static final String PARAM_TABLES = "tables";// 表名
	public static final String PARAM_END_TIME = " 00:00:00";// 时间格式
	public static final String PARAM_ALL_TABLES = "ALL";// 所有表
	public static final String PARAM_METHOD = "method";// 方法
	public static final String PARAM_TEST = "test";// 测试用
	public static final String PARAM_MSG_CODE = "msgCode";// msgcode
	public static final String PARAM_REPLACE_MSG_CODE = "replaceMsgCode";// 替换的msgcode


}