package org.mifos.pheeBillPay.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@ComponentScan(basePackages = "org.mifos.pheeBillPay")
public abstract class AbstractApplicationConfiguration {
    @Value("${async.core-pool-size}")
    public Integer corePoolSize;
    @Value("${async.max-pool-size}")
    public Integer maxPoolSize;
    @Value("${async.queue-capacity}")
    public Integer queueCapacity;
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }
}
