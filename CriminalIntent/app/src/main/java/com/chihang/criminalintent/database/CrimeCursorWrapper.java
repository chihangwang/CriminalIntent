package com.chihang.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.chihang.criminalintent.model.Crime;
import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
  public CrimeCursorWrapper(Cursor cursor) {
    super(cursor);
  }

  public Crime getCrime() {
    String uuid = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.UUID));
    String title = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.TITLE));
    long date = getLong(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.DATE));
    int isSolved = getInt(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.SOLVED));
    String suscpect = getString(getColumnIndex(CrimeDBSchema.CrimeTable.Cols.SUSPECT));

    Crime crime = new Crime(UUID.fromString(uuid));
    crime.setTitle(title);
    crime.setDate(new Date(date));
    crime.setSolved(isSolved == 1);
    crime.setSuspect(suscpect);

    return crime;
  }
}
