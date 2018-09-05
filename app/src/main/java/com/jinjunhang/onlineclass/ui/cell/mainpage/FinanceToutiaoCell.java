package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.FinanceToutiao;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

import org.w3c.dom.Text;

public class FinanceToutiaoCell extends BaseListViewCell {

    private FinanceToutiao mFinanceToutiao;
    private Activity mActivity;

    public FinanceToutiaoCell(Activity activity, FinanceToutiao toutiao) {
        super(activity);
        mActivity = activity;
        this.mFinanceToutiao = toutiao;
    }

    @Override
    public ViewGroup getView() {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jr_tiaotiao, null);

        TextView indexTextView = (TextView) view.findViewById(R.id.toutiaoIndex);
        TextView titleTextView = (TextView) view.findViewById(R.id.toutiaoTitle);
        ImageView tagImage = (ImageView)view.findViewById(R.id.indexImage);

        String indexStr = "";

        if (mFinanceToutiao.getIndex() == 0) {
            indexStr = "TOP 1";
            tagImage.setVisibility(View.INVISIBLE);
            indexTextView.setVisibility(View.VISIBLE);
        } else if (mFinanceToutiao.getIndex() == 1) {
            indexStr = "TOP 2";
            tagImage.setVisibility(View.INVISIBLE);
            indexTextView.setVisibility(View.VISIBLE);
        } else if (mFinanceToutiao.getIndex() == 2) {
            indexStr = "TOP 3";
            tagImage.setVisibility(View.INVISIBLE);
            indexTextView.setVisibility(View.VISIBLE);
        } else {
            indexStr = (mFinanceToutiao.getIndex() + 1) + "";
            tagImage.setVisibility(View.VISIBLE);
            indexTextView.setVisibility(View.INVISIBLE);
        }

        if (mFinanceToutiao.getIndex() > 2) {
            indexTextView.setTextColor( mActivity.getResources().getColor(R.color.black));
        } else {
            indexTextView.setTextColor( mActivity.getResources().getColor(R.color.red));
        }

        indexTextView.setText(indexStr);
        titleTextView.setText(mFinanceToutiao.getContent());

        titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mFinanceToutiao.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mFinanceToutiao.getLink());
                mActivity.startActivity(i);
            }
        });
        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
