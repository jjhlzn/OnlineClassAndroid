package com.jinjunhang.onlineclass.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;
import com.jinjunhang.onlineclass.ui.fragment.ZhuanLanListFragment;

public class QuestionAnswerActivity extends BaseMusicSingleFragmentActivity {
    private final static String TAG = LogHelper.makeLogTag(QuestionAnswerActivity.class);

    private QuestionAnswerFragment mQuestionAnswerFragment;

    @Override
    protected Fragment createFragment() {
        mQuestionAnswerFragment =  new QuestionAnswerFragment();
        return mQuestionAnswerFragment;
    }

    @Override
    protected String getActivityTitle() {
        return "回复";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar();
    }

    public void setActionBar() {
        AppCompatActivity activity = this;
        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_question, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        Button leftButton = (Button)customView.findViewById(R.id.actionbar_left_button);
        Button rightButton = (Button)customView.findViewById(R.id.actionbar_right_button);
        mQuestionAnswerFragment.setButtonClickListener(leftButton, rightButton);


        parent.setContentInsetsAbsolute(0, 0);

        Utils.setLightStatusBar(customView, activity);
    }


}