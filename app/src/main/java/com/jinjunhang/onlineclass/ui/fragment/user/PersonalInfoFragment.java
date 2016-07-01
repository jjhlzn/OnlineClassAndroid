package com.jinjunhang.onlineclass.ui.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.db.LoginUserDao;
import com.jinjunhang.onlineclass.model.LoginUser;
import com.jinjunhang.onlineclass.ui.cell.CellClickListener;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.me.LineRecord;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-1.
 */
public class PersonalInfoFragment extends BaseFragment {

    private List<LineRecord> mItems;


    private void createItems() {
        LoginUser loginUser = LoginUserDao.getInstance(getContext()).get();
        mItems = new ArrayList<>();
        mItems.add(new LineRecord("姓名", loginUser.getName(), new CellClickListener() {
            @Override
            public void onClick(ListViewCell cell) {

            }
        }));
        mItems.add(new LineRecord("昵称", loginUser.getNickName(), new CellClickListener() {
            @Override
            public void onClick(ListViewCell cell) {

            }
        }));
        mItems.add(new LineRecord("性别", loginUser.getSex(), new CellClickListener() {
            @Override
            public void onClick(ListViewCell cell) {

            }
        }));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);

        ListView listView =  (ListView) v.findViewById(R.id.listView);
        createItems();
        final PersonalInfoAdapter adapter = new PersonalInfoAdapter(mItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mItems.get(position).getListener().onClick(null);
            }
        });

        return v;
    }

    private class PersonalInfoAdapter extends ArrayAdapter<LineRecord> {
        //private List<LineRecord> mItems;

        public PersonalInfoAdapter(List<LineRecord> items) {
            super(getActivity(), 0, items);
            //this.mItems = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.list_item_personal_info, null);

            TextView keyTextView = (TextView) v.findViewById(R.id.key_label);
            TextView valueTextView = (TextView) v.findViewById(R.id.value_label);

            LineRecord item = getItem(position);
            keyTextView.setText(item.getTitle());
            valueTextView.setText(item.getOtherInfo());

            return v;
        }
    }
}
