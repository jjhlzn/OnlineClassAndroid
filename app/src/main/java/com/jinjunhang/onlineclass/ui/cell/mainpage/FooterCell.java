package com.jinjunhang.onlineclass.ui.cell.mainpage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.service.GetFooterAdvsResponse;
import com.jinjunhang.onlineclass.ui.activity.WebBrowserActivity;
import com.jinjunhang.onlineclass.ui.cell.BaseListViewCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjunhang on 17/1/6.
 */

public class FooterCell extends BaseListViewCell {

    private static final String TAG = LogHelper.makeLogTag(FooterCell.class);

    public FooterCell(Activity activity) {super(activity);}

    private List<GetFooterAdvsResponse.FooterAdv> mAdvs = new ArrayList<>();

    public void setAdvs(List<GetFooterAdvsResponse.FooterAdv> advs) {
        mAdvs = advs;
    }

    @Override
    public ViewGroup getView() {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_mainpage_footer, null);

        ImageView image1 = (ImageView) view.findViewById(R.id.list_mainpage_footer_image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.list_mainpage_footer_image2);
        ImageView image3 = (ImageView) view.findViewById(R.id.list_mainpage_footer_image3);
        ImageView image4 = (ImageView) view.findViewById(R.id.list_mainpage_footer_image4);

        if (mAdvs.size() == 4) {
            setImageView(mAdvs.get(0), image1);
            setImageView(mAdvs.get(1), image2);
            setImageView(mAdvs.get(2), image3);
            setImageView(mAdvs.get(3), image4);
        }

        return (LinearLayout)view.findViewById(R.id.list_item_viewgroup);
    }

    private void setImageView(final GetFooterAdvsResponse.FooterAdv adv, ImageView imageView) {
        Glide.with(mActivity).load(adv.getImageUrl()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            LogHelper.d(TAG, "adv.url : " + adv.getUrl());
            //快速下卡的链接在外部浏览器打开
            if (!"".equals(adv.getUrl()) && "快速下卡".equals(adv.getTitle())) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(adv.getUrl()));
                mActivity.startActivity(intent);
            } else {
                Intent i = new Intent(mActivity, WebBrowserActivity.class)
                        .putExtra(WebBrowserActivity.EXTRA_TITLE, adv.getTitle())
                        .putExtra(WebBrowserActivity.EXTRA_URL, adv.getUrl());
                mActivity.startActivity(i);
            }
            }
        });
    }

}
