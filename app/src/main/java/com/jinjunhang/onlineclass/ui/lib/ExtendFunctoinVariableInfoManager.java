package com.jinjunhang.onlineclass.ui.lib;


import com.jinjunhang.framework.lib.LogHelper;
import com.jinjunhang.onlineclass.db.KeyValueDao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jinjunhang on 17/1/7.
 */

public class ExtendFunctoinVariableInfoManager {
    private static final String TAG = LogHelper.makeLogTag(ExtendFunctoinVariableInfoManager.class);

    private static ExtendFunctoinVariableInfoManager instance = null;

    private KeyValueDao mKeyValueDao = KeyValueDao.getInstance(CustomApplication.get());
    private Map<String, Integer> map = new HashMap<>();

    public void reload() {
        LogHelper.d(TAG, "ExtendFunctoinVariableInfoManager.reload() called");
        this.map = new HashMap<>();
        ExtendFunctionManager manager = new ExtendFunctionManager(this, CustomApplication.get());
        List<ExtendFunctionManager.ExtendFunction> funcList = manager.getFunctions();
        Date start = Calendar.getInstance().getTime();
        for (ExtendFunctionManager.ExtendFunction func : funcList) {
            String code = func.getCode();
            Integer value = Integer.parseInt(mKeyValueDao.getValue(func.getCode(), "0"));
            //LogHelper.d(TAG, code, ": ", value);
            this.map.put(code, value);

        }
        Date end = Calendar.getInstance().getTime();
        //debugMap();
        LogHelper.d(TAG, "load functions time: ", end.getTime() - start.getTime());
    }

    public String getFunctionName(String code, String defaultValue) {
        String newName = mKeyValueDao.getValue(code+"_name", defaultValue);
        //LogHelper.d(TAG, code + "_name: " + newName);
        return newName;
    }

    public String getImageUrl(String code) {
        return mKeyValueDao.getValue(code+"_imageUrl", "");
    }

    public void debugMap() {
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            String key = entry.getKey();
            Integer value = entry.getValue();
            LogHelper.d(TAG, key , " = ", value);
        }
    }

    public static ExtendFunctoinVariableInfoManager getInstance() {
        if (instance == null) {
            instance = new ExtendFunctoinVariableInfoManager();
            instance.reload();
        }
        return instance;
    }

    public boolean hasMessage(String code) {
        if (map.containsKey(code)) {
            return 0 != map.get(code);
        }
        //LogHelper.d(TAG, "don't contain function code: ", code);
        return false;
    }


    public void update(String code, Integer value) {
        mKeyValueDao.saveOrUpdate(code, value+"");
        map.put(code, value);
    }

    public void updateName(String code, String name) {
        mKeyValueDao.saveOrUpdate(code+"_name", name);
    }

    public void updateImageUrl(String code, String imageUrl) {
        mKeyValueDao.saveOrUpdate(code+"_imageUrl", imageUrl);
    }

    public void clearMessage(String code) {
        update(code, 0);
        map.put(code, 0);
    }
}
