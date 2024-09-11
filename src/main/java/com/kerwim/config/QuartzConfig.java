package com.kerwim.config;

import com.kerwim.job.MyJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;


//@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail jobDetail(){
        return JobBuilder.newJob(MyJob.class) //detail 绑定 Job
                .storeDurably() // 持久化
                .withIdentity("job1","group1") //唯一标识
                .usingJobData("cunout",1) //(共享)数据初始化
                .build();
    }

    @Bean
    public Trigger trigger(){
        String croExpression = "0/2 * * * * ? *"; // 每隔 2s 执行一次
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail())
                .withIdentity("trigger1","group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(croExpression))
                .build();
    }
}
