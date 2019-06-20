package com;

import java.util.ArrayList;
import java.util.List;

public class task {
    //任务集合(tasks[i]表示第i个任务的长度)
    List<Integer> tasks=new ArrayList<Integer>();
    //任务数量
    int taskNum = 100;

    /** 任务长度取值范围 */
    //int[] taskLengthRange = {10,100};
    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }
    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

}
