package com.jinjunhang.framework.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jinjunhang.onlineclass.R;


/**
 * Created by lzn on 16/3/19.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final String TAG = "SingleFragmentActivity";

    protected abstract Fragment createFragment();

    protected String getActivityTitle() {
        return "巨方助手";
    }

    public void setActivityTitle(String title) {
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(title);
    }

    protected void createActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = getLayoutInflater().inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(customView);
        Toolbar parent = (Toolbar) customView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

    }

    //没有用了
    protected boolean isNeedPushDownFresh() {return false;}


    protected boolean hasParent() {return true;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);

        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);


        if (fragment == null) {
            fragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


        if (hasActionBar()) {
            Log.d(TAG, "has action bar");
            createActionBar();
            ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_text)).setText(getActivityTitle());
            //if (NavUtils.getParentActivityName(this) == null) {
            if (!hasParent()) {
                getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button).setVisibility(View.INVISIBLE);
            } else {

                ImageButton backButton = (ImageButton) getSupportActionBar().getCustomView().findViewById(R.id.actionbar_back_button);
                backButton.setVisibility(View.VISIBLE);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected boolean hasActionBar() {
        return true;
    }

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    @Override
    public void onBackPressed() {

        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
