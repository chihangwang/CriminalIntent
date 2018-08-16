package com.example.chihang.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
  private static final String EXTRA_ANSWER_IS_TRUE =
      CheatActivity.class.getCanonicalName() + ":isAnswerTrue";
  private static final String EXTRA_HAS_SHOWN =
      CheatActivity.class.getCanonicalName() + ":hasShown";

  private boolean mAnswerIsTrue;

  private Button mShowAnswer;
  private TextView mAnswerTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_cheat);

    mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

    mAnswerTextView = findViewById(R.id.answer_text_view);

    mShowAnswer = findViewById(R.id.show_answer_button);
    mShowAnswer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_HAS_SHOWN, true));

        if (mAnswerIsTrue) {
          mAnswerTextView.setText(R.string.true_button);
        } else {
          mAnswerTextView.setText(R.string.false_button);
        }
      }
    });
  }

  public static final Intent newIntent(Context ctx, boolean isTrue) {
    return new Intent(ctx, CheatActivity.class)
        .putExtra(EXTRA_ANSWER_IS_TRUE, isTrue);
  }

  public static final boolean wasAnswerShown(Intent intent) {
    return intent.getBooleanExtra(EXTRA_HAS_SHOWN, false);
  }
}
