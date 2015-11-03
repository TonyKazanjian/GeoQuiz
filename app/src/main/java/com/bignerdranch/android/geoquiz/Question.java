package com.bignerdranch.android.geoquiz;

/**
 * Created by tonyk_000 on 10/2/2015.
 */
public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsCheatedOn;

    public Question (int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public boolean isCheatedOn() {return mIsCheatedOn = true; }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public void setCheatedOn(boolean cheatedOn){
        mIsCheatedOn = cheatedOn;
    }
}
