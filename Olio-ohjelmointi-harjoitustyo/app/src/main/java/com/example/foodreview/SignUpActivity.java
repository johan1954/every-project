package com.example.foodreview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    private EditText username, nickname, password, passwordagain;
    private Context context = this;
    Spinner uniSpinner;
    UniversityManager universityManager;
    ArrayList<String> uniNames;
    DatabaseManager dbms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button create, cancel;
        username = findViewById(R.id.username);
        nickname = findViewById(R.id.nickname);
        password = findViewById(R.id.newPassword);
        passwordagain = findViewById(R.id.newPasswordAgain);
        uniSpinner = findViewById(R.id.uniSpinner);
        dbms = DatabaseManager.getInstance(this);
        universityManager = UniversityManager.getInstance();
        dbms.updateUniversities();
        uniNames = universityManager.getUniNames();

        create = findViewById(R.id.create);
        cancel = findViewById(R.id.cancel);

        final DatabaseManager dbmsSU = DatabaseManager.getInstance(context);
        final PasswordChecker pwc = PasswordChecker.getInstance(context);

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, uniNames);
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uniSpinner.setAdapter(adapterUni);

        System.out.println("size " + uniNames.size());

        passwordagain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String message = pwc.checker(password.getText().toString());
                //Let's check if we get any error messages from passwordCheck
                //If we don't, the password fulfills the requirements and we don't need to add any text listeners to the first editText
                //If the password is not ok, we make the line red + add error drawable to indicate that there is an error in the password
                if (!message.isEmpty()) {
                    password.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                    password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);

                    password.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (pwc.checker(password.getText().toString()).isEmpty()) {
                                password.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                                password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                            }
                            else {
                                password.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                                password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                            }
                        }
                    });
                }
                else {

                    if (passwordagain.getText().toString().equals(password.getText().toString())) {
                        passwordagain.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                        passwordagain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                    }
                    else {
                        passwordagain.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                        passwordagain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                    }

                    passwordagain.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (passwordagain.getText().toString().equals(password.getText().toString())) {
                                passwordagain.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                                passwordagain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                            }
                            else {
                                passwordagain.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                                passwordagain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                            }
                        }
                    });
                }
            }
        });



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if the password fulfills all the requirements
                String message = pwc.checker(password.getText().toString());
                //Print the message to user if it's not empty
                if (!message.isEmpty()) {
                    Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                }
                else if (!passwordagain.getText().toString().equals(password.getText().toString())) {
                    Snackbar.make(v, getResources().getString(R.string.signup_password_notequal), Snackbar.LENGTH_LONG).show();
                    passwordagain.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                    passwordagain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                }
                else if (username.getText().toString().length() < 6 || username.getText().toString().length() > 16) {
                    Snackbar.make(v, getResources().getString(R.string.signup_wrongusername), Snackbar.LENGTH_LONG).show();
                    username.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                    username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);
                    username.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (username.getText().toString().length() < 6 || username.getText().toString().length() > 16) {
                                username.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                                username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);

                            }
                            else {
                                username.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                                username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,0, 0);
                            }

                        }
                    });
                }
                else if (nickname.getText().toString().length() < 1 || nickname.getText().toString().length() > 16) {
                    Snackbar.make(v, getResources().getString(R.string.signup_wrongnickname), Snackbar.LENGTH_LONG).show();
                    nickname.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                    nickname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);
                    nickname.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (nickname.getText().toString().length() < 1 || nickname.getText().toString().length() > 16) {
                                nickname.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                                nickname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);

                            }
                            else {
                                nickname.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                                nickname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,0, 0);
                            }

                        }
                    });
                }
                //If the password fulfills all the requirements (error message is empty), we will create new user
                else {
                    String newUsername = username.getText().toString().trim();
                    String newPassword = password.getText().toString().trim();
                    String newNickname = nickname.getText().toString().trim();
                    int newHomeUniId = universityManager.getUniversity(uniNames.get(uniSpinner.getSelectedItemPosition())).getUniId();
                    //Check if user with this username has already been created
                    if (!dbmsSU.checkExistance(newUsername)) {
                        dbmsSU.addItem(newUsername, newPassword, newNickname, newHomeUniId);
                        closeActivity(1);
                    }
                    else {
                        Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.signup_usernametaken), Snackbar.LENGTH_LONG).show();
                        username.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.error));
                        username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);
                        username.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                username.setBackgroundTintList(ContextCompat.getColorStateList(SignUpActivity.this, R.color.colorAccent));
                                username.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,0, 0);
                            }
                        });
                    }
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivityCheck();
            }
        });
    }

    @Override
    public void onBackPressed() {
        closeActivityCheck();
    }


    //If user has typed something in the text fields and tries to leave, alert dialog shows up
    private void closeActivityCheck() {
        if (!username.getText().toString().equals("") || !password.getText().toString().equals("") || !passwordagain.getText().toString().equals("")) {
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle(R.string.signup_alertdialog_notsaved)
                    .setMessage(R.string.signup_alertdialog_confirm)
                    .setPositiveButton(R.string.signup_alertdialog_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //onClickListener if user clicks exit
                            dialog.cancel();
                            closeActivity(-1);
                        }
                    })
                    //Keep editing button doesn't need onClickListener because it just goes back to the activity
                    .setNegativeButton(R.string.signup_alertdialog_keepediting, null)
                    .setIcon(R.drawable.ic_warning)
                    .show();
        }
        else {
            closeActivity(-1);
        }
    }

    private void closeActivity(int endResult) {
        Intent intent = new Intent();
        //Activity can be ended two different ways: user creates new account successfully or user presses cancel
        //If cancel is pressed endResult equals -1 and activity is ended with RESULT_CANCELLED, otherwise with RESULT_OK
        if (endResult == -1) {
            setResult(RESULT_CANCELED, intent);
        }
        else {
            setResult(RESULT_OK, intent);
            intent.putExtra("username", username.getText().toString());
        }
        finish();
    }


}
