package com.ualr.todohub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ualr.todohub.database.DataBaseHelper;
import com.ualr.todohub.fragments.DatePickerDialogFragment;
import com.ualr.todohub.fragments.NewTaskDialogFragment;
import com.ualr.todohub.fragments.SettingsDialogFragment;
import com.ualr.todohub.fragments.TaskListFragment;
import com.ualr.todohub.model.Task;
import com.ualr.todohub.model.TaskViewModel;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FRAGMENT_TAG = "TaskListFragment";
    private static final String SETTINGS_FRAGMENT_TAG = "SettingsDialogFragment";
    private static final String DATEPICKER_FRAGMENT_TAG = "DatePickerDialogFragment";
    private static final String NEW_TASK_FRAGMENT_TAG = "NewTaskDialogFragment";

    private FloatingActionButton mFAB;
    private Button mDueDateBtn;
    public static Context here;
    private RecyclerView mRecyclerView;
    private Button mDelete;
    private ViewModel viewModel;
    private CoordinatorLayout parentView;
    public DataBaseHelper dataBaseHelper;
    private String msg;
    private int duration;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mBinding = InboxFragmentBinding.inflate(getLayoutInflater());
        //setContentView(mBinding.getRoot());
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean dialogShown = settings.getBoolean("dialogShown", false);

        if (!dialogShown) {
            showStartDialog();
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("dialogShown", true);
            editor.commit();
        }
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initComponent();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new TaskListFragment(), FRAGMENT_TAG);
        ft.commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Welcome to ToDoHub!")
                .setMessage("To start your first task, click the button in the bottom right!")
                .setPositiveButton(R.string.positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void showSettingsDialog() {
        SettingsDialogFragment dialog = new SettingsDialogFragment();
        dialog.show(getSupportFragmentManager(), SETTINGS_FRAGMENT_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                TaskListFragment listFragment1 = (TaskListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
                showSettingsDialog();
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

    public void nullDueDate() {
        Snackbar.make(findViewById(R.id.rootView), R.string.null_due_date,
                Snackbar.LENGTH_SHORT)
                .show();
    }

    public void nullTitle() {
        Snackbar.make(findViewById(R.id.rootView), R.string.null_title,
                Snackbar.LENGTH_SHORT)
                .show();
    }
}