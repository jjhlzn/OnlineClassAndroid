package com.jinjunhang.onlineclass.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.controller.SingleFragmentActivity;
import com.jinjunhang.framework.lib.LoadingAnimation;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerResponse;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.service.GetPagedQuestionsRequest;
import com.jinjunhang.onlineclass.service.GetPagedQuestionsResponse;
import com.jinjunhang.onlineclass.ui.activity.AskQuestionActivity;
import com.jinjunhang.onlineclass.ui.activity.album.AlbumDetailActivity;
import com.jinjunhang.onlineclass.ui.lib.BaseListViewOnItemClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 *
 */
public class QuestionsFragment extends BaseFragment implements
        AbsListView.OnScrollListener, PagableController.PagableErrorResponseHandler {

    private final static String TAG = LogHelper.makeLogTag(QuestionsFragment.class);

    private PagableController mPagableController;

    private ListView mListView;
    private LoadingAnimation mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fragment_pushdownrefresh;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mLoading = new LoadingAnimation(getActivity());
        mListView = (ListView) v.findViewById(R.id.listView);
        //去掉列表的分割线
        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mListView.setOnItemClickListener(new BaseListViewOnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //设置ActionBar的按钮
        View actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView();
        Button rightButton = (Button)actionBar.findViewById(R.id.actionbar_right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AskQuestionActivity.class);
                getActivity().startActivity(i);
            }
        });


        SmartRefreshLayout swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        Utils.setRefreshHeader(getActivity(), swipeRefreshLayout);
        //swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        //设置PagableController
        mPagableController = new PagableController(getActivity(), mListView);
        //LogHelper.d(TAG, "mAlbumType = " + mAlbumType.getName());
        mPagableController.setShowLoadCompleteTip(false);

        QuestionsAdapter adapter = new QuestionsAdapter(mPagableController, this, new ArrayList<Question>());
        mPagableController.setSwipeRefreshLayout(swipeRefreshLayout);
        mPagableController.setPagableArrayAdapter(adapter);
        mPagableController.setPagableRequestHandler(new QuestionListHanlder());
        mPagableController.setErrorResponseHanlder(this);
        mPagableController.setOnScrollListener(this);

        //((FrameLayout)v.findViewById(R.id.fragmentContainer)).addView(mPlayerController.getView());
        return v;
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //此方法不需要实现
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mPagableController.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }

    private class QuestionListHanlder implements PagableController.PagableRequestHandler {
        @Override
        public PagedServerResponse handle() {
            GetPagedQuestionsRequest request = new GetPagedQuestionsRequest();
            request.setPageIndex(mPagableController.getPageIndex());
            GetPagedQuestionsResponse response = (GetPagedQuestionsResponse)new BasicService().sendRequest(request);
            return response;
        }
    }


    @Override
    public void handle(PagedServerResponse resp) {

        Utils.showMessage(getActivity(), resp.getErrorMessage());

    }

    private void addAnswer(Answer answer) {
        for (Object obj : mPagableController.getPagableArrayAdapter().getDataSet()) {
            Question question = (Question)obj;
            if (question.getId().equals(answer.getQuestion().getId())) {
                question.getAnswers().add(answer);
                break;
            }
        }
    }
    public void refreshListView(Answer answer) {
        addAnswer(answer);
        mPagableController.getPagableArrayAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogHelper.d(TAG, "onActivityResult called");
        if (requestCode == QuestionAnswerFragment.REQUEST_QUESTION && data != null) {
            boolean isSuccess = data.getBooleanExtra(QuestionAnswerFragment.EXTRA_IS_SUCCESS, false);
            if (isSuccess) {
                Answer answer = (Answer)data.getSerializableExtra(QuestionAnswerFragment.EXTRA_ANSWER);
                if(answer != null)
                    refreshListView(answer);
            }
        }
    }

}


