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
    public int getItemViewType() {
        return BaseListViewCell.FINANCE_TOUTIAO_CELL;
    }

    private void setView(ViewHolder viewHolder) {
        viewHolder.titleTextView.setText(mFinanceToutiao.getContent());
        viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mFinanceToutiao.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mFinanceToutiao.getLink());
                mActivity.startActivity(i);
            }
        });
    }

    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jr_tiaotiao, null).findViewById(R.id.container);

            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);

            viewHolder. titleTextView =  convertView.findViewById(R.id.toutiaoTitle);
            viewHolder.tagImage =  convertView.findViewById(R.id.indexImage);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setView(viewHolder);
        return (RelativeLayout)convertView;
    }

    class ViewHolder {
        TextView titleTextView;
        ImageView tagImage;
    }
}
