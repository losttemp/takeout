package com.baidu.iov.dueros.waimai.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.iov.dueros.waimai.adapter.CinemaAdapter;
import com.baidu.iov.dueros.waimai.adapter.ListDropDownAdapter;
import com.baidu.iov.dueros.waimai.net.entity.response.CinemaBean;
import com.baidu.iov.dueros.waimai.presenter.CinemaPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.DoubleLinkListView;
import com.baidu.iov.dueros.waimai.view.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CinemaFragment extends BaseFragment<CinemaPresenter, CinemaPresenter.CinemaUi> implements CinemaPresenter.CinemaUi {
    private static final String TAG = CinemaFragment.class.getSimpleName();

    private List<View> popupViews = new ArrayList<>();
    private DropDownMenu mDropDownMenu;
    private CinemaAdapter mCinemaAdapter;
    private ListDropDownAdapter mBrandAdapter;
    private ArrayMap<String, String> map;
    private DoubleLinkListView mCityView;
    private static final int MESSAGE_UPDATE_UI = 1;
    private DoubleLinkListView mFilterView;

    @Override
    CinemaPresenter createPresenter() {
        return new CinemaPresenter();
    }

    @Override
    CinemaPresenter.CinemaUi getUi() {
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.baidu.iov.dueros.waimai.ui.R.layout.cinema_fragment, container, false);
        initView(view);

        map = new ArrayMap<>();
        return view;
    }

    private void initView(View view) {
        mDropDownMenu = (DropDownMenu) view.findViewById(com.baidu.iov.dueros.waimai.ui.R.id.dropDownMenu);

        mCityView = new DoubleLinkListView(getActivity());
        mCityView.setBackgroundColor(Color.WHITE);
        mCityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCityView.getLeftAdapter().setSelectedItem(position);
                mCityView.getRightAdapter().setLeftSelectedItem(position);

                if (position == 0) {
                    mCityView.getRightList().setVisibility(View.GONE);
                    mCityView.getRightAdapter().setSelectedItem(0, -1);

                    mDropDownMenu.setTabText(((CinemaBean.FilterBean.AreasBean) mCityView.getLeftAdapter().getItem(0)).getName());
                    mDropDownMenu.closeMenu();
                    loadData(0, 0, -1, -1, -1);
                } else {
                    mCityView.getRightList().setVisibility(View.VISIBLE);
                    mCityView.getRightAdapter().setData(((CinemaBean.FilterBean.AreasBean) mCityView.getLeftAdapter().getItem(position)).getChildren());
                }
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCityView.getRightAdapter().setSelectedItem(mCityView.getLeftAdapter().getSelectedItem(), position);
                String tabTitle = position == 0 ? ((CinemaBean.FilterBean.AreasBean) mCityView.getLeftAdapter().getItem(mCityView.getLeftAdapter().getSelectedItem())).getName()
                        : ((CinemaBean.FilterBean.AreasBean.ChildrenBean) mCityView.getRightAdapter().getItem(position)).getName();

                mDropDownMenu.setTabText(tabTitle);
                mDropDownMenu.closeMenu();
                loadData(mCityView.getLeftAdapter().getSelectedItem(), position, -1, -1, -1);
            }
        });

        ListView brandView = new ListView(getActivity());
