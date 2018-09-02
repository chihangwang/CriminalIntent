package com.chihang.criminalintent.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chihang.criminalintent.R;
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

  private void updateUI() {
    List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();
    crimeAdapter = new CrimeAdapter(crimes);

    mCrimeRecycleView.setAdapter(crimeAdapter);
  }

  private class CrimeHolder extends RecyclerView.ViewHolder {
    public TextView mTitleTextView;

    public CrimeHolder(View itemView) {
      super(itemView);
      mTitleTextView = (TextView) itemView;
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
          .inflate(android.R.layout.simple_list_item_1, parent, false);

      return new CrimeHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
      holder.mTitleTextView.setText(mCrimes.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
      return mCrimes.size();
    }
  }
}
