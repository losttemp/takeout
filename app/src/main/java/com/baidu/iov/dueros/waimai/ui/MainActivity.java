package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
//import com.baidu.iov.dueros.waimai.ui.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainFragment mMainFragment;
    private FragmentTransaction mTransaction;
    private TextView mFimeTab;
    private TextView mCinemaTab;
    private CinemaFragment mCinemaFragment;

    private static final int TAG_FILM = 0;
    private static final int TAG_CINEMA = 1;

    private int currentFragTag = TAG_FILM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        showFragment(TAG_CINEMA);
    }

    private void initView() {
        getSupportActionBar().hide();

        mFimeTab = (TextView) findViewById(R.id.tv_tab_film);
        mFimeTab.setOnClickListener(this);
        mCinemaTab = (TextView) findViewById(R.id.tv_tab_cinema);
        mCinemaTab.setOnClickListener(this);

        findViewById(R.id.ib_finish).setOnClickListener(this);
        findViewById(R.id.iv_menu).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab_film:
                if (currentFragTag != TAG_FILM) {
                    showFragment(TAG_FILM);
                }
                break;
            case R.id.tv_tab_cinema:
                if (currentFragTag != TAG_CINEMA) {
                    showFragment(TAG_CINEMA);
                }
                break;
            case R.id.ib_finish:
                finish();
                break;
            case R.id.iv_menu:
                // TODO: 18-10-8 choose current location
                break;
            default:
                break;
        }
    }

    private void showFragment(int fragTag) {
        currentFragTag = fragTag;

        mTransaction = getSupportFragmentManager().beginTransaction();
        if (fragTag == TAG_FILM) {
            mFimeTab.setTextColor(getResources().getColor(R.color.drop_down_selected));
            mCinemaTab.setTextColor(getResources().getColor(R.color.drop_down_unselected));

            if (mCinemaFragment != null) {
                mTransaction.hide(mCinemaFragment);
            }

            mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_FILM + "");
            if (mMainFragment == null) {
                mMainFragment = new MainFragment();
                //mTransaction.replace(R.id.main_fragment, mMainFragment, TAG_FILM + "");
                mTransaction.add(R.id.main_fragment, mMainFragment, TAG_FILM + "");
            }
            mTransaction.show(mMainFragment);
        } else {
            mCinemaTab.setTextColor(getResources().getColor(R.color.drop_down_selected));
            mFimeTab.setTextColor(getResources().getColor(R.color.drop_down_unselected));

            if (mMainFragment != null) {
                mTransaction.hide(mMainFragment);
            }

            mCinemaFragment = (CinemaFragment) getSupportFragmentManager().findFragmentByTag(TAG_CINEMA + "");
            if (mCinemaFragment == null) {
                mCinemaFragment = new CinemaFragment();
                //mTransaction.replace(R.id.main_fragment, mCinemaFragment, TAG_CINEMA + "");
                mTransaction.add(R.id.main_fragment, mCinemaFragment, TAG_CINEMA + "");
            }
            mTransaction.show(mCinemaFragment);
        }
        //mTransaction.addToBackStack(null);
        mTransaction.commit();
    }
}
