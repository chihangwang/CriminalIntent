package com.chihang.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.chihang.criminalintent.R;
import com.chihang.criminalintent.activity.CrimePagerActivity;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import java.util.List;

public class CrimeListFragment extends Fragment {
  private static final int REQUEST_CRIME = 1;

  private RecyclerView mCrimeRecycleView;
  private CrimeAdapter crimeAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

    mCrimeRecycleView = v.findViewById(R.id.crime_recycle_view);
    mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    updateUI();

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();

    //updateUI();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    switch (requestCode) {
      case REQUEST_CRIME:
        if (resultCode == Activity.RESULT_OK) {
          int position =
              CrimeLab.get(getContext()).getPosition(CrimeFragment.getCrimeIdFromIntent(intent));
          crimeAdapter.notifyItemChanged(position);
        }
        break;
    }

    return;
  }

  private void updateUI() {
    List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();

    crimeAdapter = new CrimeAdapter(crimes);
    mCrimeRecycleView.setAdapter(crimeAdapter);
  }

  private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Crime mCrime;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckBox;

    public CrimeHolder(View v) {
      super(v);
      v.setOnClickListener(this);

      mTitleTextView = v.findViewById(R.id.list_item_crime_title_text_view);
      mDateTextView = v.findViewById(R.id.list_item_crime_date_text_view);
      mSolvedCheckBox = v.findViewById(R.id.list_item_crime_solved_check_box);
    }

    public void bindCrime(Crime crime) {
      this.mCrime = crime;
      this.mTitleTextView.setText(crime.getTitle());
      this.mDateTextView.setText(crime.getDate().toString());
      this.mSolvedCheckBox.setChecked(crime.isSolved());
    }

    @Override
    public void onClick(View v) {
      Intent intent = CrimePagerActivity.newIntent(getContext(), mCrime.getId());
      startActivityForResult(intent, REQUEST_CRIME);
    }
  }

  private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
    private List<Crime> mCrimes;

    public CrimeAdapter(List<Crime> crimes) {
      mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      // it must be a text view
      View v = LayoutInflater.from(getContext())
          .inflate(R.layout.list_item_crime, parent, false);

      return new CrimeHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
      holder.bindCrime(mCrimes.get(position));
    }

    @Override
    public int getItemCount() {
      return mCrimes.size();
    }
  }
}
