package com.jinjunhang.onlineclass.ui.lib;

import android.os.AsyncTask;

import com.jinjunhang.framework.lib.LogHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by jinjunhang on 17/8/2.
 */

public class ParseHtmlPageTask extends AsyncTask<String, Void, Void> {

    private final static String TAG = LogHelper.makeLogTag(ParseHtmlPageTask.class);

    private  ShareManager mShareManager;

    public ParseHtmlPageTask setShareManager(ShareManager shareManager) {
        this.mShareManager = shareManager;
        return this;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String url = strings[0];
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
            LogHelper.d(TAG, "title: ", title);

            mShareManager.resetSetting();

            if (title != null && title != "") {
                mShareManager.setShareTitle(title);
            }

            Elements metas = doc.select("meta");
            for (Element meta : metas) {
                LogHelper.d(TAG, meta.tagName() + ",  " + meta.attr("name") + ", " + meta.attr("content"));
                if ( "shareurl".equals(meta.attr("name")) && meta.attr("content") != null && !"".equals(meta.attr("content")) ) {
                    mShareManager.setShareUrl(meta.attr("content"));
                }
                if ( "description".equals(meta.attr("name")) && meta.attr("content") != null && !"".equals(meta.attr("content")) ) {
                    mShareManager.setDescription(meta.attr("content"));
                }
            }

        }
        catch (Exception ex) {
            LogHelper.e(TAG, ex);
        }
        return null;
    }
}