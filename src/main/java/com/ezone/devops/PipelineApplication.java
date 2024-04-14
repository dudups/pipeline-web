package com.ezone.devops;

import com.ezone.galaxy.framework.mq.annotation.EnableRocketMQ;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@EnableRocketMQ
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {"com.ezone.galaxy", "com.ezone.ezbase", "com.ezone.devops"})
public class PipelineApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(PipelineApplication.class, args);
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfig() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:/ftl/");
        configurer.setDefaultEncoding("UTF-8");
        return configurer;
    }

}
