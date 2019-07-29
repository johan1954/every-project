package com.example.elokuvattv;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class timePicker extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar kalenteri = Calendar.getInstance();
        int hour = kalenteri.get(Calendar.HOUR_OF_DAY);
        int minute = kalenteri.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog,(TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, true);
        return tpd;

    }
}
