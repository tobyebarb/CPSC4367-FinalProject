package com.ualr.todohub.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ualr.todohub.R;
import com.ualr.todohub.adapter.Adapter;
import com.ualr.todohub.adapter.subtaskAdapter;
import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;
import com.ualr.todohub.utils.DataGenerator;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskDialogFragment extends DialogFragment {

    public static Context ctx;

    private RecyclerView mRecyclerView;
    private subtaskAdapter mAdapter;
    public Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private static TaskViewModel viewModel;

    private TextView titleTV;
    private TextView descTV;
    private TextView dueDateTV;
    private Button subtaskBtn;

    public static int selector = 0; //CHANGE THIS LATER
    public static Task parentTask;
    public static int position;

    private static final String TAG = TaskDialogFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG = "TaskListFragment";
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsDialogFragment";
    private static final String DATEPICKER_FRAGMENT_TAG = "DatePickerDialogFragment";
    private static final String NEW_TASK_FRAGMENT_TAG = "NewTaskDialogFragment";

    public TaskDialogFragment(int position) {
        this.position = position;
    }

    public TaskDialogFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public void updateItems(List<Task> tasks){
        mAdapter.updateItems(tasks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_dialog_fragment, container, false);

        return inflater.inflate(R.layout.task_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.subtask_recyclerView);
        /*

        VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

         */
        //List<Task> items = DataGenerator.getTaskData(mContext); // THIS MAY CAUSE PROBLEMS WITH THE EXISTING TASKLIST
        /*

        ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

         */
        //viewModel.setTaskList(items); //setting the model up with items
        final Context ctx = mContext;
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new subtaskAdapter(mContext, viewModel.getTaskList().getValue(), parentTask);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new subtaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Task obj, int position) {
                viewModel.setSelectedIndex(getIndex());
            }
        });
    }

    public int getIndex(){
        return mAdapter.getIndex();
    }

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
        View dialoglayout = inflater.inflate(R.layout.task_dialog_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

        descTV = (TextView) dialoglayout.findViewById(R.id.task_desc_expanded);
        dueDateTV = (TextView) dialoglayout.findViewById(R.id.task_due_date_expanded);
        subtaskBtn = (Button) dialoglayout.findViewById(R.id.add_subtask_btn);
        mRecyclerView = (RecyclerView) dialoglayout.findViewById(R.id.subtask_recyclerView);
//VVV
        /*final Context ctx = mContext;
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new subtaskAdapter(mContext, viewModel.getTaskList().getValue(), parentTask);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new subtaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Task obj, int position) {
                viewModel.setSelectedIndex(getIndex());
            }
        });*///^^^^

        int index = viewModel.getSelectedIndex();
        parentTask = viewModel.getTaskList().getValue().get(index);

        descTV.setText(viewModel.getTaskList().getValue().get(index).getDescription());
        dueDateTV.setText(viewModel.getTaskList().getValue().get(index).getDueDateString());

        builder.setView(dialoglayout);

        builder.setTitle(viewModel.getTaskList().getValue().get(index).getTitle())
                .setPositiveButton(R.string.positive_btn, null)
                .setNegativeButton(R.string.new_task_prompt_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        subtaskBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSubtaskBtn();
            }
        });

        return builder.create();
    }

    public void onSubtaskBtn() {
        TaskListFragment listFragment = (TaskListFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        Log.d(TAG, String.valueOf(parentTask.getId()));
        listFragment.showNewSubtaskDialog(parentTask.getId());
    }

    public void toggleCompleted(int position) {
        viewModel.toggleItem(position);
    }
}
