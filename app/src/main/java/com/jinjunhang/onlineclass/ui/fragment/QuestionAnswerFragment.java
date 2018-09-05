package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.service.GetAlbumsRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLansRequest;
import com.jinjunhang.onlineclass.service.GetZhuanLansResponse;
import com.jinjunhang.onlineclass.service.LikeQuestionRequest;
import com.jinjunhang.onlineclass.service.LikeQuestionResponse;
import com.jinjunhang.onlineclass.service.SendQuestionAnswerRequest;
import com.jinjunhang.onlineclass.service.SendQuestionAnswerResponse;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ZhuanLanInListCell;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerFragment extends BottomPlayerFragment    {
    private final static String TAG = LogHelper.makeLogTag(QuestionAnswerFragment.class);

    public static final String EXTRA_QUESTION = "EXTRA_QUESTION";
    public static final String EXTRA_TO_USER_ID = "EXTRA_TO_USER_ID";
    public static final String EXTRA_TO_USER_NAME = "EXTRA_TO_USER_NAME";
    public static final String EXTRA_IS_SUCCESS = "EXTRA_IS_SUCCESS";
    public static final String EXTRA_ANSWER = "EXTRA_ANSWER";
    public static final int REQUEST_QUESTION = 1;

    private Button mSendButton;
    private Button mCloseButton;

    private Question mQuestion;
    private Answer mAnswer;
    private String mToUserId;
    private String mToUserName;
    private EditText mEditText;

    public void setButtonClickListener(Button closeButton, Button sendButton) {
        this.mSendButton= sendButton;
        this.mCloseButton = closeButton;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mEditText.getText().toString();
                if (str.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "回复不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAnswer = new Answer();
                mAnswer.setQuestion(mQuestion);
                mAnswer.setContent(str);
                mAnswer.setFromUserName("我");
                mAnswer.setToUserName(mToUserName);
                mAnswer.setToUserId(mToUserId);
                SendQuestionAnswerRequest req = new SendQuestionAnswerRequest();
                req.setQuestion(mQuestion);
                req.setContent(str);
                req.setToUser(mToUserId);
                new SendQuestionAnswerTask().execute(req);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_question_comment, container, false);

        mEditText = (EditText)v.findViewById(R.id.content);

        mQuestion = (Question) getActivity().getIntent().getSerializableExtra(EXTRA_QUESTION);
        mToUserId = getActivity().getIntent().getStringExtra(EXTRA_TO_USER_ID);
        mToUserName = getActivity().getIntent().getStringExtra(EXTRA_TO_USER_NAME);

        if (mToUserName != null && !mToUserName.isEmpty()) {
            mEditText.setHint("回复"+mToUserName+"：内容限制在200字以内");
        } else {
            mEditText.setHint("回复内容限制在200字以内");
        }



        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = mEditText.getText().toString();
                if (mSendButton != null) {
                    if (str.isEmpty()) {
                        mSendButton.setEnabled(false);
                        mSendButton.setTextColor(getActivity().getResources().getColor(R.color.ccl_grey600));
                    } else {
                        mSendButton.setEnabled(true);
                        mSendButton.setTextColor(getActivity().getResources().getColor(R.color.tab_selected_color));
                    }
                }
            }
        });
        return v;
    }

    private class SendQuestionAnswerTask extends AsyncTask<SendQuestionAnswerRequest, Void, SendQuestionAnswerResponse> {
        protected SendQuestionAnswerResponse doInBackground(SendQuestionAnswerRequest... params) {
            SendQuestionAnswerRequest request = params[0];

            return new BasicService().sendRequest(request);
        }

        protected void onPostExecute(SendQuestionAnswerResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());

                return;
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_ANSWER, mAnswer)
                  .putExtra(EXTRA_IS_SUCCESS, true);
            getActivity().setResult( REQUEST_QUESTION, intent);
            getActivity().finish();
        }

    }


}
