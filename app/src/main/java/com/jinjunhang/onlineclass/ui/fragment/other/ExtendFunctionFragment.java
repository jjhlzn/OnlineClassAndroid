package com.jinjunhang.onlineclass.ui.fragment.other;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.ListViewCellAdapter;
import com.jinjunhang.onlineclass.ui.fragment.BaseFragment;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.lib.ExtendFunctoinMessageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jjh on 2016-7-5.
 */
public class ExtendFunctionFragment extends BaseFragment {

    private ExtendFunctionManager mFunctionManager;
    private List<ListViewCell> mCells = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_listview, container, false);

        v.findViewById(R.id.fragmentContainer).setBackgroundColor(Color.WHITE);

        ListView listView = (ListView) v.findViewById(R.id.listView);
        listView.setBackgroundColor(Color.WHITE);

        //去掉列表的分割线
        listView.setDividerHeight(0);
        listView.setDivider(null);

        mFunctionManager = new ExtendFunctionManager(ExtendFunctoinMessageManager.getInstance(), getActivity(), false);

        int functionRowCount = mFunctionManager.getRowCount();
        for (int i = 0; i < functionRowCount; i++) {
            mCells.add(mFunctionManager.getCell(i));
        }

        ListViewCellAdapter adapter = new ListViewCellAdapter(getActivity(), mCells);
        listView.setAdapter(adapter);

        return v;
    }
}
