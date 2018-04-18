package com.jinjunhang.onlineclass.ui.lib;

import android.content.Context;
import android.util.AttributeSet;

import com.ppi.emoji.EmojiTextView;

public class MyEmojiTextView extends EmojiTextView {

    public MyEmojiTextView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public MyEmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEmojiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
