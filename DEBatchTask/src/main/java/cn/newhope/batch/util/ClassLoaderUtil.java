package cn.newhope.batch.util;


import java.lang.reflect.Constructor;

public class ClassLoaderUtil {
	
	/**
	 * 根据类名, 参数数据类型和参数值生成类的实例.
	 * @param clzzName 类名(包括包名)
	 * @param params 参数数据类型的Class数组, 当构造函数无参数时为null
	 * @param pvalues 参数值的Object数组, 当构造函数无参数时为null
	 * @return 返回类的实例
	 */
	public static Object load(String clzzName, Class<?>[] params, Object[] pvalues) {
		try {
			Class<?> clazz = Class.forName(clzzName);
			
			if (params == null) {
				return clazz.newInstance();
			}
			Constructor<?> constructor = clazz.getConstructor(params);
			Object obj = constructor.newInstance(pvalues);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
}
