package com.jinjunhang.onlineclass.ui.fragment.search;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.onlineclass.R;

/**
 * Created by jinjunhang on 2018/4/4.
 */

public class SearchPage {

    public View view;

    public SearchPage(FragmentActivity activity, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_fragment_search_frag1, container, false);

    }
}
