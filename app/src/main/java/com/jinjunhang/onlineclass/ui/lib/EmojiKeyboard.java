package com.jinjunhang.onlineclass.ui.lib;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.data5tream.emojilib.EmojiParser;
import com.jinjunhang.onlineclass.R;
import com.ppi.emoji.EmojiTextView;
import com.ppi.emoji.emojiParser;

/**
 * Created by jjh on 2016-7-3.
 */
public class EmojiKeyboard {
    private Activity mContext;

    public EmojiKeyboard(Activity context) {
        mContext = context;
    }

    private String[] emojiKeys = new String[]{
            ":smile:", ":smiley:", ":kissing_smiling_eyes:", ":heart_eyes:", ":heart:", ":heartpulse:", ":sparkling_heart:",
            ":star:", ":fire:", ":thumbsup:", ":ok_hand:", ":v:", ":clap:", ":muscle:",
            ":kiss:", ":ox:", ":coffee:", ":tea:", ":beers:", ":100:", ":lollipop:",
            ":tangerine:", ":watermelon:", ":lemon:", ":strawberry:", ":pear:", ":tomato:", ":apple:",
            ":cherries:", ":peach:", ":pineapple:", ":sunny:", ":sunflower:", ":rose:", ":hibiscus:"};

    public View getView() {
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);

        ViewGroup.LayoutParams params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // Changes the height and width to the specified *pixels*
        params.height = 213 * 3;
        layout.setLayoutParams(params);

        int rows = (emojiKeys.length + 6) / 7;
        int index = 0;
        for (int i = 0; i < rows; i++) {
            LinearLayout rowView = createRow();
            for(int j = 0; j < 7; j++) {
                TextView textView = (TextView) rowView.findViewById(getEmojiTextViewId(rowView, j));
               // textView.setText(emojiParser.demojizedText(emojiKeys[index]));
                textView.setText(EmojiParser.parseEmojis(emojiKeys[index]));
                index++;
            }
            layout.addView(rowView);
        }
        return layout;
    }

    private LinearLayout createRow() {
        LinearLayout layout = (LinearLayout) mContext.getLayoutInflater().inflate(R.layout.emojikeyboard_row, null).findViewById(R.id.root_container);
        return layout;
    }

    private int  getEmojiTextViewId(LinearLayout row, int column) {
        switch (column) {
            case 0:
                return R.id.key0;
            case 1:
                return R.id.key1;
            case 2:
                return R.id.key2;
            case 3:
                return R.id.key3;
            case 4:
                return R.id.key4;
            case 5:
                return R.id.key5;
            case 6:
                return R.id.key6;
        }
        //should not catch here
        throw new RuntimeException("wrong column, column = " + column);
    }


}
