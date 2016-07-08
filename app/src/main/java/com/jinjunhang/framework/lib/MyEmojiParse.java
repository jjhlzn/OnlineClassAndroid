package com.jinjunhang.framework.lib;

import com.github.data5tream.emojilib.EmojiParser;
import com.github.data5tream.emojilib.emoji.Map;
import com.jinjunhang.player.utils.LogHelper;

import java.util.Arrays;
import java.util.HashMap;

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
    private static final HashMap<Integer, String> map;

    static {
        map = new HashMap<>();
        map.put(10084, ":heart:");
        map.put(11088,":star:" );
        map.put(9996, ":v:");
        map.put(9749, ":coffee:");
        map.put(9728, ":sunny:");

    }

    public static String convertToCheatCode(String text){

        String returnString = text;

        char[] charArray = text.toCharArray();

        if (charArray.length == 1) {
            LogHelper.d(TAG, String.format("char = %d" , (int)charArray[0]));
            if (map.containsKey((int)charArray[0])) {
                returnString = map.get((int)charArray[0]);
            }
            return returnString;
        }

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
