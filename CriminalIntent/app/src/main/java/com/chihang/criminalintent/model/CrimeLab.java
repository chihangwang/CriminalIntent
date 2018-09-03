package com.chihang.criminalintent.model;

import android.content.Context;
import android.support.annotation.Nullable;
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

  @Nullable public Crime getCrime(UUID id) {
    for (Crime c : mCrimes) {
      if (c.getId().equals(id)) {
        return c;
      }
    }
    return null;
  }

  public int getPosition(UUID id) {
    int pos = 0;
    for (Crime c : mCrimes) {
      if (c.getId().equals(id)) {
        return pos;
      }

      pos++;
    }

    return -1;
  }

  public static CrimeLab get(Context context) {
    if (sCrimeLab == null) {
      sCrimeLab = new CrimeLab(context);
    }
    return sCrimeLab;
  }
}
