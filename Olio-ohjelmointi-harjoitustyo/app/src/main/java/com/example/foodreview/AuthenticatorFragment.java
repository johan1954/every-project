package com.example.foodreview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class AuthenticatorFragment extends Fragment {

    View view;
    EditText authenticatorEditText;
    TextView authenticatorCheck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_authenticatorfragment, container, false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        authenticatorEditText = this.view.findViewById(R.id.authenticatorEditText);
        authenticatorCheck = this.view.findViewById(R.id.authenticatorCheck);

        String string = randomString();
        authenticatorCheck.setText(string);
    }

    public String getAuthenticatorEditText() {
        String string = authenticatorEditText.getText().toString();
        return string;
    }

    public String getAuthenticatorCheck() {
        String string = authenticatorCheck.getText().toString();
        return string;
    }

    //Generates the random number string for the authenticator
    public String randomString() {
        String string = "";

        Random random = new Random();
        for (int i = 0; i < 6; i++){
            int n = random.nextInt(10);
            string = string + n;
        }
        return string;
    }
}
