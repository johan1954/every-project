package com.example.foodreview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class AdminUserManager extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AdminUserManagerRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<User> mUserList;
    private DatabaseManager dbms;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_manager);

        dbms = DatabaseManager.getInstance(this);
        dbms.updateUsers();

        UserManager userManager = UserManager.getInstance();
        mUserList = userManager.getUsers();

        username = getIntent().getStringExtra("usernameUserManager");


        Toolbar toolbar = findViewById(R.id.toolbaradmin);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.adminusermanager_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buildRecyclerView();
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.adminUserManagerRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new AdminUserManagerRecyclerViewAdapter(mUserList, username);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AdminUserManagerRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCheckboxClick(int position, boolean isChecked) {
                if (isChecked) {
                    //TODO: Incorporate this method to accept value from recyclerView which is the users homeuni?
                    String tempError = dbms.modifyUser(username,mUserList.get(position).getUsername(), true);

                    if (tempError.equals("databaseError")) {
                        //TODO: Database error to user
                    }

                }
                else {
                    String tempError = dbms.modifyUser(username,mUserList.get(position).getUsername(), false);
                    if (tempError.equals("adminError")) {
                    }
                    else if (tempError.equals("databaseError")) {
                        //TODO: Database error to user
                    }
                }
            }
        });
    }
}
