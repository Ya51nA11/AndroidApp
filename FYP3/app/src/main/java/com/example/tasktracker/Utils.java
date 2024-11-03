package com.example.tasktracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Utils
{
    public static void saveTask(Context context, Task task)
    {
        String data = SharedPreferencesManager.getInstance(context)
                .getString("tasks", "");


        List<Task> tasks;
        SavedTask savedTasks = new SavedTask();
        HashMap<String, List<Task>> taskMap;

        if (data.isEmpty())
        {
            taskMap = new HashMap<>();
            tasks = new ArrayList<>();
        } else
        {
            savedTasks = (new Gson().fromJson(data, SavedTask.class));
            taskMap = savedTasks.getTasks();
            tasks = taskMap.get(getFormattedDateWithMillis(task.getTaskDate()));
        }

        if(tasks != null)
        {
            tasks.add(task);
        }
        else
        {
            tasks = new ArrayList<>();
            tasks.add(task);
        }

        taskMap.put(getFormattedDateWithMillis(task.getTaskDate()), tasks);
        savedTasks.setTasks(taskMap);

        String jsonData = new Gson().toJsonTree(savedTasks).toString();

        SharedPreferencesManager.getInstance(context)
                .putString("tasks", jsonData);

        Log.d("MLK", SharedPreferencesManager.getInstance(context)
                .getString("tasks", ""));

        Toast.makeText(context, "Task Saved!", Toast.LENGTH_SHORT).show();


    }

    public static void updateTask(Context context, Task task)
    {
        String data = SharedPreferencesManager.getInstance(context)
                .getString("tasks", "");
        List<Task> tasks;
        SavedTask savedTasks = new SavedTask();
        HashMap<String, List<Task>> taskMap;


        savedTasks = (new Gson().fromJson(data, SavedTask.class));
        taskMap = savedTasks.getTasks();
        tasks = taskMap.get(getFormattedDateWithMillis(task.getTaskDate()));


        if(tasks != null)
        {
            tasks = findAndReplaceTask(tasks, task);
            taskMap.put(getFormattedDateWithMillis(task.getTaskDate()), tasks);
            savedTasks.setTasks(taskMap);

            String jsonData = new Gson().toJsonTree(savedTasks).toString();

            SharedPreferencesManager.getInstance(context)
                    .putString("tasks", jsonData);

            Log.d("MLK", SharedPreferencesManager.getInstance(context)
                    .getString("tasks", ""));

            Toast.makeText(context, "Task Updated!", Toast.LENGTH_SHORT).show();
        }

    }


    public static void deleteTask(Context context, Task task)
    {
        String data = SharedPreferencesManager.getInstance(context)
                .getString("tasks", "");
        List<Task> tasks;
        SavedTask savedTasks = new SavedTask();
        HashMap<String, List<Task>> taskMap;


        savedTasks = (new Gson().fromJson(data, SavedTask.class));
        taskMap = savedTasks.getTasks();
        tasks = taskMap.get(getFormattedDateWithMillis(task.getTaskDate()));


        if(tasks != null)
        {
            tasks = findAndDeleteTask(tasks, task);
            taskMap.put(getFormattedDateWithMillis(task.getTaskDate()), tasks);
            savedTasks.setTasks(taskMap);

            String jsonData = new Gson().toJsonTree(savedTasks).toString();

            SharedPreferencesManager.getInstance(context)
                    .putString("tasks", jsonData);

            Log.d("MLK", SharedPreferencesManager.getInstance(context)
                    .getString("tasks", ""));

            Toast.makeText(context, "Task Deleted!", Toast.LENGTH_SHORT).show();
        }

    }

    private static List<Task> findAndReplaceTask(List<Task> tasks, Task task)
    {
        for(int i=0; i < tasks.size(); i++)
        {
            if(task.getTaskId().equals(tasks.get(i).getTaskId()))
            {
                tasks.set(i, task);
            }
        }

        return tasks;
    }

    private static List<Task> findAndDeleteTask(List<Task> tasks, Task task)
    {
        for(int i=0; i < tasks.size(); i++)
        {
            if(task.getTaskId().equals(tasks.get(i).getTaskId()))
            {
                tasks.remove(i);
                break;
            }
        }

        return tasks;
    }

    public static List<Task> getTasks(Context context, String date)
    {
        String data = SharedPreferencesManager.getInstance(context)
                .getString("tasks", "");

        Log.d("MLK", "NEW: " + data);

        List<Task> tasks;
        SavedTask savedTasks = new SavedTask();

        if (data.isEmpty())
        {
            tasks = new ArrayList<>();
        } else
        {
            savedTasks = (new Gson().fromJson(data, SavedTask.class));
            tasks = savedTasks.getTasks().get(date);
        }

        if(tasks == null)
        {
            return new ArrayList<>();
        }
        else
        {
            return tasks;
        }
    }

    public static String getFormattedDate(Calendar selectedDate)
    {
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(selectedDate.getTime());
    }

    public static String getFormattedDateWithMillis(long timeInMillis)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(calendar.getTime());
    }
}
