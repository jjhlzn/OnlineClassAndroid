package com.jinjunhang.onlineclass.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.jinjunhang.framework.service.BasicService;
import com.jinjunhang.onlineclass.R;
import com.jinjunhang.onlineclass.model.Advertise;
import com.jinjunhang.onlineclass.model.AlbumType;
import com.jinjunhang.onlineclass.service.GetAdsRequest;
import com.jinjunhang.onlineclass.service.GetAdsResponse;
import com.jinjunhang.onlineclass.ui.cell.AdvImageCell;
import com.jinjunhang.onlineclass.ui.cell.AlbumTypeCell;
import com.jinjunhang.onlineclass.ui.cell.ExtendFunctionManager;
import com.jinjunhang.onlineclass.ui.cell.ListViewCell;
import com.jinjunhang.onlineclass.ui.cell.SectionSeparatorCell;
import com.jinjunhang.player.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class MainPageFragment extends android.support.v4.app.Fragment {


    private static final String TAG = LogHelper.makeLogTag(MainPageFragment.class);

    private List<AlbumType> mAlbumTypes = AlbumType.getAllAlbumType();
    private List<ListViewCell> mCells = new ArrayList<>();
    private ExtendFunctionManager mFunctionManager;
    private AdvImageCell mAdvImageCell;

    private ListView mListView;
    private AlbumTypeAdapter mAlbumTypeAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment_pushdownrefresh, container, false);
        mListView = (ListView) v.findViewById(R.id.listView);

        mListView.setDividerHeight(0);
        mListView.setDivider(null);

        mFunctionManager = new ExtendFunctionManager(getActivity());

        for (AlbumType albumType : mAlbumTypes) {
            AlbumTypeCell item = new AlbumTypeCell(getActivity(), albumType);
            mCells.add(item);
        }

        mCells.add(new SectionSeparatorCell(getActivity()));


        int functionRowCount = mFunctionManager.getRowCount();
        for(int i = 0; i < functionRowCount; i++) {
            mCells.add(mFunctionManager.getCell(i));
        }

        mAdvImageCell = new AdvImageCell(getActivity());
        mCells.add(mAdvImageCell);

        mAlbumTypeAdapter = new AlbumTypeAdapter(mCells);
        mListView.setAdapter(mAlbumTypeAdapter);

        //不能下拉刷新
        v.findViewById(R.id.swipe_refresh_layout).setEnabled(false);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                AlbumType albumType = mAlbumTypeAdapter.getItem(position);

                Intent i = new Intent(getActivity(), AlbumListActivity.class);
                i.putExtra(AlbumListFragment.EXTRA_ALBUMTYPE, albumType);
                startActivity(i); */
            }
        });

        new GetAdvImageTask().execute();
        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdvImageCell != null) {
            mAdvImageCell.release();
        }
    }

    private class GetAdvImageTask extends AsyncTask<Void, Void, GetAdsResponse> {
        @Override
        protected GetAdsResponse doInBackground(Void... params) {
            GetAdsRequest req = new GetAdsRequest();
            return new BasicService().sendRequest(req);
        }

        @Override
        protected void onPostExecute(GetAdsResponse getAdsResponse) {
            super.onPostExecute(getAdsResponse);
            SliderLayout sliderShow = mAdvImageCell.getSliderShow();

            for (Advertise adv : getAdsResponse.getAdvertises()) {
                DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
                textSliderView.image(adv.getImageUrl());
                sliderShow.addSlider(textSliderView);
            }

        }
    }

    private class AlbumTypeAdapter extends ArrayAdapter<ListViewCell> {
        private List<ListViewCell> mAlbumTypes;

        public AlbumTypeAdapter(List<ListViewCell> albumTypes) {
            super(getActivity(), 0, albumTypes);
            mAlbumTypes = albumTypes;
        }

        @Override
        public int getCount() {
            return mAlbumTypes.size();
        }

        @Override
        public ListViewCell getItem(int position) {
            return mAlbumTypes.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewCell item = getItem(position);

            return item.getView();
        }
    }




}
