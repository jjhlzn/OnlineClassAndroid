package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;


/**
 * Created by jinjunhang on 2018/3/21.
 */

public class MainPageFragment extends BaseFragment {

    private  static final String TAG = LogHelper.makeLogTag(MainPageFragment.class);

    private Page rongziPage;
    private Page touziPage;

    private ImageButton touziBtn;
    private ImageButton rongziBtn;
    private ViewGroup rongziView;
    private ViewGroup touziView;

    private boolean isrongziSelected = true;

    private View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_main, container, false);

        rongziView = (ViewGroup)v.findViewById(R.id.frag_1);
        touziView = (ViewGroup)v.findViewById(R.id.frag_2);

        rongziPage = new Page(getActivity(), inflater, container, savedInstanceState);
        rongziView.addView(rongziPage.v);
        touziPage = new Page(getActivity(), inflater, container, savedInstanceState);
        touziView.addView(touziPage.v);

        changeActionBar();
        return v;
    }

    @Override
    public void changeActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity == null)
            return;

        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);

        setLightStatusBar(customView, activity);

        touziBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.touzi_btn);
        rongziBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.rongzi_btn);

        touziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn_s));
                rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn));
                rongziView.setVisibility(View.VISIBLE);
                touziView.setVisibility(View.INVISIBLE);
                isrongziSelected = false;

            }
        });

        rongziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn));
                rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn_s));
                rongziView.setVisibility(View.INVISIBLE);
                touziView.setVisibility(View.VISIBLE);
                isrongziSelected = true;
            }
        });

        LogHelper.d(TAG, "isrongziSelected = " + isrongziSelected);
        if (isrongziSelected) {
            touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn));
            rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn_s));

        } else {
            touziBtn.setImageDrawable(getContext().getDrawable(R.drawable.touzi_btn_s));
            rongziBtn.setImageDrawable(getContext().getDrawable(R.drawable.rongzi_btn));
        }
    }

}




