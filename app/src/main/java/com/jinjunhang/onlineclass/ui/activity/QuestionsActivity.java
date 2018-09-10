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
import android.widget.ImageButton;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;
import com.jinjunhang.onlineclass.ui.fragment.QuestionsFragment;

public class QuestionsActivity extends BaseMusicSingleFragmentActivity {
    private final static String TAG = LogHelper.makeLogTag(QuestionsActivity.class);

    private QuestionsFragment mQuestionsFragment;

    @Override
    protected Fragment createFragment() {
        mQuestionsFragment =  new QuestionsFragment();
        return mQuestionsFragment;
    }

    @Override
    protected String getActivityTitle() {
        return "问答";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void createActionBar() {
        AppCompatActivity activity = this;
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_questionlist, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);
        Utils.setLightStatusBar(customView, activity);

    }
}