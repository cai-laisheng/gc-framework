package com.allen.thread.container;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author xuguocai
 * @date 2022/9/1 14:17
 */
public class DelayEntity implements Delayed {

    private static final Long currentTime = System.currentTimeMillis();
    private String str;
    private Long scheduleTime;

    public DelayEntity(String str, Long scheduleTime) {
        this.str = str;
        this.scheduleTime = System.currentTimeMillis() + 10000 * scheduleTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(scheduleTime -System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int)( this.scheduleTime - ((DelayEntity) o).scheduleTime);
    }

    public String getStr() {
        return str;
    }

    public Long getScheduleTime() {
        return scheduleTime;
    }

    public String showScheduleTime() {
        return "计划执行时间:" + new Date(this.scheduleTime);
    }
}
