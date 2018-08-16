package com.example.chihang.geoquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chihang.geoquiz.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
  private static final String TAG = QuizActivity.class.getName();
  private static final int REQUEST_CODE_CHEAT = 0;

  private Button mTrueButton;
  private Button mFalseButton;
  private Button mNextButton;
  private Button mCheatButton;
  private TextView mQuestionTextView;

  private Question currentQuestion = next();

  private int mCurrentIndex = 0;
  private boolean mIsCheater = false;
  private static final Question[] QUESTIONS = new Question[] {
      new Question(R.string.q_1, true),
      new Question(R.string.q_2, false),
      new Question(R.string.q_3, false),
      new Question(R.string.q_4, true),
      new Question(R.string.q_5, true)
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate(Bundle) called");
    setContentView(R.layout.activity_quiz);

    mQuestionTextView = findViewById(R.id.question_text_view);
    mQuestionTextView.setText(currentQuestion.getTextResId());

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mIsCheater = false;
        updateQuestion();
      }
    });

    mTrueButton = findViewById(R.id.true_button);
    mTrueButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(true);
      }
    });

    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(false);
      }
    });

    mCheatButton = findViewById(R.id.cheat_button);
    mCheatButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = CheatActivity.newIntent(QuizActivity.this, currentQuestion.isAnswerTrue());
        startActivityForResult(intent, REQUEST_CODE_CHEAT);
      }
    });
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != RESULT_OK) {
      return;
    }

    if (requestCode == REQUEST_CODE_CHEAT) {
      if (data == null) {
        return;
      }
      mIsCheater = CheatActivity.wasAnswerShown(data);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.d(TAG, "onStart() called");
  }

  @Override
  public void onPause() {
    super.onPause();
    Log.d(TAG, "onPause called");
  }

  @Override
  public void onResume() {
    super.onResume();
    Log.d(TAG, "onResume() called");
  }

  @Override
  public void onStop() {
    super.onStop();
    Log.d(TAG, "onStop() called");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy() called");
  }

  private void updateQuestion() {
    mQuestionTextView.setText(next().getTextResId());
  }

  private void checkAnswer(boolean isTrue) {
    int messageId;

    if (mIsCheater) {
      messageId = R.string.judgement_toast;
    } else {
      if (isTrue == currentQuestion.isAnswerTrue()) {
        messageId = R.string.correct_toast;
      } else {
        messageId = R.string.false_button;
      }
    }

    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show();
  }

  private Question next() {
    currentQuestion = QUESTIONS[mCurrentIndex++ % QUESTIONS.length];
    return currentQuestion;
  }
}
