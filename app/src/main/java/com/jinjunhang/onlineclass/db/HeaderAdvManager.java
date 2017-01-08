package com.jinjunhang.onlineclass.db;

import com.jinjunhang.onlineclass.service.GetHeaderAdvResponse;
import com.jinjunhang.onlineclass.ui.lib.CustomApplication;

/**
 * Created by jinjunhang on 17/1/8.
 */

public class HeaderAdvManager {

    private static HeaderAdvManager instance;
    private KeyValueDao mKeyValueDao;
    private static final String KEY_HEADER_ADV_IMAGE_URL = "header_adv_image_url";
    private static final String KEY_HEADER_ADV_TYPE = "header_adv_type";
    private static final String KEY_HEADER_ADV_SONG = "header_adv_song";

    private GetHeaderAdvResponse.HeaderAdvImage adv = new GetHeaderAdvResponse.HeaderAdvImage();

    static {
        instance = new HeaderAdvManager();
        instance.mKeyValueDao = KeyValueDao.getInstance(CustomApplication.get());
        instance.reload();
    }

    public static HeaderAdvManager getInstance() {
        return instance;
    }

    private HeaderAdvManager() {}

    private void reload() {

        adv.setImageUrl( mKeyValueDao.getValue(KEY_HEADER_ADV_IMAGE_URL, ""));
        adv.setType(mKeyValueDao.getValue(KEY_HEADER_ADV_TYPE, GetHeaderAdvResponse.HeaderAdvImage.TYPE_ALBUMLIST));
        adv.getParams().put(GetHeaderAdvResponse.HeaderAdvImage.PARAM_KEY_SONG, mKeyValueDao.getValue(KEY_HEADER_ADV_SONG, "14"));
    }

    public GetHeaderAdvResponse.HeaderAdvImage getHeaderAdv() {
        return adv;
    }

    public void update(GetHeaderAdvResponse.HeaderAdvImage adv) {
        this.adv = adv;

        mKeyValueDao.saveOrUpdate(KEY_HEADER_ADV_IMAGE_URL, adv.getImageUrl());
        mKeyValueDao.saveOrUpdate(KEY_HEADER_ADV_TYPE, adv.getType());
        if (GetHeaderAdvResponse.HeaderAdvImage.TYPE_SONG.equals(adv.getType())) {
            if (adv.getParams().containsKey(GetHeaderAdvResponse.HeaderAdvImage.PARAM_KEY_SONG)) {
                mKeyValueDao.saveOrUpdate(KEY_HEADER_ADV_SONG, adv.getParams().get(GetHeaderAdvResponse.HeaderAdvImage.PARAM_KEY_SONG));
            }
        }
    }
}
