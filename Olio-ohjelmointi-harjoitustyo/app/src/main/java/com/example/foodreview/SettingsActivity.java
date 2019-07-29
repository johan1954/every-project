package com.example.foodreview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    TextView fieldUsernameText;
    EditText fieldOldPassword, fieldNewPassword,fieldNewPasswordAgain, fieldUsername;
    Button changeNickname, changePassword, changeUni;
    DatabaseManager dbms;
    Context context;
    Spinner settingsUniSpinner;
    UniversityManager universityManager;
    ArrayList<String> uniNames;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set the username from another activity
        fieldUsernameText = findViewById(R.id.fieldUsernameText);
        username = getIntent().getStringExtra("username");
        fieldUsernameText.setText(username);

        fieldOldPassword = findViewById(R.id.oldPassword);
        fieldNewPassword = findViewById(R.id.newPassword);
        fieldNewPasswordAgain = findViewById(R.id.newPasswordAgain);
        fieldUsername = findViewById(R.id.fieldUsername);
        changeNickname = findViewById(R.id.changeNickname);
        changePassword = findViewById(R.id.changePassword);
        changeUni = findViewById(R.id.changeUni);
        settingsUniSpinner = findViewById(R.id.settingsUniSpinner);
//        cancel = findViewById(R.id.settings_cancel);
        context = this;
        dbms = DatabaseManager.getInstance(context);
        universityManager = UniversityManager.getInstance();
        final PasswordChecker pwc = PasswordChecker.getInstance(context);
        uniNames = universityManager.getUniNames();

        Toolbar toolbar = findViewById(R.id.toolbarsettings);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.settings_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivityCheck();
            }
        });

        fieldUsername.setText(dbms.getOwnUser(username).getNickname());

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeActivityCheck();
//            }
//        });

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, uniNames);
        adapterUni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        settingsUniSpinner.setAdapter(adapterUni);

        int homeUni = dbms.getOwnUser(username).getHomeUniId() - 1;
        settingsUniSpinner.setSelection(homeUni);

        changeNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = fieldOldPassword.getText().toString().trim();

                if (checkOldPassword(username, oldPassword)) {
                    Snackbar.make(v, getResources().getString(R.string.settings_oldpasswordwrong), Snackbar.LENGTH_LONG).show();
                } else {
                    if (fieldUsername.getText().toString().length() < 1 || fieldUsername.getText().toString().length() > 16) {
                        Snackbar.make(v, getResources().getString(R.string.signup_wrongnickname), Snackbar.LENGTH_LONG).show();
                        fieldUsername.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                        fieldUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);
                        fieldUsername.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if (fieldUsername.getText().toString().length() < 1 || fieldUsername.getText().toString().length() > 16) {
                                    fieldUsername.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                                    fieldUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0, R.drawable.ic_error, 0);

                                }
                                else {
                                    fieldUsername.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                                    fieldUsername.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,0, 0);
                                }

                            }
                        });
                    } else {
                        dbms.modifyNickname(username, fieldUsername.getText().toString().trim());
                        fieldOldPassword.setText("");
                        Toast.makeText(context, "Nickname changed", Toast.LENGTH_SHORT).show();
                    }

                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        changeUni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = fieldOldPassword.getText().toString().trim();

                if (checkOldPassword(username, oldPassword)) {
                    Snackbar.make(v, getResources().getString(R.string.settings_oldpasswordwrong), Snackbar.LENGTH_LONG).show();
                } else {
                    dbms.updateNewHomeUni(universityManager.getUniversity(uniNames.get(settingsUniSpinner.getSelectedItemPosition())).getUniId(), username);
                    fieldOldPassword.setText("");
                    Toast.makeText(context, "Home university changed", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = fieldOldPassword.getText().toString().trim();
                String newPassword = fieldNewPassword.getText().toString().trim();

                String message = pwc.checker(newPassword);

                //Check from the database whether user's old password is correct
                if (checkOldPassword(username, oldPassword)) {
                    Snackbar.make(v, getResources().getString(R.string.settings_oldpasswordwrong), Snackbar.LENGTH_LONG).show();
                } else {
                    //If the old password is correct, let's check whether the new password fulfills all the requirements
                    if (message.isEmpty()) {
                        if (fieldOldPassword.getText().toString().equals(fieldNewPassword.getText().toString())) {
                            //Old and new passwords are the same. Send error message to user
                            Snackbar.make(v, getResources().getString(R.string.settings_oldnewpasswordnotequal), Snackbar.LENGTH_LONG).show();
                            fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                            fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                            fieldNewPassword.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                                    fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);
                                }
                            });
                        } else if (!fieldNewPasswordAgain.getText().toString().equals(fieldNewPassword.getText().toString())) {
                            Snackbar.make(v, getResources().getString(R.string.signup_password_notequal), Snackbar.LENGTH_LONG).show();
                            fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                            fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                        } else if (!dbms.changePassword(username, newPassword)) {
                            Snackbar.make(v, getResources().getString(R.string.settings_passworderror), Snackbar.LENGTH_LONG).show();
                        } else {
                            //Password is changed successfully, clear text fields and print message to user
                            fieldOldPassword.setText("");
                            fieldOldPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                            fieldOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);

                            fieldNewPassword.setText("");
                            fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                            fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);

                            fieldNewPasswordAgain.setText("");
                            fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                            fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);

                            Snackbar.make(v, getResources().getString(R.string.settings_passwordsuccessful), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        //Print error message to user
                        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
                    }
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        fieldNewPasswordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String message = pwc.checker(fieldNewPassword.getText().toString());

                //Let's check if we get any error messages from passwordCheck
                //If we don't, the password fulfills the requirements and we don't need to add any text listeners to the first editText
                //If the password is not ok, we make the line red + add error drawable to indicate that there is an error in the password
                if (!message.isEmpty()) {
                    fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                    fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);

                    fieldNewPassword.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (pwc.checker(fieldNewPassword.getText().toString()).isEmpty()) {
                                fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                                fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                            }
                            else {
                                fieldNewPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                                fieldNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                            }
                        }
                    });
                }
                else {

                    if (fieldNewPasswordAgain.getText().toString().equals(fieldNewPassword.getText().toString())) {
                        fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                        fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                    }
                    else {
                        fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                        fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                    }

                    fieldNewPasswordAgain.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (fieldNewPasswordAgain.getText().toString().equals(fieldNewPassword.getText().toString())) {
                                fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                                fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0,0, 0);
                            }
                            else {
                                fieldNewPasswordAgain.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
                                fieldNewPasswordAgain.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
                            }
                        }
                    });
                }
            }
        });


    }

    //If user has typed something in the text fields and tries to leave, alert dialog shows up
    private void closeActivityCheck() {
        if (!fieldOldPassword.getText().toString().equals("") ||
                !fieldNewPassword.getText().toString().equals("") ||
                !fieldNewPasswordAgain.getText().toString().equals("") ||
                !fieldUsername.getText().toString().equals(dbms.getOwnUser(username).getNickname())) {
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle(R.string.settings_alertdialog_notsaved)
                    .setMessage(R.string.signup_alertdialog_confirm)
                    .setPositiveButton(R.string.signup_alertdialog_exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //onClickListener if user clicks exit
                            dialog.cancel();
                            closeActivity();
                        }
                    })
                    //Keep editing button doesn't need onClickListener because it just goes back to the activity
                    .setNegativeButton(R.string.signup_alertdialog_keepediting, null)
                    .setIcon(R.drawable.ic_warning)
                    .show();
        }
        else {
            closeActivity();
        }
    }

    private void closeActivity() {

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean checkOldPassword(String username, String oldPassword) {
        if (!dbms.searchDatabase(username, oldPassword)) {
            //If the password is incorrect, add error message + red tint to indicate that there is error
            fieldOldPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.error));
            fieldOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, R.drawable.ic_error, 0);
            fieldOldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Set text field back to normal after user edits text
                    fieldOldPassword.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
                    fieldOldPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock, 0, 0, 0);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        closeActivityCheck();
    }
}
