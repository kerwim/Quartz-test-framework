package com.kerwim.controller;

import com.kerwim.entity.JobBean;
import com.kerwim.job.MyJob;
import com.kerwim.utils.JobUtils;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quartz")
public class QuartzController {


    @Autowired
    Scheduler scheduled;

    private String jobName = "myJob";

    @GetMapping("/create")
    public String createJob() {
        JobBean jobBean = new JobBean(jobName, MyJob.class.getName(), "0/3 * * * * ?");
        JobUtils.createJob(scheduled,jobBean);
        return "任务创建成功";
    }

    @GetMapping("/pause")
    public String pauseJob() {
        JobUtils.pauseJob(scheduled,jobName);
        return "任务暂停成功";
    }

    @GetMapping("/resume")
    public String resumeJob() {
        JobUtils.resumeJob(scheduled,jobName);
        return "任务恢复成功";
    }

    @GetMapping("/delete")
    public String deleteJob() {
        JobUtils.deleteJob(scheduled,jobName);
        return "任务删除成功";
    }

    @GetMapping("/once")
    public String runOnceJob() {
        JobUtils.runJobOnce(scheduled,jobName);
        return "任务执行一次成功";
    }

    @GetMapping("/modify")
    public String modifyJob() {
        JobBean jobBean = new JobBean(jobName, MyJob.class.getName(), "0/20 * * * * ?");
        JobUtils.updateJob(scheduled,jobBean);
        return "任务修改成功";
    }

}
