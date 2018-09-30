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
    private View view;

    public FinanceToutiaoCell(Activity activity, FinanceToutiao toutiao) {
        super(activity);
        mActivity = activity;
        this.mFinanceToutiao = toutiao;
    }

    @Override
    public ViewGroup getView() {
        if (view == null) {
            view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jr_tiaotiao, null);


            TextView titleTextView = (TextView) view.findViewById(R.id.toutiaoTitle);
            ImageView tagImage = (ImageView) view.findViewById(R.id.indexImage);

            String indexStr = "";

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
        }
        return (RelativeLayout)view.findViewById(R.id.container);
    }

}
