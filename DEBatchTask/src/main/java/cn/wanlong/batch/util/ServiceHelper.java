package cn.wanlong.batch.util;

import redis.clients.jedis.JedisPool;

/**
 * @author Administrator 获取Spring容器中的service bean
 */
public final class ServiceHelper {

	public static <T> T getService(Class<T> t) {
		return ApplicationContextUtils.getBean(t);
	}

	public static Object getService(String serviceName) {
		return ApplicationContextUtils.getBean(serviceName);
	}

	public static JedisPool getJedisPool() {
		return (JedisPool) getService("getJedisPool");
	}
}
