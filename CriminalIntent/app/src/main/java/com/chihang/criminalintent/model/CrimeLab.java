package com.chihang.criminalintent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import com.chihang.criminalintent.database.CrimeBaseHelper;
import com.chihang.criminalintent.database.CrimeCursorWrapper;
import com.chihang.criminalintent.database.CrimeDBSchema;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.os.Environment.DIRECTORY_PICTURES;

public class CrimeLab {
  private static final Logger logger = Logger.getLogger(CrimeLab.class.getName());
  private static CrimeLab sCrimeLab;

  private Context mContext;
  private SQLiteDatabase mDatabase;

  private CrimeLab(Context context) {
    mContext = context.getApplicationContext();
    mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
  }

  public static CrimeLab get(Context context) {
    if (sCrimeLab == null) {
      sCrimeLab = new CrimeLab(context);
    }
    return sCrimeLab;
  }

  @Nullable public Crime getCrime(UUID id) {
    CrimeCursorWrapper cursor = query(CrimeDBSchema.CrimeTable.Cols.UUID + " = ?",
        new String[] { id.toString() });

    try {
      if (cursor.getCount() == 0) {
        return null;
      }

      cursor.moveToFirst();
      return cursor.getCrime();
    } finally {
      cursor.close();
    }
  }

  @Nullable public File getPhotoFile(Crime crime) {
    File storageDir = mContext.getExternalFilesDir(DIRECTORY_PICTURES);

    File tempFile;
    try {
      tempFile = File.createTempFile(crime.getPhotoFilename(), ".jpg", storageDir);
    } catch (IOException e) {
      logger.log(Level.WARNING, "IOException occurs: %s", e);
      return null;
    }
    // // Save a file
    String absolutePath = tempFile.getAbsolutePath();

    return tempFile;
  }

  public List<Crime> getCrimes() {
    List<Crime> crimes = new ArrayList<>();

    CrimeCursorWrapper cursor = query(null, null);
    try {
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
        crimes.add(cursor.getCrime());
        cursor.moveToNext();
      }
    } finally {
      cursor.close();
    }

    return crimes;
  }

  public void add(Crime crime) {
    mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, getContextValues(crime));
  }

  public void updateCrime(Crime crime) {
    mDatabase.update(CrimeDBSchema.CrimeTable.NAME, getContextValues(crime),
        CrimeDBSchema.CrimeTable.Cols.UUID + " = ?", new String[] { crime.getId().toString() });
  }

  private static ContentValues getContextValues(Crime crime) {
    ContentValues values = new ContentValues();
    values.put(CrimeDBSchema.CrimeTable.Cols.UUID, crime.getId().toString());
    values.put(CrimeDBSchema.CrimeTable.Cols.TITLE, crime.getTitle());
    values.put(CrimeDBSchema.CrimeTable.Cols.DATE, crime.getDate().getTime());
    values.put(CrimeDBSchema.CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
    values.put(CrimeDBSchema.CrimeTable.Cols.SUSPECT, crime.getSuspect());
    return values;
  }

  private CrimeCursorWrapper query(String whereClause, String[] whereArgs) {
    return new CrimeCursorWrapper(mDatabase.query(
        CrimeDBSchema.CrimeTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    ));
  }
}
