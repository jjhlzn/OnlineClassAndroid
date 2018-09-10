package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Answer;
import com.jinjunhang.onlineclass.model.ZhuanLan;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

public class AnswerCell extends BaseListViewCell {

    private Answer mAnswer;

    public AnswerCell(Activity activity, Answer answer) {
        super(activity);
        mAnswer = answer;
    }

    @Override
    public ViewGroup getView() {

        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_answer, null);

        TextView contentView = (TextView) v.findViewById(R.id.answerContent);


        String content = "";
        if (mAnswer.isFromManager()) {
            content = "<font color=red>"+mAnswer.getFromUserName() +"</font> ";
        } else {
            content = "<font color=#707070>"+mAnswer.getFromUserName() +"</font> ";
        }

        if (mAnswer.getToUserName() != null && !"".equals(mAnswer.getToUserName())) {
            content += " 回复 " + "<font color=#707070>"+mAnswer.getToUserName() +"</font> " ;
        }

        content += "：";
        content += mAnswer.getContent();

        contentView.setText(Html.fromHtml(content));

        return (RelativeLayout)v.findViewById(R.id.container);
    }

}
