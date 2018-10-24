package com.jinjunhang.onlineclass.ui.cell;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Message;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;

public class MessageCell extends BaseListViewCell {

    private Message mMessage;

    public MessageCell(Activity activity, Message message) {
        super(activity);
        this.mMessage = message;
    }

    @Override
    public ViewGroup getView(View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_message, null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
            viewHolder.titleLabel = convertView.findViewById(R.id.title);
            viewHolder.detailLabel = (TextView) convertView.findViewById(R.id.detail);
            viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        setView(viewHolder);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, mMessage.getClickTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, mMessage.getClickUrl());
                mActivity.startActivity(i);
            }
        });
        return (RelativeLayout) convertView;
    }

    private void setView(ViewHolder viewHolder) {
        viewHolder.timeLabel.setText(mMessage.getTitle());
        viewHolder.detailLabel.setText(mMessage.getDetail());
        viewHolder.timeLabel.setText(mMessage.getTime());
    }

    class ViewHolder {
        private TextView titleLabel;
        private TextView detailLabel;
        private TextView timeLabel;
    }
}
