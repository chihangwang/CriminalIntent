package com.chihang.criminalintent.fragment;

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
import com.chihang.criminalintent.activity.CrimeActivity;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import java.util.List;

public class CrimeListFragment extends Fragment {
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

    updateUI();
  }

  private void updateUI() {
    List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();

    // on initial setup
    if (crimeAdapter == null) {
      crimeAdapter = new CrimeAdapter(crimes);
      mCrimeRecycleView.setAdapter(crimeAdapter);
    // on resume from back stack
    } else {
      crimeAdapter.notifyDataSetChanged();
    }
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
      mCrime = crime;
      mTitleTextView.setText(crime.getTitle());
      mDateTextView.setText(crime.getDate().toString());
      mSolvedCheckBox.setChecked(crime.isSolved());
    }

    @Override
    public void onClick(View v) {
      Intent intent = CrimeActivity.newIntent(getContext(), mCrime.getId());
      startActivity(intent);
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
