package com.example.foodreview;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    Context context;
    EditText loginText, passwordText;
    AuthenticatorFragment authenticatorFragment;
    FrameLayout authenticatorFrame;
    String username;
    String authenticatorEditText;
    String authenticatorCheck;
    FragmentManager manager;
    boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        context = this;

        final Button login, signup;
        final DatabaseManager dbms = DatabaseManager.getInstance(context);
        if (!isLoaded) {
            dbms.hardCodeDatabaseTestData();
            isLoaded = true;
        }


        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        loginText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.passwordEditText);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = loginText.getText().toString().trim();
                String password = passwordText.getText().toString().trim();

                //Checks if the username and password are in the database and then starts the user authentication event
                if (dbms.searchDatabase(username, password)) {
                    authenticatorFragment = new AuthenticatorFragment();
                    authenticatorFrame = findViewById(R.id.authenticatorframe);
                    authenticatorFrame.setVisibility(View.VISIBLE);
                    manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.authenticatorframe, authenticatorFragment);
                    transaction.commit();


                }
                else {

                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.login_failed), Snackbar.LENGTH_LONG).show();
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Toast.makeText(this, getResources().getString(R.string.login_usercreated1) + " " + data.getStringExtra("username") + " " + getResources().getString(R.string.login_usercreated2), Toast.LENGTH_SHORT).show();
            }
//            else {
//                Toast.makeText(this, "Cancelled" , Toast.LENGTH_SHORT).show();
//            }
        }
    }

    @Override
    public void onBackPressed() { }


    //Checks if the user has authenticated itself successfully after pressing continue in the authenticator fragment and then starts the main activity
    public void fragmentContinue(View view) {
        AuthenticatorFragment authenticatorFragment = (AuthenticatorFragment) manager.findFragmentById(R.id.authenticatorframe);
        authenticatorEditText = authenticatorFragment.getAuthenticatorEditText();
        authenticatorCheck = authenticatorFragment.getAuthenticatorCheck();

        if (authenticatorEditText.equals(authenticatorCheck)) {
            Toast.makeText(this, getString(R.string.authenticator_correct), Toast.LENGTH_SHORT).show();

            authenticatorFrame.setVisibility(View.INVISIBLE);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.detach(authenticatorFragment);
            transaction.commit();

            Intent mainActivityIntent = new Intent(LogInActivity.this, MainActivity.class);
            setResult(RESULT_OK, mainActivityIntent);
            mainActivityIntent.putExtra("username", username);
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainActivityIntent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.authenticator_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    }


