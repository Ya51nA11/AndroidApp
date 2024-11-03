package com.example.tasktracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.tasktracker.databinding.LiTaskBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class TaskAdapter extends ArrayAdapter<Task>
{
    List<Task> taskList;
    private final Context context;
    boolean calledManually;


    public TaskAdapter(@NonNull Context context, List<Task> taskList)
    {
        super(context, R.layout.li_task, taskList);
        this.taskList = taskList;
        this.context = context;
    }

    public void setTaskList(List<Task> taskList)
    {
        this.taskList = taskList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            LiTaskBinding binding = DataBindingUtil.inflate(inflater, R.layout.li_task, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }

        LiTaskBinding binding = (LiTaskBinding) convertView.getTag();

        binding.tvTask.setText(taskList.get(position).getTaskName());
        int status = SharedPreferencesManager.getInstance(context).getInt(taskList.get(position).getTaskId(), 0);
        binding.cbTask.setChecked(status == 1);

        binding.cbTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean isChecked = SharedPreferencesManager.getInstance(context).getInt(taskList.get(position).getTaskId(), 0) == 1;
                isChecked = !isChecked;
                SharedPreferencesManager.getInstance(context)
                        .putInt(taskList.get(position).getTaskId(), isChecked ? 1 : 0);
            }
        });

        // binding.cbTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        // {
        //     @Override
        //     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        //     {
        //         if(calledManually)
        //         {
        //             calledManually = false;
        //             return;
        //         }
        //
        //     }
        // });



        return convertView;
    }
}
