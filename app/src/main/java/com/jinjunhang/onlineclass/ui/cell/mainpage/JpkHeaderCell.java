package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.activity.ZhuanLanListActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;
import com.jinjunhang.onlineclass.ui.fragment.ZhuanLanListFragment;

public class JpkHeaderCell extends BaseListViewCell {

    public JpkHeaderCell(Activity activity) {
        super(activity);
    }



    @Override
    public int getItemViewType() {
        return BaseListViewCell.JPK_HEADER_CELL;
    }

    @Override
    public ViewGroup getView(View convertView) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_jpk_header, null).findViewById(R.id.container);
            Button btn = (Button) convertView.findViewById(R.id.viewAllBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mActivity, ZhuanLanListActivity.class)
                            .putExtra(ZhuanLanListFragment.EXTRA_TYPE, ZhuanLanListFragment.TYPE_JPK);
                    mActivity.startActivity(i);
                }
            });
        }
        return (ViewGroup)convertView;
    }

    @Override
    public ViewGroup getView() {
        return this.getView(null);
    }


}