//        brandView.setDivider(new ColorDrawable(Color.GRAY));
        brandView.setDividerHeight(0);
        mBrandAdapter = new ListDropDownAdapter(getActivity());
        brandView.setAdapter(mBrandAdapter);

        mFilterView = new DoubleLinkListView(getActivity());
        mFilterView.setBackgroundColor(Color.WHITE);
        mFilterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFilterView.getLeftAdapter().setSelectedItem(position);
                mFilterView.getRightAdapter().setLeftSelectedItem(position);

                CinemaBean.DataBean extraBean = (CinemaBean.DataBean) mFilterView.getLeftAdapter().getItem(position);
                if (position == 0) {
                    mFilterView.getRightList().setVisibility(View.GONE);
                    mFilterView.getRightAdapter().setSelectedItem(0, -1);

                    mDropDownMenu.setTabText(extraBean.getName());
                    mDropDownMenu.closeMenu();
                    loadData(-1, -1, -1, 0, 0);
                } else if (Constant.SPECIAL_HALL_ID == extraBean.getId()) {
                    mFilterView.getRightList().setVisibility(View.VISIBLE);
                    mFilterView.getRightAdapter().setData(((CinemaBean.FilterBean.ExtraBean)extraBean).getChildren());
                } else {
                    mFilterView.getRightList().setVisibility(View.GONE);
                    mFilterView.getRightAdapter().setSelectedItem(position, -1);

                    mDropDownMenu.setTabText(extraBean.getName());
                    mDropDownMenu.closeMenu();
                    loadData(-1, -1, -1, -1, position);
                }
            }
        }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFilterView.getRightAdapter().setSelectedItem(Constant.SPECIAL_HALL_ID, position);

                CinemaBean.FilterBean.ExtraBean extraBean = (CinemaBean.FilterBean.ExtraBean) mFilterView.getLeftAdapter().getItem(Constant.SPECIAL_HALL_ID);
                mDropDownMenu.setTabText(extraBean.getChildren().get(position).getName());
                mDropDownMenu.closeMenu();
                loadData(-1, -1, -1, position, Constant.SPECIAL_HALL_ID);
            }
        });

        popupViews.add(mCityView);
        popupViews.add(brandView);
        popupViews.add(mFilterView);

        brandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBrandAdapter.setSelectedItem(position);
                mDropDownMenu.setTabText(((CinemaBean.DataBean) mBrandAdapter.getItem(position)).getName());
                mDropDownMenu.closeMenu();

                loadData(-1, -1, position, -1, -1);
            }
        });

        final ListView cinimaList = new ListView(getActivity());
        cinimaList.setDivider(new ColorDrawable(Color.GRAY));
        cinimaList.setDividerHeight(1);
        mCinemaAdapter = new CinemaAdapter(getActivity());
        cinimaList.setAdapter(mCinemaAdapter);
        cinimaList.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cinimaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CinemaBean.ListBean listBean = (CinemaBean.ListBean) mCinemaAdapter.getItem(position);
                // TODO: 18-10-9
            }
        });

        mDropDownMenu.setDropDownMenu(Arrays.asList(getResources().getStringArray(com.baidu.iov.dueros.waimai.ui.R.array.tab_filters)), popupViews, cinimaList);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().requestData(null);
        getPresenter().registerCmd(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        Lg.getInstance().d(TAG, "onPause getPresenter().unregisterCmd");
        getPresenter().unregisterCmd(getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        popupViews.clear();
    }

    private void loadData(int areaId, int aoiId, int brandId, int featureId, int extraId) {
        Lg.getInstance().d(TAG, "loadData areaId:" + areaId + "aoiId:" + aoiId + " brandId:" + brandId + " featureId:" + featureId + " extraId:" + extraId);
        if (areaId != -1) {
            map.put(Constant.AREA_ID, areaId + "");
        }
        if (aoiId != -1) {
            map.put(Constant.AOI_ID, aoiId + "");
        }
        if (brandId != -1) {
            map.put(Constant.BRAND_ID, brandId + "");
        }
        if (featureId != -1) {
            map.put(Constant.FEATURE_ID, featureId + "");
        }
        if (extraId != -1) {
            map.put(Constant.EXTRA_ID, extraId + "");
        }
        getPresenter().requestData(map);
    }


    @Override
    public void onSuccess(CinemaBean cinemaBean) {
        if (cinemaBean != null && cinemaBean.getList() != null) {
            addFirstItem(cinemaBean);

            Message msg = new Message();
            msg.what = MESSAGE_UPDATE_UI;
            msg.obj = cinemaBean;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void failure(String msg) {
        mCinemaAdapter.setData(null);
    }

    @Override
    public void close() {
        if (null != getActivity()) {
            getActivity().finish();
        }
    }

    private CinemaBean addFirstItem(CinemaBean cinemaBean) {
        CinemaBean.FilterBean filter = cinemaBean.getFilter();

        for (CinemaBean.FilterBean.AreasBean areasBean: filter.getAreas()) {
            areasBean.getChildren().add(0, addAll(areasBean.getChildren()));
        }

        filter.getAreas().add(0, addAll(filter.getAreas()));
        filter.getBrands().add(0, addAll(filter.getBrands()));
        filter.getExtra().add(0, addAll(filter.getExtra()));

        return cinemaBean;
    }

    public <T extends CinemaBean.DataBean> T addAll(List<T> list){
        if (list == null || list.size()== 0)
            return null;

        int sum = 0;
        for (T bean:list) {
            sum += bean.getValue();
        }

        T allBean = list.get(0);
        if(allBean instanceof CinemaBean.FilterBean.AreasBean){
            allBean = (T) new CinemaBean.FilterBean.AreasBean();
            allBean.setName(getString(com.baidu.iov.dueros.waimai.ui.R.string.all_area));
            allBean.setValue(sum);
        } else {
            allBean = (T) new CinemaBean.DataBean();
            allBean.setName(getString(com.baidu.iov.dueros.waimai.ui.R.string.all));
            allBean.setValue(sum);
        }
        return allBean;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_UPDATE_UI) {
                CinemaBean cinemaBean = (CinemaBean) msg.obj;

                mCinemaAdapter.setData(cinemaBean.getList());
                mCityView.getLeftAdapter().setData(cinemaBean.getFilter().getAreas());
                mBrandAdapter.setData(cinemaBean.getFilter().getBrands());
                mFilterView.getLeftAdapter().setData(cinemaBean.getFilter().getExtra());
            }
        }
    };

}
