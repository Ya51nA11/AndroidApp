package com.example.tasktracker;

import com.google.gson.annotations.SerializedName;

public class Task
{
    private String taskId;
    private String taskName;
    private long taskDate;
    @SerializedName("complete")
    private boolean complete;

    public Task(String taskId, String taskName, long taskDate, boolean isComplete)
    {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.complete = isComplete;
    }

    public boolean isComplete()
    {
        return complete;
    }

    public void setComplete(boolean isComplete)
    {
        complete = isComplete;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public void setTaskName(String taskName)
    {
        this.taskName = taskName;
    }

    public long getTaskDate()
    {
        return taskDate;
    }

    public void setTaskDate(long taskDate)
    {
        this.taskDate = taskDate;
    }
}
