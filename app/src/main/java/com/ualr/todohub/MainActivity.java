package com.ualr.todohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ualr.todohub.fragments.TaskListFragment;
import com.ualr.todohub.model.TaskViewModel;

import java.util.List;

import javax.sql.DataSource;


// TODO 05. Create a new Adapter class and the corresponding ViewHolder class in a different file. The adapter will be used to populate
//  the recyclerView and manage the interaction with the items in the list
// TODO 06. Detect click events on the list items. Implement a new method to toggle items' selection in response to click events
// TODO 07. Detect click events on the thumbnail located on the left of every list row when the corresponding item is selected.
//  Implement a new method to delete the corresponding item in the list
// TODO 08. Create a new method to add a new item on the top of the list. Use the DataGenerator class to create the new item to be added.

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "TaskListFragment";

    private FloatingActionButton mFAB;
    public static Context here;
    private RecyclerView mRecyclerView;
    private Button mDelete;
    private ViewModel viewModel;
    private CoordinatorLayout parentView;
    private String msg;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mBinding = InboxFragmentBinding.inflate(getLayoutInflater());
        //setContentView(mBinding.getRoot());
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initComponent();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new TaskListFragment(), FRAGMENT_TAG);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFAB(View view) {
        mRecyclerView = findViewById(R.id.recyclerView);
        mFAB = findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskListFragment listFragment = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                listFragment.showNewTaskDialog();

                /*List<Inbox> currentData = viewModel.getInboxList();
                currentData.add(0, newItem);
                viewModel.setInboxList(currentData);*/

            }
        });
    }
}