package com.devteamvietnam.framework.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid configuration properties
 *
 * @author ivan
 */
@Configuration
public class DruidProperties
{
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    public DruidDataSource dataSource(DruidDataSource datasource)
    {
        /** Configure initial size, minimum, maximum */
        datasource.setInitialSize(initialSize);
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /** Configure the timeout time for obtaining the connection waiting */
        datasource.setMaxWait(maxWait);

        /** Configure how long the interval is to perform detection, and detect idle connections that need to be closed, in milliseconds */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /** Configure the minimum and maximum survival time of a connection in the pool, in milliseconds */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);

        /**
         * SQL used to check whether the connection is valid, the requirement is a query statement, usually select'x'. If validationQuery is null, testOnBorrow, testOnReturn, testWhileIdle will not work.
         */
        datasource.setValidationQuery(validationQuery);
        /** It is recommended to configure to true, which will not affect performance and ensure safety. Check when applying for a connection. If the idle time is greater than timeBetweenEvictionRunsMillis, execute a validationQuery to check whether the connection is valid. */
        datasource.setTestWhileIdle(testWhileIdle);
        /** Execute validationQuery to check whether the connection is valid when applying for a connection. This configuration will reduce performance. */
        datasource.setTestOnBorrow(testOnBorrow);
        /** Execute validationQuery to check whether the connection is valid when returning the connection. This configuration will reduce performance. */
        datasource.setTestOnReturn(testOnReturn);
        return datasource;
    }
}
