package com.ualr.todohub.fragments;

import android.content.Context;
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
import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;
import com.ualr.todohub.utils.DataGenerator;

import java.util.List;

public class TaskListFragment extends Fragment {

    public static Context ctx;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    public Context mContext;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton mFAB;
    private static TaskViewModel viewModelUncompleted;
    private static TaskViewModel viewModelCompleted;
    private static TaskViewModel viewModelAll;
    private static TaskViewModel viewModel;
    private LiveData<List<Task>> uncompletedTasks;
    private LiveData<List<Task>> completedTasks;
    private LiveData<List<Task>> allTasks;

    public static int selector = 0; //CHANGE THIS LATER

    private static final String TAG = TaskListFragment.class.getSimpleName();

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
        allTasks = viewModel.getTaskList();
        allTasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks);
            }
        });

        /*viewModelUncompleted = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        viewModelCompleted = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        viewModelAll = new ViewModelProvider(getActivity()).get(TaskViewModel.class);
        uncompletedTasks = viewModelUncompleted.getTaskList();
        uncompletedTasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks, selector);
            }
        });

        completedTasks = viewModelCompleted.getTaskList();
        completedTasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks, selector);
            }
        });
        allTasks = viewModelAll.getTaskList();
        allTasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks, selector);
            }
        });*/
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
        // TODO 01. Generate the item list to be displayed using the DataGenerator class
        List<Task> items = DataGenerator.getTaskData(mContext);
        viewModel.setTaskList(items); //setting the model up with items
        final Context ctx = mContext;
        //Log.d(TAG, "Uncompleted size: " + uncompletedTasks.getValue().size() /*+ "\nCompleted size: " + completedTasks.getValue().size() + "\n All: " + allTasks.getValue().size()*/);

        // TODO 03. Do the setup of a new RecyclerView instance to display the item list properly
        //RecyclerView contactListView = mBinding.recyclerView;
        // TODO 04. Define the layout of each item in the list
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(mContext, allTasks.getValue(), selector); // 0: Uncompleted tasks, 1: Completed Tasks, 2: All Tasks
        //Log.d(TAG, viewModel.getTaskList().getValue().toString());

        // TODO 09. Create a new instance of the created Adapter class and bind it to the RecyclerView instance created in step 03
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

    public void toggleCompleted(int position) {
        allTasks.getValue().get(position).toggleCompleted();
        notify();
    }

    public void showNewTaskDialog() { //FIX THIS TO SHOW NEW TASK DIALOG

    }

}
