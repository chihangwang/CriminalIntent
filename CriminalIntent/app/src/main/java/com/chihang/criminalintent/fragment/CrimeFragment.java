package com.chihang.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.chihang.criminalintent.R;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
  private static final String CRIME_ID_KEY = "crime_id";
  private static final String DIALOG_DATE = "dialog_date";
  private static final int REQUEST_DATE = 1;

  private Crime mCrime;
  private EditText mTitleField;
  private Button mDateButton;
  private CheckBox mSolvedCheckBox;

  public static CrimeFragment newInstance(UUID crimeId) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(CRIME_ID_KEY, crimeId);

    CrimeFragment crimeFragment = new CrimeFragment();
    crimeFragment.setArguments(bundle);
    return crimeFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID_KEY);
    mCrime = CrimeLab.get(getContext()).getCrime(crimeId);
  }

  @Override
  public void onPause() {
    super.onPause();

    CrimeLab.get(getContext()).updateCrime(mCrime);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false);

    mTitleField = v.findViewById(R.id.crime_title);
    mTitleField.setText(mCrime.getTitle());
    mTitleField.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {
      }
    });

    mDateButton = v.findViewById(R.id.crime_date);
    updateDate();
    mDateButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(fragmentManager, DIALOG_DATE);
      }
    });

    mSolvedCheckBox = v.findViewById(R.id.crime_solved);
    mSolvedCheckBox.setChecked(mCrime.isSolved());
    mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setSolved(isChecked);
      }
    });

    return v;
  }

  // To be called explicitly by child fragment to pass data back. We use this pattern when two
  // fragments that live in the same activity want to talk to each other.
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_DATE:
        if (resultCode != Activity.RESULT_OK) {
          return;
        }

        Date selectedDate = DatePickerFragment.getCrimeDateFromIntent(data);
        mCrime.setDate(selectedDate);
        updateDate();
        break;

      default:
        break;
    }
  }

  private void updateDate() {
    mDateButton.setText(mCrime.getDate().toString());
  }
}
