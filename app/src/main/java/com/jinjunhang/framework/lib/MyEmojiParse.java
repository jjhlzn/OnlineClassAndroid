package com.jinjunhang.framework.lib;

import com.github.data5tream.emojilib.EmojiParser;
import com.github.data5tream.emojilib.emoji.Map;
import com.jinjunhang.player.utils.LogHelper;

import java.util.Arrays;

/**
 * Created by jjh on 2016-7-8.
 */
public class MyEmojiParse extends EmojiParser {
    private static final String TAG = "CommonSongFragment";

    /**
     * Converts unicode characters into Cheat Sheet codes
     *
     * @param text String containing unicode formatted emojis
     * @return String containing the Cheat Sheet codes of the emojis
     */
    public static String convertToCheatCode(String text){

        String returnString = text;

        char[] charArray = text.toCharArray();

        LogHelper.d(TAG, "charArray.length = " + charArray.length);

        for (int i = 0; i <= charArray.length - 2; i++) {
            String testString = new String(Arrays.copyOfRange(charArray, i, i+2));
            int test = testString.codePointAt(0);
            if (Map.emojiMap.containsKey(test)) {
                returnString = returnString.replace(testString, Map.emojiMap.get(test));
            }
        }

        return returnString;
    }
}
