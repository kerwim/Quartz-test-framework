package com.kerwim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobBean {

    /**
     * 任务名字
     */
    private String jobName;

    /**
     * 任务类
     */
    private String jobClass;

    /**
     * cron表达式
     */
    private String cronExpression;
}
