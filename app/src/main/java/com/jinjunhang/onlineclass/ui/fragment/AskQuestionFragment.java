package com.jinjunhang.onlineclass.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.service.AskQuestionRequest;
import com.jinjunhang.onlineclass.service.AskQuestionResponse;
import com.jinjunhang.onlineclass.service.SendQuestionAnswerRequest;
import com.jinjunhang.onlineclass.service.SendQuestionAnswerResponse;

public class AskQuestionFragment extends BottomPlayerFragment    {
    private final static String TAG = LogHelper.makeLogTag(AskQuestionFragment.class);


    public static final int REQUEST_QUESTION = 1;

    private Button mSendButton;

    private EditText mEditText;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_comment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mEditText = (EditText)v.findViewById(R.id.content);
        mSendButton = (Button)((AppCompatActivity)getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionbar_right_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mEditText.getText().toString();
                if (str.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                AskQuestionRequest req = new AskQuestionRequest();
                req.setContent(str);
                new SendAskQuestionTask().execute(req);
            }
        });

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

    private class SendAskQuestionTask extends AsyncTask<AskQuestionRequest, Void, AskQuestionResponse> {
        protected AskQuestionResponse doInBackground(AskQuestionRequest... params) {
            AskQuestionRequest request = params[0];

            return new BasicService().sendRequest(request);
        }

        protected void onPostExecute(AskQuestionResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                return;
            }

            Utils.showMessage(getActivity(),"发送成功，需要审核后才能查看", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().onBackPressed();
                }
            });
            /*
            Intent intent = new Intent();
            intent.putExtra(EXTRA_ANSWER, mAnswer)
                  .putExtra(EXTRA_IS_SUCCESS, true);
            getActivity().setResult( REQUEST_QUESTION, intent);
            getActivity().finish(); */
        }

    }


}
