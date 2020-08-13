package com.baidu.iov.dueros.waimai.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.CityListBean;
import com.baidu.iov.dueros.waimai.utils.CheckUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.GsonUtil;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.view.LetterListView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.domain.multipltextview.MultiplTextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class CityListActivity extends Activity {

    private static final String TAG = CityListActivity.class.getSimpleName();
    private CityListAdapter mCityAdapter;
    private ListView mCityList;
    private TextView mOverlay;
    private LetterListView mLetterListView;
    private HashMap<String, Integer> mAlpha;
    private String[] mSections;
    private List<CityListBean.AllBean> mAllCityData;
    private List<CityListBean.HotBean> mHotCityData;

    private WindowManager mWindowManager;
    private ImageView mivBack;

    private static final int OVERLAY_GONE = 13;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case OVERLAY_GONE:
                    mOverlay.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private TextView mTvError;
    private LocationManager mlocationManager;
    private String mlocation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        initView();
        initData();
        initOverlay();
        getLocationCity();
//        getPresenter().requestData(null);
        setCityListAdapterData();
    }

    private void initView() {
        mCityList = findViewById(R.id.list_city);
        mivBack = findViewById(R.id.list_iv_back);
        mLetterListView = findViewById(R.id.letter_listView);
        mLetterListView.setOnTouchingLetterChangedListener(mLetterlister);
        mTvError = findViewById(R.id.error_fl);
        mTvError.setVisibility(View.GONE);
    }


    private void initData() {
        mAllCityData = new ArrayList<>();
        mHotCityData = new ArrayList<>();
        mAlpha = new HashMap<>();

        mCityList.setOnItemClickListener(mItemClickListener);
        mCityAdapter = new CityListAdapter(mAllCityData, mHotCityData);
        mCityList.setAdapter(mCityAdapter);
        CityListBean.AllBean city = new CityListBean.AllBean(-1, getResources().getString(R.string.positioned), "0");
        mAllCityData.add(city);
        city = new CityListBean.AllBean(-1, getResources().getString(R.string.hot), "1");
        mAllCityData.add(city);
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            reFreshCityInfo(mAllCityData.get(position).getName(), mAllCityData.get(position).getId());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityListActivity.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationManager!=null){
            mlocationManager.stopLocation();
            mlocationManager = null;
        }
        mLocationListener = null;
        mWindowManager.removeViewImmediate(mOverlay);
        mAllCityData.clear();
        mHotCityData.clear();
        mAlpha.clear();

    }


    private BDAbstractLocationListener mLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            int requestCode = bdLocation.getLocType();
            switch (requestCode) {
                case LocationManager.TypeGpsLocation:
                case LocationManager.TypeOffLineLocation:
                case LocationManager.TypeNetWorkLocation:
                    //Constant.LATITUDE = bdLocation.getLatitude();
                    //Constant.LONGITUDE = bdLocation.getLongitude();
                    mlocation = String.valueOf(bdLocation.getCity());
//                    getPresenter().requestData(null);
                    setCityListAdapterData();
                    break;
                default:
                    break;

            }
        }
    };

    class CityListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<CityListBean.AllBean> mList;
        private List<CityListBean.HotBean> mHotList;


        final int VIEW_TYPE = 5;

        public void setList(List<CityListBean.AllBean> alllist, List<CityListBean.HotBean> hotList) {
            this.mList = alllist;
            this.mHotList = hotList;
            mAlpha = new HashMap<>();
            mSections = new String[alllist.size()];
            for (int i = 0; i < alllist.size(); i++) {
                String currentStr = getAlpha(alllist.get(i).getPinyin());
                String previewStr = (i - 1) >= 0 ? getAlpha(alllist.get(i - 1)
                        .getPinyin()) : " ";
                if (!previewStr.equals(currentStr)) {
                    String name = getAlpha(alllist.get(i).getPinyin());
                    mAlpha.put(name, i);
                    mSections[i] = name;
                }
            }
        }


        public CityListAdapter(List<CityListBean.AllBean> alllist,
                               List<CityListBean.HotBean> hotList) {
            this.mInflater = LayoutInflater.from(CityListActivity.this);
            this.mList = alllist;

            this.mHotList = hotList;

        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 4 ? position : 4;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder mHolder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            int viewType = getItemViewType(position);
            if (viewType == 0) {
                convertView = mInflater.inflate(R.layout.frist_list_item, null);
                Button mCityBtn = convertView.findViewById(R.id.current_city);
                TextView mCityTv = convertView.findViewById(R.id.try_city);
                if (!CheckUtils.isEmpty(mlocation)) {
                    mCityBtn.setText(mlocation);
                    mCityBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reFreshCityInfo(mlocation, 0);
                        }
                    });
                } else {
                    mCityBtn.setText(R.string.position_fail);
                    mCityTv.setVisibility(View.VISIBLE);
                    mCityBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getLocationCity();
                            mTvError.setVisibility(View.GONE);
                        }
                    });
                }

            } else if (viewType == 1) {
                convertView = mInflater.inflate(R.layout.recent_city, null);
                GridView hotCity = convertView
                        .findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        reFreshCityInfo(mHotCityData.get(position).getName(), mHotCityData.get(position).getId());
                    }
                });
                hotCity.setAdapter(new HotCityAdapter(mHotList));
            } else {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CityListActivity.this).inflate(R.layout.list_item, null);
                    mHolder = new ViewHolder();
                    mHolder.alpha = convertView
                            .findViewById(R.id.city_alpha);
                    mHolder.name = convertView
                            .findViewById(R.id.city_name);
                    mHolder.alpha.setEnabled(false);
                    convertView.setTag(mHolder);
                } else {
                    mHolder = (ViewHolder) convertView.getTag();
                }

                mHolder.name.setText(mAllCityData.get(position).getName());
                String currentStr = getAlpha(mAllCityData.get(position).getPinyin());
                mHolder.alpha.setVisibility(View.VISIBLE);
                mHolder.alpha.setText(currentStr);
                if (position >= 3) {
                    String previewStr = getAlpha(mAllCityData
                            .get(position - 1).getPinyin());
                    if (previewStr.equals(currentStr)) {
                        mHolder.alpha.setVisibility(View.GONE);

                    }
                }

            }
            return convertView;
        }

        private class ViewHolder {
            MultiplTextView alpha;
            MultiplTextView name;

        }


    }

    private void getLocationCity() {
        mlocationManager = LocationManager.getInstance(getApplicationContext());
        mlocationManager.getLcation(null, null, 0, true);
        mlocationManager.setLocationListener(mLocationListener);
        mlocationManager.startLocation();
    }


    class HotCityAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<CityListBean.HotBean> mHotCitys;

        public HotCityAdapter(List<CityListBean.HotBean> hotCitys) {

            mInflater = LayoutInflater.from(CityListActivity.this);
            this.mHotCitys = hotCitys;
        }

        @Override
        public int getCount() {
            return mHotCitys.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.item_city_hot, null);
            MultiplTextView city = convertView.findViewById(R.id.city_hot);
            city.setText(mHotCitys.get(position).getName());
            return convertView;
        }
    }


    private void initOverlay() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mOverlay = (TextView) inflater.inflate(R.layout.overlay, null);
        mOverlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(mOverlay, lp);
    }

    private LetterListView.OnTouchingLetterChangedListener mLetterlister
            = new LetterListView.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String letter) {
            mOverlay.setText(letter);
            mOverlay.setVisibility(View.VISIBLE);
            if (mAlpha.get(letter) != null) {
                int position = mAlpha.get(letter);
                mCityList.setSelection(position);

            }
        }

        @Override
        public void onTouchexit() {
            mHandler.sendEmptyMessageDelayed(OVERLAY_GONE, 100);
        }
    };


    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);

        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else {
            return "#";
        }
    }


    private void reFreshCityInfo(String name, int id) {
        Intent intent = new Intent();
        intent.putExtra(Constant.CITYCODE, name);
        setResult(Constant.CITY_RESULT_CODE_CHOOSE, intent);
        finish();
    }

    private CityListBean readCityJson() {
        try {
            InputStreamReader isr = new InputStreamReader(getAssets().open("city.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());
            String array = testjson.getString("data");
            CityListBean cityListBean = GsonUtil.fromJson(array, CityListBean.class);
            return cityListBean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setCityListAdapterData() {
        CityListBean cityListBean = readCityJson();
        if (cityListBean != null) {
            mAllCityData.clear();
            mHotCityData.clear();
            mAllCityData.addAll(cityListBean.getAll());
            mHotCityData.addAll(cityListBean.getHot());
            mCityAdapter.setList(mAllCityData, mHotCityData);
            mCityAdapter.notifyDataSetChanged();
        }
    }
}
