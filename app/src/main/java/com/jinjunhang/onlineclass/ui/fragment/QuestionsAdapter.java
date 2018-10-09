package com.jinjunhang.onlineclass.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.controller.PagableController;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Album;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.model.Question;
import com.jinjunhang.onlineclass.ui.cell.mainpage.QuestionCell;

import java.util.List;

/**
 * Created by lzn on 16/6/29.
 */
public class QuestionsAdapter extends PagableController.PagableArrayAdapter<Question> {
    private final String TAG = LogHelper.makeLogTag(QuestionsAdapter.class);
    private Activity mActivity;
    private Fragment mFragment;

    public QuestionsAdapter(PagableController pagableController, Fragment fragment, List<Question> dataSet){
        super(pagableController, dataSet);
        mActivity = pagableController.getActivity();
        mFragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = getItem(position);
        QuestionCell cell = new QuestionCell(mActivity, question, mFragment,  this);
        convertView = cell.getView(convertView);
        return convertView;
    }
}