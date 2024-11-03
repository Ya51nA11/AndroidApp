package com.example.tasktracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.tasktracker.databinding.ActivityMainBinding;
import com.example.tasktracker.databinding.DialogAddTaskBinding;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;

public class MainActivity extends AppCompatActivity
{
    ActivityMainBinding binding;
    Calendar selectedDate = Calendar.getInstance();
    Calendar taskDate = null;
    TaskAdapter adapter;
    List<Task> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.tvDate.setText(Utils.getFormattedDate(selectedDate));
        setOnClickListeners();
        binding.lvTasks.setAdapter(adapter);
        setListView();
    }

    private void setListView()
    {
        taskList = Utils.getTasks(this, Utils.getFormattedDateWithMillis(selectedDate.getTimeInMillis()));
        adapter = new TaskAdapter(this, taskList);
        binding.lvTasks.setAdapter(adapter);

        if (taskList.isEmpty())
        {
            binding.tvNoTasks.setVisibility(View.VISIBLE);
            binding.lvTasks.setVisibility(View.GONE);
        } else
        {
            binding.tvNoTasks.setVisibility(View.GONE);
            binding.lvTasks.setVisibility(View.VISIBLE);
        }
    }


    private void setOnClickListeners()
    {
        binding.btnAdd.setOnClickListener(v -> showTaskDialog(null));

        binding.btnCalendar.setOnClickListener(v -> {

            DatePickerDialog dialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        binding.tvDate.setText(Utils.getFormattedDate(selectedDate));
                        setListView();
                    },
                    selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)
            );

            dialog.show();
        });


        binding.lvTasks.setOnItemClickListener((parent, view, position, id) -> {
            // Create a PopupMenu
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);

            // Inflate the menu resource
            popupMenu.getMenuInflater().inflate(R.menu.task_menu, popupMenu.getMenu());

            // Set an OnMenuItemClickListener to handle menu item clicks
            popupMenu.setOnMenuItemClickListener(item -> {

                if (item.getItemId() == R.id.edit)
                {
                    showTaskDialog(taskList.get(position));
                } else if (item.getItemId() == R.id.delete)
                {
                    showDeletionDialog(taskList.get(position));
                }

                return true;
            });

            popupMenu.show();
        });

    }

    private void showDeletionDialog(Task task)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Do you really want to delete this task?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            Utils.deleteTask(MainActivity.this, task);
            setListView();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void showTaskDialog(Task task)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogAddTaskBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater
                .from(MainActivity.this), R.layout.dialog_add_task, null, false);


        builder.setView(dialogBinding.getRoot());

        AlertDialog dialog = builder.create();

        if (task != null)
        {
            dialogBinding.etName.setText(task.getTaskName());
            dialogBinding.tvDate.setText(Utils.getFormattedDateWithMillis(task.getTaskDate()));
            taskDate = Calendar.getInstance();
            taskDate.setTimeInMillis(task.getTaskDate());
        }

        dialogBinding.rlDate.setOnClickListener(v -> {
            DatePickerDialog dialog1 = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        if (taskDate == null)
                        {
                            taskDate = Calendar.getInstance();
                        }
                        taskDate.set(Calendar.YEAR, year);
                        taskDate.set(Calendar.MONTH, month);
                        taskDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        dialogBinding.tvDate.setText(Utils.getFormattedDate(taskDate));

                    },
                    selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH)
            );

            dialog1.show();
        });

        dialogBinding.btnAddTask.setOnClickListener(v -> {
            String taskName = Objects.requireNonNull(dialogBinding.etName.getText()).toString();

            if (taskName.isEmpty())
            {
                Toast.makeText(MainActivity.this, "Please enter task name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (taskDate == null)
            {
                Toast.makeText(MainActivity.this, "Please select task date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (task == null)
            {

                Task task1 = new Task(
                        UUID.randomUUID().toString(),
                        taskName,
                        taskDate.getTimeInMillis(),
                        false
                );

                Utils.saveTask(MainActivity.this, task1);
            } else
            {
                task.setTaskName(taskName);
                task.setTaskDate(taskDate.getTimeInMillis());

                Utils.updateTask(MainActivity.this, task);
            }


            setListView();

            dialog.dismiss();

        });

        dialog.show();
    }


}