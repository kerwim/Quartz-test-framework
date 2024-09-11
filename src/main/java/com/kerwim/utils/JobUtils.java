package com.kerwim.utils;


import com.kerwim.entity.JobBean;
import org.quartz.*;

public class JobUtils {

    /**
     * 创建任务
     *
     * 该方法负责根据提供的调度器和任务信息来创建一个任务。它解释了为什么需要这些参数以及它们是如何被使用的，
     * 从而帮助开发者理解这个方法的目的和工作方式。
     *
     * @param scheduler 调度器。用于调度任务的执行。它是创建任务所必需的，因为它决定了任务何时何地执行。
     * @param jobBean JobBean。包含任务的具体信息。这是创建任务所需的，因为它定义了任务的实际行为和属性。
     */
    public static void createJob(Scheduler scheduler, JobBean jobBean){
        Class<? extends Job> jobClass = null;
        JobDetail jobDetail = null;
        Trigger trigger = null;
        String cron = "0/2 * * * * ? *";

        try {
            //从jobBean 反射获取 任务类对象
            jobClass = (Class<? extends Job>) Class.forName(jobBean.getJobClass());
            //创建jobDetail
            jobDetail = JobBuilder.newJob(jobClass)
                    .storeDurably()
                    .withIdentity(jobBean.getJobName()) //设置唯一标识
                    .usingJobData("count",1)
                    .build();
            //创建trigger
            trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .withIdentity(jobBean.getJobName() + "_trigger") //设置唯一标识
                    .build();
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 暂停指定的定时任务
     *
     * @param scheduler 调度器实例，用于操作定时任务
     * @param jobName 任务名称，用于标识需要暂停的任务
     */
    public static void pauseJob(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 恢复定时任务
     *
     * 该方法用于恢复一个之前被暂停的定时任务通过Scheduler实例和任务名称来指定特定的定时任务
     *
     * @param scheduler Scheduler实例，用于操作定时任务
     * @param jobName 定时任务的名称，用于标识特定的定时任务
     */
    public static void resumeJob(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除指定的定时任务
     *
     * @param scheduler 非空调度器实例，用于操作定时任务
     * @param jobName   待删除任务的名称
     */
    public static void deleteJob(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用指定的调度器运行工作一次
     *
     * 此方法旨在触发一次性工作，通过将工作实例添加到调度器中来执行
     * 它提供了一种机制，可以用于按需运行定期任务或响应特定事件
     *
     * @param scheduler 调度器实例，用于管理和触发工作
     * @param jobName 工作的名称，用于标识要运行的工作
     */
    public static void runJobOnce(Scheduler scheduler, String jobName){
        JobKey jobKey = JobKey.jobKey(jobName);
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新任务的调度配置
     *
     * 该方法允许在不重启任务的情况下，修改任务的调度策略或参数
     * 主要用于在运行时对任务进行灵活调整，比如改变任务的执行频率或执行不同的任务逻辑
     *
     * @param scheduler 调度器实例，用于执行更新操作
     * @param jobBean 包含任务调度信息的bean，包含任务的标识和新的调度配置
     */
    public static void updateJob(Scheduler scheduler, JobBean jobBean){
        //获取任务触发器唯一标识
        TriggerKey triggerKey = TriggerKey.triggerKey(jobBean.getJobName() + "_trigger");
        try {
            //通过唯一标识获取旧的触发器对象
            CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            //获取新的 cron 表达式
            String cronExpression = jobBean.getCronExpression();
            //创建新的触发器对象 并设置新的表达式
            CronTrigger newTrigger = oldTrigger.getTriggerBuilder()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                    .build();
            //调度器更新任务的触发器
            scheduler.rescheduleJob(triggerKey,newTrigger);
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

}
