package com.baidu.iov.dueros.waimai.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.presenter.MainPresenter;
import com.baidu.iov.dueros.waimai.utils.GlideApp;

public class MainFragment extends BaseFragment<MainPresenter, MainPresenter.MainUi> implements MainPresenter.MainUi, OnClickListener {

    private Button mBtn;
    private TextView mTxt;
    private ImageView img;

    @Override
    MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    MainPresenter.MainUi getUi() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        mBtn = (Button) view.findViewById(R.id.btn);
        mTxt = (TextView) view.findViewById(R.id.txt);
        img = view.findViewById(R.id.img);
        mBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        getPresenter().requestData();
    }

    @Override
    public void update(CinemaBean data) {
        mTxt.setText(data.toString());
        String url = "http://img5.imgtn.bdimg.com/it/u=415293130,2419074865&fm=26&gp=0.jpg";
        GlideApp.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(img);
    }

    @Override
    public void failure(String error) {
        mTxt.setText(error);
    }
}
