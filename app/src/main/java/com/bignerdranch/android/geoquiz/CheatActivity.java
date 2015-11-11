package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String EXTRA_QUESTION_WAS_CHEATED = "com.bignerdranch.android.geoquiz.question_was_cheated";
    private static final String PLAYER_CHEATED = "player cheated";
    private static final String QUIZ_ANSWER = "quiz answer";
    private static final String TAG = CheatActivity.class.getSimpleName();

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private TextView mApiVersion;
    private Button mShowAnswer;

    //to keep track whether or not hte answer was shown
    private boolean mAnswerWasShown = false;

    //to keep track whether or not the question was cheated on
    private boolean mQuestionWasCheated = false;
    private static int mCheatedQuestion;

    // Making a method that encapsulates putting an extra into a new intent
    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    // For passing the data back to the main activity
    private void setAnswerShownResult (boolean isAnswerShown, int cheatedQuestion){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        //TODO: int cheatedQuestion should be the resID (should reference mCheatedQuestons variable)
        data.putExtra(EXTRA_QUESTION_WAS_CHEATED, cheatedQuestion);
        setResult(RESULT_OK, data);
    }

    // Method for showing the result that QuizActivity can use
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    public static int questionWasCheated(Intent result){
        //TODO: pass back the resID that you get from Quiz Activity. mCheatedQuestion becomes a member variable, assigned by the extra we get from quizactiity
        return result.getIntExtra(EXTRA_QUESTION_WAS_CHEATED, mCheatedQuestion);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        //TODO: should be a call to getExtra from the intent received from quiz activity
        mCheatedQuestion = QuizActivity.currentQuestion;

        mApiVersion = (TextView) findViewById(R.id.api_version_text_view);
        mApiVersion.setText("API level " + Build.VERSION.SDK);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        // Retrieves value from the extra
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        //restoring whther the palyer cheated and the answer
        if (savedInstanceState != null) {
            mAnswerWasShown = savedInstanceState.getBoolean(PLAYER_CHEATED,false);
            mAnswerIsTrue = savedInstanceState.getBoolean(QUIZ_ANSWER,false);

            if (mAnswerWasShown) {
                showAnswer();
            }
        }

        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
                setAnswerShownResult(true, mCheatedQuestion);
                mAnswerWasShown = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mShowAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mShowAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void showAnswer(){
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //saving whether the player cheated and answer is true
        savedInstanceState.putBoolean(PLAYER_CHEATED, mAnswerWasShown);
        savedInstanceState.putBoolean(QUIZ_ANSWER,mAnswerIsTrue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
