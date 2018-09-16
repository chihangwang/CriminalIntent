package com.chihang.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.chihang.criminalintent.R;
import com.chihang.criminalintent.fragment.CrimeFragment;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
  private static final String EXTRA_CRIME_ID =
      CrimePagerActivity.class.getCanonicalName() + "crime_id";

  private ViewPager mViewPager;
  private List<Crime> mCrimes;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_crime_pager);

    mCrimes = CrimeLab.get(this).getCrimes();
    FragmentManager fragmentManager = getSupportFragmentManager();

    mViewPager = findViewById(R.id.activity_crime_pager_view_pager);
    mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
      @Override public Fragment getItem(int i) {
        return CrimeFragment.newInstance(mCrimes.get(i).getId());
      }

      @Override public int getCount() {
        return mCrimes.size();
      }
    });

    UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    mViewPager.setCurrentItem(CrimeLab.get(this).getPosition(crimeId));
  }

  public static Intent newIntent(Context packageContext, UUID crimeId) {
    Intent intent = new Intent(packageContext, CrimePagerActivity.class);
    intent.putExtra(EXTRA_CRIME_ID, crimeId);
    return intent;
  }
}
