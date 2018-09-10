package com.jinjunhang.onlineclass.ui.activity;

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
import com.jinjunhang.onlineclass.ui.fragment.AskQuestionFragment;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;

public class AskQuestionActivity extends BaseMusicSingleFragmentActivity {
    private final static String TAG = LogHelper.makeLogTag(AskQuestionActivity.class);


    @Override
    protected Fragment createFragment() {
        return new AskQuestionFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "提问";
    }


    protected void createActionBar() {
        AppCompatActivity activity = this;
        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_ask_question, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();


        parent.setContentInsetsAbsolute(0, 0);

        Utils.setLightStatusBar(customView, activity);
    }


}