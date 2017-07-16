package org.qianrenxi.core.schedule.quartz.config;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.qianrenxi.core.schedule.quartz.QuartJobSchedulingListener;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by david on 2015-01-20.
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
public class QuartzConfiguration {
	

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext)
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public QuartJobSchedulingListener quartJobSchedulingListener(){
        return new QuartJobSchedulingListener();
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setAutoStartup(true);
		factory.setDataSource(dataSource);
		factory.setJobFactory(jobFactory);
		factory.setQuartzProperties(quartzProperties());

		return factory;
    }
    
    @Bean
	public Properties quartzProperties() throws IOException {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		propertiesFactoryBean.afterPropertiesSet();
		return propertiesFactoryBean.getObject();
	}
    
}