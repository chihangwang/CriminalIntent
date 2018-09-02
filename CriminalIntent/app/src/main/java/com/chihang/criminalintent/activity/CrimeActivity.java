package com.chihang.criminalintent.activity;

import android.support.v4.app.Fragment;
import com.chihang.criminalintent.fragment.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {

  @Override
  public Fragment createFragment() {
    return new CrimeFragment();
  }
}
