package com.chihang.criminalintent.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.chihang.criminalintent.R;
import com.chihang.criminalintent.model.Crime;
import com.chihang.criminalintent.model.CrimeLab;
import com.chihang.criminalintent.util.PictureUtils;
import java.io.File;
import java.util.Date;
import java.util.UUID;

import static android.provider.MediaStore.EXTRA_OUTPUT;

public class CrimeFragment extends Fragment {
  private static final String CRIME_ID_KEY = "crime_id";
  private static final String DIALOG_DATE = "dialog_date";
  private static final int REQUEST_DATE = 1;
  private static final int REQUEST_CONTACT = 2;
  private static final int REQUEST_PHOTO = 3;

  private Crime mCrime;
  private File mPhotoFile;
  private EditText mTitleField;
  private Button mDateButton;
  private CheckBox mSolvedCheckBox;
  private Button mReportButton;
  private Button mSuspectButton;
  private ImageButton mPhotoButton;
  private ImageView mPhotoView;

  public static CrimeFragment newInstance(UUID crimeId) {
    Bundle bundle = new Bundle();
    bundle.putSerializable(CRIME_ID_KEY, crimeId);

    CrimeFragment crimeFragment = new CrimeFragment();
    crimeFragment.setArguments(bundle);
    return crimeFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID_KEY);
    mCrime = CrimeLab.get(getContext()).getCrime(crimeId);
    mPhotoFile = CrimeLab.get(getContext()).getPhotoFile(mCrime);
  }

  @Override
  public void onPause() {
    super.onPause();

    CrimeLab.get(getContext()).updateCrime(mCrime);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime, container, false);

    mTitleField = v.findViewById(R.id.crime_title);
    mTitleField.setText(mCrime.getTitle());
    mTitleField.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCrime.setTitle(s.toString());
      }

      @Override public void afterTextChanged(Editable s) {
      }
    });

    mDateButton = v.findViewById(R.id.crime_date);
    updateDate();
    mDateButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
        dialog.show(fragmentManager, DIALOG_DATE);
      }
    });

    mSolvedCheckBox = v.findViewById(R.id.crime_solved);
    mSolvedCheckBox.setChecked(mCrime.isSolved());
    mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mCrime.setSolved(isChecked);
      }
    });

    mReportButton = v.findViewById(R.id.crime_report);
    mReportButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
        // always create a chooser for the user to choose from
        i = Intent.createChooser(i, getString(R.string.crime_report_subject));
        startActivity(i);
      }
    });

    final Intent pickContact = new Intent(Intent.ACTION_PICK,
        ContactsContract.Contacts.CONTENT_URI);
    mSuspectButton = v.findViewById(R.id.crime_suspect);
    mSuspectButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivityForResult(pickContact, REQUEST_CONTACT);
      }
    });

    if (mCrime.getSuspect() != null) {
      mSuspectButton.setText(mCrime.getSuspect());
    }

    // Check if the device has apps that support the implicit intent here. Otherwise, it can crash
    // if there's none to support the intent.
    PackageManager packageManager = getActivity().getPackageManager();
    if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
      mSuspectButton.setEnabled(false);
    }

    mPhotoButton = v.findViewById(R.id.crime_camera);

    final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    boolean canTakePhoto =
        // Only if the File was successfully created
        mPhotoFile != null
            // // Ensure that there's a camera activity to handle the intent
            && captureImage.resolveActivity(packageManager) != null;
    mPhotoButton.setEnabled(canTakePhoto);

    if (canTakePhoto) {
      Uri photoURI = FileProvider.getUriForFile(getContext(),
          "com.chihang.criminalintent.android.fileprovider", // authority
          mPhotoFile);

      // write the full image to the path indicated by the uri
      captureImage.putExtra(EXTRA_OUTPUT, photoURI);
    }

    mPhotoButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        startActivityForResult(captureImage, REQUEST_PHOTO);
      }
    });

    mPhotoView = v.findViewById(R.id.crime_photo);
    updatePhotoView();

    return v;
  }

  // To be called explicitly by child fragment to pass data back. We use this pattern when two
  // fragments that live in the same activity want to talk to each other.
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    switch (requestCode) {
      case REQUEST_DATE:
        Date selectedDate = DatePickerFragment.getCrimeDateFromIntent(data);
        mCrime.setDate(selectedDate);
        updateDate();
        break;

      case REQUEST_CONTACT:
        Uri contactUri = data.getData();
        String[] queryFields = new String[] { ContactsContract.Contacts.DISPLAY_NAME };
        Cursor c = getActivity().getContentResolver()
            .query(contactUri, queryFields, null, null, null);

        try {
          // double check that you actually got results
          if (c.getCount() == 0) {
            return;
          }

          c.moveToFirst();
          mCrime.setSuspect(c.getString(0));
          mSuspectButton.setText(c.getString(0));
        } finally {
          c.close();
        }
        break;

      case REQUEST_PHOTO:
        updatePhotoView();
        break;

      default:
        break;
    }
  }

  private void updateDate() {
    mDateButton.setText(mCrime.getDate().toString());
  }

  private String getCrimeReport() {
    String solvedString;
    if (mCrime.isSolved()) {
      solvedString = getString(R.string.crime_report_solved);
    } else {
      solvedString = getString(R.string.crime_report_unsolved);
    }

    String dateFormat = "EEE, MMM, dd";
    String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

    String suspect = mCrime.getSuspect();
    if (suspect == null) {
      suspect = getString(R.string.crime_report_no_suspect);
    } else {
      suspect = getString(R.string.crime_report_suspect, mCrime.getSuspect());
    }

    return getString(R.string.crime_report,
        mCrime.getTitle(), dateString, solvedString, suspect);
  }

  private void updatePhotoView() {
    if (mPhotoFile == null || !mPhotoFile.exists()) {
      mPhotoView.setImageDrawable(null);
    } else {
      Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
      mPhotoView.setImageBitmap(bitmap);
    }
  }
}
