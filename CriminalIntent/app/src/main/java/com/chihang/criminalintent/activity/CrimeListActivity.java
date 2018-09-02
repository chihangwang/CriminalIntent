package com.chihang.criminalintent.activity;

import android.support.v4.app.Fragment;
import com.chihang.criminalintent.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

  @Override
  public Fragment createFragment() {
    return new CrimeListFragment();
  }
}
