
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ualr.todohub.MainActivity;
import com.ualr.todohub.R;
import com.ualr.todohub.adapter.Adapter;
import com.ualr.todohub.adapter.subtaskAdapter;
import com.ualr.todohub.database.DataBaseHelper;
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
    private ImageView deleteBtn;

    public static int selector = 0; //CHANGE THIS LATER
    public static Task parentTask;
    public static int position;
    public DataBaseHelper dataBaseHelper;
    private AlertDialog dialog;

    private static final String TAG = TaskDialogFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG = "TaskListFragment";
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsDialogFragment";
    private static final String DATEPICKER_FRAGMENT_TAG = "DatePickerDialogFragment";
    private static final String NEW_TASK_FRAGMENT_TAG = "NewTaskDialogFragment";


    public TaskDialogFragment(int position, Task parentTask) {
        this.position = position;
        this.parentTask = parentTask;
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
                updateItems(tasks);
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.task_dialog_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));

        descTV = (TextView) dialoglayout.findViewById(R.id.task_desc_expanded);
        dueDateTV = (TextView) dialoglayout.findViewById(R.id.task_due_date_expanded);
        subtaskBtn = (Button) dialoglayout.findViewById(R.id.add_subtask_btn);
        deleteBtn = (ImageView) dialoglayout.findViewById(R.id.task_delete_btn);

        descTV.setText(parentTask.getDescription());
        dueDateTV.setText(parentTask.getDueDateString());

        subtaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onSubtaskBtn();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Deleting task").setMessage("Are you sure you want to delete this task, along with all subtasks?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDeleteBtn();
                                Toast.makeText(mContext, "Task(s) deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

        int index = viewModel.getSelectedIndex();
        parentTask = viewModel.getTaskList().getValue().get(index);
        dataBaseHelper = new DataBaseHelper(mContext);

        mRecyclerView = (RecyclerView) dialoglayout.findViewById(R.id.subtask_recyclerView);
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new subtaskAdapter(mContext, viewModel.getTaskList().getValue(), parentTask);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListenerSub(new subtaskAdapter.OnItemClickListenerSub() {
            public void onItemClick(View v, Task obj, int position) {
                viewModel.setSelectedIndex(getIndex());
            }
        });

        builder.setTitle(viewModel.getTaskList().getValue().get(index).getTitle())
                .setView(dialoglayout)
                .setPositiveButton(R.string.positive_btn, null)
                .setNegativeButton(R.string.new_task_prompt_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
        return dialog;
    }

    public void onSubtaskBtn(){
        TaskListFragment listFragment = (TaskListFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        Log.d(TAG, "PARENT ID BEING INPUTTED AS "+String.valueOf(parentTask.getId()));
        listFragment.showNewSubtaskDialog(parentTask.getId());
    }

    public int deleteSubtasks(int parentID, List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            if(tasks.get(i).getParentID() == parentID) {
                dataBaseHelper.deleteItem(tasks.get(i));
                return deleteSubtasks(tasks.get(i).getId(), tasks);
            }
        }
        return 0;
    }

    public void onDeleteBtn() {
        List<Task> allTasks = dataBaseHelper.getAll();
        /*for (int i = 0; i < allTasks.size(); i++) {
            if(allTasks.get(i).getParentID() == parentTask.getId()) {
                Log.d(TAG, "DELETED TASK: " + allTasks.get(i).getTitle());
                dataBaseHelper.deleteItem(allTasks.get(i)); //DELETES ALL SUBTASKS UNDER THE DELETED TASK
            }
        }*/
        deleteSubtasks(parentTask.getId(), allTasks);
        dataBaseHelper.deleteItem(parentTask);
        allTasks = dataBaseHelper.getAll();
        viewModel.setTaskList(allTasks); // may need to update before this?
        dialog.dismiss();
    }
}
