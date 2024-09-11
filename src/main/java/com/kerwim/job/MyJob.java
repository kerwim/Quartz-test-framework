package com.kerwim.job;


import lombok.extern.slf4j.Slf4j;
import org.quartz.*;


@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
public class MyJob implements Job {

    /**
     * 执行任务
     * @param context 表示定时任务执行的环境、也有很多资料叫上下文
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();

        log.info("任务名称:{}",jobDetail.getKey().getName());
        log.info("任务分组名称:{}",jobDetail.getKey().getGroup());
        log.info("任务类:{}",jobDetail.getJobClass().getName());
        log.info("本次执行时间:{}",context.getFireTime());
        log.info("下次执行时间:{}",context.getNextFireTime());

        //记录任务次数
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Integer cunout = (Integer) jobDataMap.get("count");
        log.info("执行次数:{}",cunout);
        jobDataMap.put("count",++cunout);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
