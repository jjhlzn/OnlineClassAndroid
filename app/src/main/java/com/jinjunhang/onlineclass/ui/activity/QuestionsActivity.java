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
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;
import com.jinjunhang.onlineclass.ui.fragment.QuestionsFragment;

public class QuestionsActivity extends BaseMusicSingleFragmentActivity {
    private final static String TAG = LogHelper.makeLogTag(QuestionsActivity.class);

    @Override
    protected Fragment createFragment() {

        return new QuestionsFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "问答";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}