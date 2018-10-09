package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.QuestionsActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class QuestionHeaderCell extends BaseListViewCell {


    public QuestionHeaderCell(Activity activity) {
        super(activity);
    }

    @Override
    public ViewGroup getView(View convertView) {
        return this.getView();
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_question_header, null);

        Button viewAllBtn = (Button) view.findViewById(R.id.viewAllBtn);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, QuestionsActivity.class);
                mActivity.startActivity(i);
            }
        });

        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
