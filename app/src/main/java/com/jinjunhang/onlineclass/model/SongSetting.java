package com.jinjunhang.onlineclass.model;

/**
 * Created by lzn on 16/6/10.
 */
public class SongSetting extends BaseModelObject {

    private int mMaxCommentWord;
    private Boolean mCanComment;

    public int getMaxCommentWord() {
        return mMaxCommentWord;
    }

    public Boolean getCanComment() {
        return mCanComment;
    }

    public void setMaxCommentWord(int maxCommentWord) {
        mMaxCommentWord = maxCommentWord;
    }

    public void setCanComment(Boolean canComment) {
        mCanComment = canComment;
    }
}
