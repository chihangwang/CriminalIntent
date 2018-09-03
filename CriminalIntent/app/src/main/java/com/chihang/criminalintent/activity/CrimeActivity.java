package com.chihang.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import java.util.UUID;

import static com.chihang.criminalintent.fragment.CrimeFragment.newInstance;

public class CrimeActivity extends SingleFragmentActivity {
  private static final String EXTRA_CRIME_ID = CrimeActivity.class.getCanonicalName() + "crime_id";

  @Override
  public Fragment createFragment() {
    UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
    return newInstance(crimeId);
  }

  public static Intent newIntent(Context packageContext, UUID crimeId) {
    return new Intent(packageContext, CrimeActivity.class)
        .putExtra(EXTRA_CRIME_ID, crimeId);
  }
}
