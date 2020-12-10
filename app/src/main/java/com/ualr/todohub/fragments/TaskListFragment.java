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
    private FloatingActionButton mFAB;
    private static TaskViewModel viewModel;

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
        viewModel.getTaskList().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                updateItems(tasks);
                mRecyclerView.scrollToPosition(0);
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
        // TODO 01. Generate the item list to be displayed using the DataGenerator class
        List<Task> items = DataGenerator.getTaskData(mContext);
        viewModel.setTaskList(items); //setting the model up with items

        final Context ctx = mContext;

        // TODO 03. Do the setup of a new RecyclerView instance to display the item list properly
        //RecyclerView contactListView = mBinding.recyclerView;
        // TODO 04. Define the layout of each item in the list
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new Adapter(mContext, viewModel.getTaskList().getValue());
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

    public void showNewTaskDialog() { //FIX THIS TO SHOW NEW TASK DIALOG

    }

}
