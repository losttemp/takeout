package com.baidu.iov.dueros.waimai.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.presenter.HomePresenter;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
import com.baidu.location.BDLocation;
public class HomeActivity extends BaseActivity<HomePresenter, HomePresenter.HomeUi> implements
		HomePresenter.HomeUi, View.OnClickListener {

	private static final String TAG = HomeActivity.class.getSimpleName();
	private RelativeLayout mRlFood;
	private RelativeLayout mRlFlower;
	private RelativeLayout mRlCake;

	private TextView mTvFood;
	private TextView mTvFlower;
	private TextView mTvCake;
	private AppCompatImageView mIvBack;
	private AppCompatImageView mIvRight;
	private TextView mTvTitle;
	private RelativeLayout mRlSearch;
	private AppCompatImageView mIvTitle;

	private StoreListFragment mStoreListFragment;

	public static String address;

	@Override
	HomePresenter createPresenter() {
		return new HomePresenter();
	}

	@Override
	HomePresenter.HomeUi getUi() {
		return this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		if (getIntent().getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false)) {
			VoiceManager.getInstance().playTTS(HomeActivity.this, getString(R.string.please_choice_commodity));
		}
		iniView();
		iniData();
	}



	@Override
	protected void onResume() {
		super.onResume();
		if (TextUtils.isEmpty(address)) {
			if (!CacheUtils.getAddress().isEmpty()) {
				address = CacheUtils.getAddress();
			} else {
				address = "地址";
			}
		}
		mTvTitle.setText(address);
	}

	private void iniView() {
		mRlFood = findViewById(R.id.rl_food);
		mRlFlower = findViewById(R.id.rl_flower);
		mRlCake =  findViewById(R.id.rl_cake);
		mTvFood = findViewById(R.id.tv_food);
		mTvFlower = findViewById(R.id.tv_flower);
		mTvCake =  findViewById(R.id.tv_cake);
		mIvBack =  findViewById(R.id.iv_back);
		mIvRight =  findViewById(R.id.iv_right);
		mTvTitle =  findViewById(R.id.tv_title);
		mRlSearch = findViewById(R.id.rl_search);
		mIvTitle = findViewById(R.id.iv_title);
	}

	private void iniData() {
		//fragment
		mStoreListFragment = new StoreListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constant.STORE_FRAGMENT_FROM_PAGE_TYPE, Constant.STORE_FRAGMENT_FROM_HOME);
		mStoreListFragment.setArguments(bundle);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fragment_store_list, mStoreListFragment);
		transaction.commit();

		mIvBack.setOnClickListener(this);
		mIvRight.setOnClickListener(this);
		mRlFlower.setOnClickListener(this);
		mRlFood.setOnClickListener(this);
		mRlCake.setOnClickListener(this);
		mTvTitle.setOnClickListener(this);
		mRlSearch.setOnClickListener(this);
		mIvTitle.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_back:
				MyApplicationAddressBean.USER_NAMES.clear();
				MyApplicationAddressBean.USER_PHONES.clear();
				finish();
				break;

			case R.id.tv_title:
			case R.id.iv_title:
				Intent addressIntent = new Intent(HomeActivity.this, AddressSelectActivity.class);
				startActivity(addressIntent);
				break;

			case R.id.iv_right:
				Intent orderListIntent = new Intent(this, OrderListActivity.class);
				startActivity(orderListIntent);
				break;

			case R.id.rl_search:
				Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
				startActivity(searchIntent);
				break;

			case R.id.rl_flower:
				Intent flowerIntent = new Intent(this, RecommendShopActivity.class);
				flowerIntent.putExtra("title", mTvFlower.getText().toString());
				startActivity(flowerIntent);
				break;

			case R.id.rl_cake:
				Intent cakeIntent = new Intent(this, RecommendShopActivity.class);
				cakeIntent.putExtra("title", mTvCake.getText().toString());
				startActivity(cakeIntent);
				break;

			case R.id.rl_food:
				Intent foodIntent = new Intent(this, FoodActivity.class);
				foodIntent.putExtra("title", mTvFood.getText().toString());
				foodIntent.putExtra("latitude", mStoreListFragment.getLatitude());
				foodIntent.putExtra("longitude", mStoreListFragment.getLongitude());
				startActivity(foodIntent);
				break;

			default:
				break;
		}

	}


	
}
