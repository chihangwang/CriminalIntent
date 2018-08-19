package com.chihang.criminalintent.model;

import java.util.UUID;

public class Crime {
  private UUID mId;
  private String mTitle;

  public Crime() {
    mId = UUID.randomUUID();
  }

  public void setTitle(String title) {
    mTitle = title;
  }

  public UUID getId() {

    return mId;
  }

  public String getTitle() {
    return mTitle;
  }
}
