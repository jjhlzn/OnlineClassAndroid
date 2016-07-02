package com.jinjunhang.onlineclass.ui.fragment.other;

import android.content.DialogInterface;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.KeyValueDao;
import com.jinjunhang.onlineclass.service.ServiceConfiguration;


/**
 * Created by lzn on 16/5/7.
 */
public class ConfigurationFragment extends android.support.v4.app.Fragment  {

    private KeyValueDao mKeyValueDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_fragment_configuration, container, false);
        mKeyValueDao = KeyValueDao.getInstance(getActivity());
        String http = mKeyValueDao.getValue(KeyValueDao.SERVER_HTTP, ServiceConfiguration.DEFAULT_HTTP);
        String serverName = mKeyValueDao.getValue(KeyValueDao.SERVER_HOST, ServiceConfiguration.DEFAULT_HOST);
        String port = mKeyValueDao.getValue(KeyValueDao.SERVER_PORT, ServiceConfiguration.DEFAULT_PORT);

        ((TextView)v.findViewById(R.id.config_http)).setText(http);
        ((TextView)v.findViewById(R.id.config_serverName)).setText(serverName);
        ((TextView)v.findViewById(R.id.config_port)).setText(port+"");

        Button saveButton = (Button)v.findViewById(R.id.config_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                String newHttp = ((TextView)v.findViewById(R.id.config_http)).getText().toString();
                String newServerName = ((TextView)v.findViewById(R.id.config_serverName)).getText().toString().trim();
                String newPort = ((TextView)v.findViewById(R.id.config_port)).getText().toString();
                int port = 80;
                if (!"http".equals(newHttp) && !"https".equals(newHttp)) {
                    Utils.showMessage(getActivity(), "协议必须是http或者https");
                    return;
                }

                try {
                    port = Integer.parseInt(newPort);
                }
                catch (Exception ex) {
                    Utils.showMessage(getActivity(), "端口号必须是数字");
                    return;
                }

                if ("".equals(newServerName)) {
                    Utils.showMessage(getActivity(), "服务器名不能为空");
                    return;
                }

                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_HTTP, newHttp);
                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_HOST, newServerName);
                mKeyValueDao.saveOrUpdate(KeyValueDao.SERVER_PORT, port+"");
                ServiceConfiguration.LOCATOR_HTTP = newHttp;
                ServiceConfiguration.LOCATOR_SERVERNAME = newServerName;
                ServiceConfiguration.LOCATOR_PORT = port;

                Utils.showMessage(getActivity(), "保存成功", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
            }
        });

        return v;
    }
}
