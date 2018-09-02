package com.chihang.criminalintent.model;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
  private static CrimeLab sCrimeLab;

  private List<Crime> mCrimes;

  private CrimeLab(Context context) {
    mCrimes = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      Crime c = new Crime();
      c.setTitle("Crime #" + i);
      c.setSolved(i % 2 == 0);
      mCrimes.add(c);
    }
  }

  public List<Crime> getCrimes() {
    return mCrimes;
  }

  public Crime getCrime(UUID id) {
    for (Crime c : mCrimes) {
      if (c.getId().equals(id)) {
        return c;
      }
    }
    return null;
  }

  public static CrimeLab get(Context context) {
    if (sCrimeLab == null) {
      sCrimeLab = new CrimeLab(context);
    }
    return sCrimeLab;
  }
}
