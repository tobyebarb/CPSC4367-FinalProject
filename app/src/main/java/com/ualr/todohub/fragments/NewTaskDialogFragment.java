package com.ualr.todohub.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;
import com.ualr.todohub.R;

import java.util.Calendar;
import java.util.List;

public class NewTaskDialogFragment extends DialogFragment {

    private static final String TAG = NewTaskDialogFragment.class.getSimpleName();
    private EditText titleET;
    private EditText descET;
    private Button dueDateBtn;
    private Calendar calendar;
    private static TaskViewModel viewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        viewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                TaskListFragment listFragment = (TaskListFragment) getFragmentManager().findFragmentByTag("TaskListFragment");
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.new_task_dialog_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

        titleET = (EditText) dialoglayout.findViewById(R.id.new_task_prompt_title_ET);
        descET = (EditText) dialoglayout.findViewById(R.id.new_task_prompt_desc_ET);
        dueDateBtn = (Button) dialoglayout.findViewById(R.id.new_task_prompt_due_date_btn);



        builder.setView(dialoglayout);

        builder.setTitle(R.string.new_task_prompt_main)
                .setPositiveButton(R.string.new_task_prompt_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskListFragment listFragment = (TaskListFragment) getFragmentManager().findFragmentByTag("TaskListFragment");
                        listFragment.createTask(titleET.getText().toString(), descET.getText().toString(), calendar);
                    }
                })
                .setNegativeButton(R.string.new_task_prompt_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Button myDialogButton = (Button)dialoglayout.findViewById(R.id.new_task_prompt_due_date_btn);
        myDialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onDueDateBtnClicked();
            }
        });

        return builder.create();
    }

    public void storeDueDate(Calendar c) {
        if(c==null) this.calendar.set(1990,9,9);
        else this.calendar = c;
    }

    public void onDueDateBtnClicked() {
        DialogFragment newFragment = new DatePickerDialogFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}
