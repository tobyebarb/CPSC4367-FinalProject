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

import com.ualr.todohub.R;
import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;

import java.util.List;

public class SettingsDialogFragment extends DialogFragment {

    private static final String TAG = SettingsDialogFragment.class.getSimpleName();
    private Button uncompletedBtn;
    private Button completedBtn;
    private Button allBtn;
    private static TaskViewModel viewModel;
    private TaskListFragment listFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        viewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                listFragment = (TaskListFragment) getFragmentManager().findFragmentByTag("TaskListFragment");
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

        builder.setTitle(R.string.settings_action_prompt)
                .setItems(R.array.sorting_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listFragment.changeSelector(which, viewModel.getTaskList().getValue());
                    }
                });
        return builder.create();
    }

}