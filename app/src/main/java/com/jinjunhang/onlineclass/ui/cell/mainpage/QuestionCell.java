package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.service.GetQuestionRequest;
import com.jinjunhang.onlineclass.service.GetQuestionResponse;
import com.jinjunhang.onlineclass.service.LikeQuestionRequest;
import com.jinjunhang.onlineclass.service.LikeQuestionResponse;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;
import com.jinjunhang.onlineclass.ui.activity.MainActivity;
import com.jinjunhang.onlineclass.ui.activity.QuestionAnswerActivity;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.QuestionAnswerFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QuestionCell extends BaseListViewCell implements Serializable {

    private final static String TAG = LogHelper.makeLogTag(QuestionCell.class);

    private Question mQuestion;
    private Activity mActivity;
    private ArrayAdapter mAdapter;
    private Fragment mFragment;

    public QuestionCell(Activity activity, Question question, Fragment fragment, ArrayAdapter adapter) {
        super(activity);
        mActivity = activity;
        this.mQuestion = question;
        this.mFragment = fragment;
        this.mAdapter = adapter;
    }

    @Override
    public int getItemViewType() {
        return BaseListViewCell.QUESTION_CELL;
    }

    @Override
    public ViewGroup getView(View convertView) {
        return updateView(convertView);
    }

    private void setView(ViewHolder viewHolder) {


        if (mQuestion.isLiked()) {
            viewHolder.thumbImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.thumb_s));
        } else {
            viewHolder.thumbImage.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.thumb));
        }

        viewHolder.userNameTV.setText(mQuestion.getUserName());
        viewHolder.dateTV.setText(mQuestion.getTime());
        viewHolder.contentTV.setText(mQuestion.getContent());
        viewHolder.answerCountTV.setText(mQuestion.getAnswerCount() + "");
        viewHolder.thumbCountTV.setText(mQuestion.getThumbCount() + "");

        viewHolder.thumbImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHelper.d(TAG, "thumbImage clicked");
                LikeQuestionRequest request = new LikeQuestionRequest();
                request.setQuestionId(mQuestion.getId());
                new LikeQuestionTask().execute(request);

            }
        });

        viewHolder.answerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogHelper.d(TAG, "answerImage clicked");
                Intent i = new Intent(mActivity, QuestionAnswerActivity.class)
                        .putExtra(QuestionAnswerFragment.EXTRA_QUESTION, mQuestion)
                        .putExtra(QuestionAnswerFragment.EXTRA_TO_USER_ID, "")
                        .putExtra(QuestionAnswerFragment.EXTRA_TO_USER_NAME, "");
                mFragment.startActivityForResult(i, QuestionAnswerFragment.REQUEST_QUESTION);
            }
        });
    }

    private void setView(final  ViewHolder viewHolder, View convertView) {
        setView(viewHolder);
        Glide.with(mActivity)
                .load(ServiceConfiguration.GetUserProfileImage(mQuestion.getUserId()))
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(new BitmapImageViewTarget(viewHolder.userImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mActivity.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        viewHolder.userImage.setImageDrawable(circularBitmapDrawable);
                    }
                });



        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Answer item = mQuestion.getAnswers().get(i);
                Intent intent = new Intent(mActivity, QuestionAnswerActivity.class)
                        .putExtra(QuestionAnswerFragment.EXTRA_QUESTION, mQuestion)
                        .putExtra(QuestionAnswerFragment.EXTRA_TO_USER_ID, item.getFromUserId())
                        .putExtra(QuestionAnswerFragment.EXTRA_TO_USER_NAME, item.getFromUserName());
                mFragment.startActivityForResult(intent, QuestionAnswerFragment.REQUEST_QUESTION);
            }
        });


        if (mQuestion.getAnswers().size() > 0) {
            List<ListViewCell> cells = new ArrayList<>();
            for (Answer answer : mQuestion.getAnswers()) {
                AnswerCell answerCell = new AnswerCell(mActivity, answer);
                cells.add(answerCell);
            }
            AnswerAdapter adapter = new AnswerAdapter(mActivity, cells);
            viewHolder.listView.setAdapter(adapter);
        } else {
            viewHolder.listView.setVisibility(View.INVISIBLE);
            ((ViewGroup)convertView).removeView(viewHolder.listView);
        }
    }

    private ViewGroup updateView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_question, null).findViewById(R.id.container);

            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder.userNameTV = (TextView) convertView.findViewById(R.id.comment_username);
            viewHolder. dateTV = (TextView) convertView.findViewById(R.id.comment_date);
            viewHolder. contentTV = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder. answerCountTV = (TextView) convertView.findViewById(R.id.answerCountText);
            viewHolder. thumbCountTV = (TextView) convertView.findViewById(R.id.thumbCountText);

            viewHolder. thumbImage = (ImageView) convertView.findViewById(R.id.thumbIcon);
            viewHolder. answerImage = (ImageView) convertView.findViewById(R.id.answerIcon);


            viewHolder.listView = convertView.findViewById(R.id.answerListView);
            viewHolder.userImage = convertView.findViewById(R.id.comment_user_image);
            //去掉列表的分割线
            viewHolder.listView.setDividerHeight(0);
            viewHolder.listView.setDivider(null);

            viewHolder.userImage.setOval(true);
            //userImage.setBorderWidth(0.5f);
            //serImage.setBorderColor(mActivity.getResources().getColor(R.color.ccl_grey600));

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setView(viewHolder, convertView);
        return (ViewGroup) convertView;
    }



    private class AnswerAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mCells;

        public AnswerAdapter(Activity activity, List<ListViewCell> cells) {
            super(activity, 0, cells);
            mCells = cells;
        }

        @Override
        public int getCount() {
            return mCells.size();
        }

        @Override
        public ListViewCell getItem(int position) {
            return mCells.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewCell item = getItem(position);
            return item.getView();
        }
    }

    private class LikeQuestionTask extends AsyncTask<LikeQuestionRequest, Void, LikeQuestionResponse> {
        protected LikeQuestionResponse doInBackground(LikeQuestionRequest... params) {
            LikeQuestionRequest request = params[0];

            return new BasicService().sendRequest(request);
        }

        protected void onPostExecute(LikeQuestionResponse response) {
            super.onPostExecute(response);

            if (!response.isSuccess()){
                LogHelper.e(TAG, response.getStatus(), response.getErrorMessage());
                Toast.makeText(mActivity, response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mQuestion.isLiked()) {
                mQuestion.setLiked(false);
                mQuestion.setThumbCount(mQuestion.getThumbCount() - 1);
                mAdapter.notifyDataSetChanged();
            } else {
                mQuestion.setLiked(true);
                mQuestion.setThumbCount(mQuestion.getThumbCount() + 1);
                mAdapter.notifyDataSetChanged();
            }

        }

    }

    class ViewHolder {
        ListView listView;
        RoundedImageView userImage;
        TextView userNameTV;
        TextView dateTV;
        TextView contentTV;
        TextView answerCountTV;
        TextView thumbCountTV;

        ImageView thumbImage;
        ImageView answerImage;

    }

}
