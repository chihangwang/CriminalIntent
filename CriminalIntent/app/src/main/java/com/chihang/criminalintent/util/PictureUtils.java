package com.chihang.criminalintent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
  public static Bitmap getScaledBitmap(String path, Activity activity) {
    Point size = new Point();
    // get the size of the screen so that we can later downsize the image to fit within the screen
    activity.getWindowManager().getDefaultDisplay().getSize(size);

    return getScaledBitmap(path, size.x, size.y);
  }

  public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
    // Read in the dimension of the image on the disk
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options); // TODO(ang) somehow the decodeFile fails...

    float srcWidth = options.outWidth;
    float srcHeight = options.outHeight;

    int inSampleSize = 1;
    if (srcWidth > destWidth || srcHeight > destHeight) {
      if (srcWidth > srcHeight) {
        inSampleSize = Math.round(srcHeight / destHeight);
      } else {
        inSampleSize = Math.round(srcWidth / destWidth);
      }
    }

    options = new BitmapFactory.Options();
    options.inSampleSize = inSampleSize;

    return BitmapFactory.decodeFile(path, options);
  }
}
