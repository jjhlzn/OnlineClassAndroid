package com.jinjunhang.onlineclass.ui.fragment.search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsRequest;
import com.jinjunhang.onlineclass.service.GetHotSearchWordsResponse;
import com.jinjunhang.onlineclass.ui.activity.search.SearchResultActivity;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.framework.lib.LogHelper;

import java.util.List;

/**
 * Created by lzn on 16/6/28.
 */
public class SearchFragment extends BaseFragment {

    private static final String TAG = LogHelper.makeLogTag(SearchFragment.class);
    private LinearLayout mKeywordsContainer;
    private List<String> mKeywords;

    private EditText mSearchField;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogHelper.d(TAG, "onCreateView called");
        View v = inflater.inflate(R.layout.activity_fragment_search, container, false);
        mKeywordsContainer = (LinearLayout) v.findViewById(R.id.hot_search_keywords);

        mSearchField = (EditText) ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.search_edit_field);
        final View background = v.findViewById(R.id.background);
        final View hotKeywordsContainer = v.findViewById(R.id.hot_search_keywords);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(getActivity());
                v.requestFocus();
            }
        });

        mSearchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    //显示背景
                    background.setVisibility(View.VISIBLE);
                }else {
                    background.setVisibility(View.INVISIBLE);
                }
            }
        });

        mSearchField.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            mSearchField.clearFocus();
                            Utils.hideSoftKeyboard(getActivity());
                            performSearch();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        if (mKeywords == null) {
            new GetHotSearchWordsTask().execute();
        } else {
            makeKeywordButtons();
        }

        mSearchField.clearFocus();
        if (mSearchField.isFocused()) {
            background.setVisibility(View.INVISIBLE);
        }
        LogHelper.d(TAG, "searchField.isFocused = " + mSearchField.isFocused());
        return v;
    }


    private void performSearch() {
        Intent i = new Intent(getActivity(), SearchResultActivity.class)
                .putExtra(SearchResultFragment.EXTRA_KEYWORD, mSearchField.getText().toString());
        startActivity(i);
    }


    private void makeKeywordButtons() {
        for (final String keyword : mKeywords) {

            View v = getActivity().getLayoutInflater().inflate(R.layout.hotkeyword_button, null);
            Button button = (Button) v.findViewById(R.id.keyword_button);
            button.setText(keyword);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSearchField.setText(keyword);
                    performSearch();

                }
            });
            mKeywordsContainer.addView(v);
        }
    }


    private class GetHotSearchWordsTask extends AsyncTask<Void, Void, GetHotSearchWordsResponse> {

        @Override
        protected GetHotSearchWordsResponse doInBackground(Void... params) {
            GetHotSearchWordsRequest req = new GetHotSearchWordsRequest();
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetHotSearchWordsResponse resp) {
            super.onPostExecute(resp);
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                LogHelper.e(TAG, resp.getErrorMessage());
                return;
            }

            mKeywords = resp.getKeywords();
            makeKeywordButtons();
        }
    }



}
