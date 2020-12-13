package com.ualr.todohub.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import com.ualr.todohub.db.TaskContract;
import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;
import com.ualr.todohub.utils.DataGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskListFragment extends Fragment {

    public static Context ctx;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    public Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton mFAB;
    private static TaskViewModel viewModel;

    public static int selector = 0; //CHANGE THIS LATER

    private static final String TAG = TaskListFragment.class.getSimpleName();
    private static final String FRAGMENT_TAG = "TaskListFragment";
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsDialogFragment";
    private static final String DATEPICKER_FRAGMENT_TAG = "DatePickerDialogFragment";
    private static final String NEW_TASK_FRAGMENT_TAG = "NewTaskDialogFragment";

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(TaskViewModel.class);

        viewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks);
            }
        });
    }

    public void updateItems(List<Task> tasks){
        mAdapter.updateItems(tasks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.task_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        //List<Task> items = DataGenerator.getTaskData(mContext);
        List<Task> items = new ArrayList<>();
        viewModel.setTaskList(items); //setting the model up with items
        final Context ctx = mContext;
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(mContext, viewModel.getTaskList().getValue());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Task obj, int position) {
                viewModel.setSelectedIndex(getIndex());
                Log.d(TAG, String.valueOf(viewModel.getSelectedIndex()));
            }
        });
    }

    public int getIndex(){
        return mAdapter.getIndex();
    }

    public void createTask(String title, String desc, Calendar cal) {
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, title);
        values.put(TaskContract.TaskEntry.COL_TASK_DESC, desc);
        values.put(TaskContract.TaskEntry.COL_MONTH, cal.MONTH);
        values.put(TaskContract.TaskEntry.COL_DAY, cal.DAY_OF_MONTH);
        values.put(TaskContract.TaskEntry.COL_YEAR, cal.YEAR);

        viewModel.getDb().insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        viewModel.getDb().close();
        viewModel.updateUI();
        /*
        List<Task> currTaskList = viewModel.getTaskList().getValue();
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(desc);
        task.setDueDate(cal);
        currTaskList.add(task);
        viewModel.setTaskList(currTaskList);*/
    }

    public void toggleCompleted(int position) {
        viewModel.toggleItem(position);
    }

    public void changeSelector (int selector, List<Task> tasks) {
        this.selector = selector;
        updateItems(tasks);
    }

    public void showNewTaskDialog() {
        NewTaskDialogFragment dialog = new NewTaskDialogFragment();
        dialog.show(getFragmentManager(), NEW_TASK_FRAGMENT_TAG);
    }

}
