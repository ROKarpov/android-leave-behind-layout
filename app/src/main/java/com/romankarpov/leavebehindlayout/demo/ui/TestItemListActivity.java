package com.romankarpov.leavebehindlayout.demo.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;

import com.romankarpov.leavebehindlayout.demo.NamesApplication;
import com.romankarpov.leavebehindlayout.demo.R;
import com.romankarpov.leavebehindlayout.demo.model.Contact;
import com.romankarpov.leavebehindlayout.demo.services.UiNamesService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class TestItemListActivity extends AppCompatActivity {
    private static int DATA_COUNT = 50;

    private UiNamesService mNamesService;

    private View mRootView;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Snackbar mShownSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testitem_list);

        mRootView = findViewById(R.id.list_view_root);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView)findViewById(R.id.testitem_list);
        mNamesService = ((NamesApplication)this.getApplication())
                .getUiNamesService();
        tryLoadData();
    }

    private void tryLoadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNamesService.getContacts(DATA_COUNT).enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                setupRecyclerView(response.body());
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                showErrorSnackbar();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerView(List<Contact> contacts) {
        mRecyclerView.setAdapter(new ContactRecyclerViewAdapter(contacts));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this.getApplicationContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void showErrorSnackbar() {
        mShownSnackbar = Snackbar.make(mRootView, R.string.error_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryLoadData();
                        if (mShownSnackbar != null) {
                            mShownSnackbar.dismiss();
                            mShownSnackbar = null;
                        }
                    }
                });
        mShownSnackbar.show();
    }
}
