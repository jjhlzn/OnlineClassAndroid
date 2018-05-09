package com.jinjunhang.onlineclass.ui.fragment.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.ServiceLinkManager;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.mainpage.BottomTabLayoutActivity;
import com.jinjunhang.onlineclass.ui.activity.search.SearchActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by jinjunhang on 2018/3/21.
 */

public class MainPageFragment extends BaseFragment {

    private  static final String TAG = LogHelper.makeLogTag(MainPageFragment.class);

    private Page rongziPage;

    private ImageButton kefuBtn;
    private ViewGroup rongziView;

    private boolean isIntied;
    private View v;

    @Override
    public void initView() {
        v = getActivity().getLayoutInflater().inflate(R.layout.frag_main, null, false);
        LogHelper.d(TAG, "v = " + v);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            initView();
        }

        rongziView = (ViewGroup)v.findViewById(R.id.frag_1);

        rongziPage = new Page(getActivity(), inflater, container, savedInstanceState, ExtendFunctionManager.RONGZI_TYPE);
        rongziView.addView(rongziPage.v);

        if (!isIntied) {
            changeActionBar();
            isIntied = true;
        }
        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        rongziPage.onStopHandler();
    }


    @Override
    public void onResume() {
        super.onResume();
        rongziPage.onStopHandler();
    }

    @Override
    public void changeActionBar() {
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity == null)
            return;

        LogHelper.d(TAG, "activity = " + activity);
        activity.getSupportActionBar().show();
        activity.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //删除actionbar下面的阴影
        activity.getSupportActionBar().setElevation(0);
        final View customView = activity.getLayoutInflater().inflate(R.layout.actionbar_main, null);
        activity.getSupportActionBar().setCustomView(customView);
        Toolbar parent =(Toolbar) customView.getParent();

        parent.setContentInsetsAbsolute(0, 0);

        setLightStatusBar(customView, activity);

        TextView searchField = (TextView) ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.search_edit_field);
        searchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                getContext().startActivity(i);
            }
        });


        kefuBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.kefu_btn);
        //searchBtn = (ImageButton)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.search_btn);

        kefuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, "客服")
                        .putExtra(WebBrowserActivity.EXTRA_URL, ServiceLinkManager.FunctionCustomerServiceUrl());
                getContext().startActivity(i);
            }
        });


        Utils.setNavigationBarMusicButton(getActivity());
    }

}




