package com.example.tasktracker;

import java.util.HashMap;
import java.util.List;

public class SavedTask
{
    private HashMap<String, List<Task>> tasks;


    public HashMap<String, List<Task>> getTasks()
    {
        return tasks;
    }

    public void setTasks(HashMap<String, List<Task>> tasks)
    {
        this.tasks = tasks;
    }

    public void setComplete()
    {

    }
}
