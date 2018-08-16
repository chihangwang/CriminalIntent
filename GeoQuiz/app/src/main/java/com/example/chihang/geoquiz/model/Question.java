package com.example.chihang.geoquiz.model;

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;

    public Question(int mTextResId, boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
        this.mTextResId = mTextResId;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }
}
