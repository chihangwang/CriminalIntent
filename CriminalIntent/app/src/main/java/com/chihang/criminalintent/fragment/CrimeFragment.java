package com.chihang.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.UUID;

public class CrimeFragment extends Fragment {
  private static final String CRIME_ID_KEY = "crime_id";

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

    setResult(crimeId);
  }

  @Nullable public static UUID getCrimeIdFromIntent(Intent intent) {
    return (UUID) intent.getSerializableExtra(CRIME_ID_KEY);
  }

  private void setResult(UUID crimeId) {
    Intent intent = new Intent();
    intent.putExtra(CRIME_ID_KEY, crimeId);
    getActivity().setResult(Activity.RESULT_OK, intent);
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
    mDateButton.setText(mCrime.getDate().toString());
    mDateButton.setEnabled(false);

    mSolvedCheckBox = v.findViewById(R.id.crime_solved);
    mSolvedCheckBox.setChecked(mCrime.isSolved());
    mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setSolved(isChecked);
      }
    });

    return v;
  }
}
