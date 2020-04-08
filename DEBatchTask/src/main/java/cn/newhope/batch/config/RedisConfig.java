package cn.newhope.batch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@ConfigurationProperties(prefix = "spring.redis")
  public class RedisConfig {

    public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Integer getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public Integer getMaxTotal() {
		return maxTotal;
	}
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	public Integer getMaxWaitMillis() {
		return maxWaitMillis;
	}
	public void setMaxWaitMillis(Integer maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}
	public Integer getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}
	public void setMinEvictableIdleTimeMillis(Integer minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}
	public Integer getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}
	public void setNumTestsPerEvictionRun(Integer numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}
	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}
	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}
	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}
	public String getClusterNodes() {
		return clusterNodes;
	}
	public void setClusterNodes(String clusterNodes) {
		this.clusterNodes = clusterNodes;
	}
	public Integer getMmaxRedirectsac() {
		return mmaxRedirectsac;
	}
	public void setMmaxRedirectsac(Integer mmaxRedirectsac) {
		this.mmaxRedirectsac = mmaxRedirectsac;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	private String hostname;

    private Integer maxIdle;

    private Integer port;

    private Integer maxTotal;

    private Integer maxWaitMillis;

    private Integer minEvictableIdleTimeMillis;

    private Integer numTestsPerEvictionRun;

    private long timeBetweenEvictionRunsMillis;

    private boolean testOnBorrow;

    private boolean testWhileIdle;

    private String clusterNodes;

    private Integer mmaxRedirectsac;

    private int database;

      /**
       * JedisPoolConfig 连接池
       * @return
       */
      @Bean
      public JedisPoolConfig jedisPoolConfig() {
          JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
          // 最大空闲数
          jedisPoolConfig.setMaxIdle(maxIdle);
          // 连接池的最大数据库连接数
          jedisPoolConfig.setMaxTotal(maxTotal);
          // 最大建立连接等待时间
          jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
          // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
          jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
          // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
          jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
          // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
          jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
          // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
          jedisPoolConfig.setTestOnBorrow(testOnBorrow);
          // 在空闲时检查有效性, 默认false
          jedisPoolConfig.setTestWhileIdle(testWhileIdle);
          return jedisPoolConfig;
      }
      /**
       * 单机版配置
      * @Title: JedisConnectionFactory 
      * @param @param jedisPoolConfig
      * @param @return
      * @return JedisConnectionFactory
      * @autor lpl
      * @date 2018年2月24日
      * @throws
       */
      @Bean
      public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
		JedisConnectionFactory JedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		// 连接池
		JedisConnectionFactory.setPoolConfig(jedisPoolConfig);
		// IP地址
		JedisConnectionFactory.setHostName(hostname);
		// 端口号
		JedisConnectionFactory.setPort(6379);
		// 如果Redis设置有密码
		// JedisConnectionFactory.setPassword(password);
		// 客户端超时时间单位是毫秒
		JedisConnectionFactory.setTimeout(5000);
		JedisConnectionFactory.setDatabase(database);
		return JedisConnectionFactory;
	}


	/**
	 * 设置数据存入 redis 的序列化方式,并开启事务
	 *
	 */
	@Bean
	private RedisTemplate<String, Object> initDomainRedisTemplate(JedisConnectionFactory JedisConnectionFactory) {
		// 如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to
		// String！
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		// 开启事务
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setConnectionFactory(JedisConnectionFactory);
		return redisTemplate;
	}



    @Bean
    public JedisPool getJedisPool(JedisPoolConfig jedisPoolConfig) {
        try {
           // JedisPool jedisPool = new JedisPool(jedisPoolConfig,hostname,port,10000);//有密码的时候传入AUTH
			JedisPool jedisPool = new JedisPool(jedisPoolConfig,hostname,port,10000,null,database,null);//有密码的时候传入AUTH
			return jedisPool;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

  
  }
 