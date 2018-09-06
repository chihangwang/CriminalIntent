package com.chihang.criminalintent.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import com.chihang.criminalintent.R;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {
  private static final String ARG_DATE = "arg.date";
  private static final String EXTRA_DATE =
      DatePickerFragment.class.getCanonicalName() + "extra.date";

  private DatePicker mDatePicker;

  public static DatePickerFragment newInstance(Date date) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(ARG_DATE, date);

    DatePickerFragment datePickerFragment = new DatePickerFragment();
    datePickerFragment.setArguments(bundle);
    return datePickerFragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Date date = (Date) getArguments().getSerializable(ARG_DATE);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DATE);

    View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

    mDatePicker = v.findViewById(R.id.dialog_date_date_picker);
    mDatePicker.init(year, month, day, null);

    return new AlertDialog.Builder(getActivity())
        .setTitle(R.string.date_picker_title)
        .setView(mDatePicker)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            Date selectedDate = new GregorianCalendar(
                mDatePicker.getYear(),
                mDatePicker.getMonth(),
                mDatePicker.getDayOfMonth()).getTime();
            setResult(selectedDate);
          }
        })
        .create();
  }

  private void setResult(Date date) {
    if (getTargetFragment() == null) {
      return;
    }

    Intent intent = new Intent();
    intent.putExtra(EXTRA_DATE, date);
    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
  }

  @Nullable public static Date getCrimeDateFromIntent(Intent intent) {
    return (Date) intent.getSerializableExtra(EXTRA_DATE);
  }
}
