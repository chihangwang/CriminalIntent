package com.chihang.criminalintent.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.chihang.criminalintent.R;
import com.chihang.criminalintent.activity.CrimePagerActivity;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import java.util.List;

import static java.lang.String.valueOf;

public class CrimeListFragment extends Fragment {
  private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

  private RecyclerView mCrimeRecycleView;
  private CrimeAdapter mCrimeAdapter;
  private boolean mSubtitleVisible;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

    mCrimeRecycleView = v.findViewById(R.id.crime_recycle_view);
    mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

    if (savedInstanceState != null) {
      mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE, false);
    }

    updateUI();

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();

    updateUI();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);

    MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
    if (mSubtitleVisible) {
      subtitleItem.setTitle(R.string.hide_subtitle);
    } else {
      subtitleItem.setTitle(R.string.show_subtitle);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_crime:
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).add(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);
        return true;

      case R.id.menu_item_show_subtitle:
        mSubtitleVisible = !mSubtitleVisible;
        // trigger a re-creation of the tool bar so that `onCreateOptionsMenu` will be called again.
        getActivity().invalidateOptionsMenu();
        updateSubtitle();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void updateSubtitle() {
    int crimeCount = CrimeLab.get(getActivity()).getCrimes().size();
    String subtitle =
        getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount,
            valueOf(crimeCount));

    if (!mSubtitleVisible) {
      subtitle = null;
    }

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.getSupportActionBar().setSubtitle(subtitle);
  }

  private void updateUI() {
    List<Crime> crimes = CrimeLab.get(getContext()).getCrimes();

    if (mCrimeAdapter == null) {
      mCrimeAdapter = new CrimeAdapter(crimes);
      mCrimeRecycleView.setAdapter(mCrimeAdapter);
    } else {
      mCrimeAdapter.notifyDataSetChanged();
    }

    updateSubtitle();
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
