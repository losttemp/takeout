package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.LogoutBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderOwnerBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitOrderPresenter;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.NetUtil;
import com.baidu.iov.dueros.waimai.utils.StandardCmdClient;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.iov.dueros.waimai.view.ConfirmDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import static com.baidu.iov.dueros.waimai.ui.OrderDetailsActivity.isForeground;

public class PaymentActivity extends BaseActivity<SubmitOrderPresenter, SubmitOrderPresenter.SubmitOrderUi>
        implements SubmitOrderPresenter.SubmitOrderUi, View.OnClickListener {

    private TextView mTimerTv;
    private TextView mAmountTv;
    private TextView mOrderIdTv;
    private TextView mShopNameTv;
    private ImageView mPayUrlImg;
    private ImageView mBackBtn;
    private int mCount = 0;
    private String mStoreId;
    private Long mOrderId;
    private String mPicUrl;
    private int mExpectedTime;
    private int mPayStatus;
    private OrderDetailsReq mOrderDetailsReq;
    private LinearLayout mNoNet;
    private Button mNoInternetButton;
    private View loadingView;
    private LinearLayout mParentsLayout;
    private OrderDetailsResponse.MeituanBean.DataBean dataBean = new OrderDetailsResponse.MeituanBean.DataBean();

    @Override
    SubmitOrderPresenter createPresenter() {
        return new SubmitOrderPresenter();
    }

    @Override
    SubmitOrderPresenter.SubmitOrderUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        Intent intent = getIntent();
        mOrderId = intent.getLongExtra(Constant.ORDER_ID, 0);
        mOrderDetailsReq = new OrderDetailsReq();
        mOrderDetailsReq.setId(mOrderId);
    }

    private void initJudgePayment() {
        if (getIntent().getBooleanExtra("flag", false)) {
            timeTick();
        } else {
            Constant.START_FOODLIST_OR_PAYMENT = false;
            //不是内部跳转 判断bduss是否为空
            if (TextUtils.isEmpty(CacheUtils.getBduss())) {
                //为空需要去登录界面获取bduss
                startLoginAct();
            } else {
                //不为空判断账号是否统一
                if (mOrderId != null) {
                    loadingView.setVisibility(View.VISIBLE);
                    getPresenter().requestCheckOrderOwner(mOrderId);
                } else {
                    loadingView.setVisibility(View.GONE);
                }
            }
        }
    }

    private void initView() {
        mAmountTv = findViewById(R.id.amount);
        mOrderIdTv = findViewById(R.id.order_id);
        mShopNameTv = findViewById(R.id.shop_name);
        mPayUrlImg = findViewById(R.id.qr_code);
        mTimerTv = findViewById(R.id.tv_pay_time);
        mBackBtn = findViewById(R.id.back);
        mNoNet = findViewById(R.id.no_net);
        mNoInternetButton = (Button) findViewById(R.id.no_internet_btn);
        loadingView = findViewById(R.id.submit_order_loading);
        mParentsLayout = findViewById(R.id.payment_layout);
        mBackBtn.setOnClickListener(this);
        mNoInternetButton.setOnClickListener(this);

        NumberFormat nf = new DecimalFormat("##.##");
        Intent intent = getIntent();
        if (intent != null) {
            double amount = intent.getDoubleExtra(Constant.TOTAL_COST, 0);
            if (!TextUtils.isEmpty(intent.getStringExtra(Constant.STORE_ID))) {
                mStoreId = intent.getStringExtra(Constant.STORE_ID);
            }
            mOrderId = intent.getLongExtra(Constant.ORDER_ID, 0);
            mPicUrl = intent.getStringExtra(Constant.PIC_URL);
            mExpectedTime = intent.getIntExtra(Constant.EXPECTED_TIME, 0);
            String shopName = intent.getStringExtra(Constant.SHOP_NAME);
            String payUrl = intent.getStringExtra(Constant.PAY_URL);
            boolean isNeedVoice = intent.getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
            if (isNeedVoice) {
                StandardCmdClient.getInstance().playTTS(PaymentActivity.this, getString(R.string.tts_topay_text));
            }

//            if (isNeedVoice) {
//                StandardCmdClient.getInstance().playTTS(PaymentActivity.this, getString(R.string.pay));
//            }
            if (amount != 0) {
                mAmountTv.setText(String.format(getResources().getString(R.string.cost_text), nf.format(amount)));
            } else {
                mAmountTv.setText("0");
            }

            mOrderIdTv.setText(String.valueOf(mOrderId));
            mShopNameTv.setText(shopName);
            if (mShopNameTv.getText().length() > 15) {
                mShopNameTv.setWidth((int) getResources().getDimension(R.dimen.px500dp));
            }
            createQRImage(payUrl, 200, 200, mPayUrlImg);
            Lg.getInstance().d("PaymentActivity", "payUrl = " + payUrl);

        }
    }

    private void timeTick() {
        ApiUtils.getOrderDetails(mOrderDetailsReq, new ApiCallBack<OrderDetailsResponse>() {
            @Override
            public void onSuccess(OrderDetailsResponse data) {

                    loadingView.setVisibility(View.GONE);
                    mParentsLayout.setVisibility(View.VISIBLE);
                    long expireTime = (long) data.getIov().getData().getExpire_time();
                    long sysTime = (long) data.getIov().getData().getSystime();
                    mTimer = new CountDownTimer((expireTime - sysTime) * 1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), formatTime(millisUntilFinished)));
//                            mCount++;
//                            if (mCount == 5) {
//                                OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq();
//                                mOrderDetailsReq.setId(mOrderId);
//                                getPresenter().requestOrderDetails(mOrderDetailsReq);
//                                mCount = 0;
//                            }
                        }

                        @Override
                        public void onFinish() {
                            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
                            OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq();
                            mOrderDetailsReq.setId(mOrderId);
                            getPresenter().requestOrderDetails(mOrderDetailsReq);
                            if (mPayStatus != Constant.PAY_STATUS_SUCCESS) {
                                ConfirmDialog dialog = new ConfirmDialog.Builder(PaymentActivity.this)
                                        .setTitle(R.string.pay_title)
                                        .setMessage(R.string.pay_time_out)
                                        .setNegativeButton(R.string.anew_submit, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent();
                                                intent.setClass(PaymentActivity.this, FoodListActivity.class);
                                                intent.putExtra(Constant.TO_SHOW_SHOP_CART, true);
                                                intent.putExtra(Constant.ORDER_LSIT_BEAN, dataBean);
                                                intent.putExtra(Constant.ONE_MORE_ORDER, true);
                                                intent.putExtra(Constant.STORE_ID, mStoreId);
                                                intent.putExtra("flag", true);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .setPositiveButton(R.string.back_store, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(PaymentActivity.this, FoodListActivity.class);
                                                intent.putExtra(Constant.STORE_ID, mStoreId);
                                                intent.putExtra("flag", true);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                if (isForeground(PaymentActivity.this)
                                        && !PaymentActivity.this.isFinishing()) {
                                    dialog.show();
                                }
                            }
                        }
                    };
                    mTimer.start();
            }

            @Override
            public void onFailed(String msg) {
                ToastUtils.show(getApplicationContext(), "数据请求失败，请重试", Toast.LENGTH_SHORT);
                loadingView.setVisibility(View.GONE);
                finish();
            }

            @Override
            public void getLogid(String id) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (getIntent().getBooleanExtra("flag", false)) {
                    finish();
                } else {
                    AtyContainer.getInstance().finishAllActivity();
                }
                break;
            case R.id.no_internet_btn:
                netDataReque();
                break;
        }
    }

    private void netDataReque() {
        if (NetUtil.getNetWorkState(this)) {
            loadingView.setVisibility(View.VISIBLE);
            mNoNet.setVisibility(View.GONE);
            mParentsLayout.setVisibility(View.GONE);
            initJudgePayment();
        } else {
            ToastUtils.show(this, getResources().getString(R.string.is_network_connected), Toast.LENGTH_SHORT);
            loadingView.setVisibility(View.GONE);
            mNoNet.setVisibility(View.VISIBLE);
            mParentsLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        netDataReque();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerCancel();
    }

    public static boolean createQRImage(String content, int widthPix, int heightPix, ImageView imageView) {
        try {
            if (content == null || "".equals(content)) {
                return false;
            }

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 2);
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix, heightPix, hints);
            int[] pixels = new int[widthPix * heightPix];
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);

            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap);
            circularBitmapDrawable.setCornerRadius(4);
            imageView.setImageDrawable(circularBitmapDrawable);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerCancel();
    }

    private CountDownTimer mTimer;


    public String formatTime(long millisecond) {
        int minute;
        int second;

        minute = (int) ((millisecond / 1000) / 60);
        second = (int) ((millisecond / 1000) % 60);

        if (minute < 10) {
            if (second < 10) {
                return "0" + minute + ":" + "0" + second;
            } else {
                return "0" + minute + ":" + second;
            }
        } else {
            if (second < 10) {
                return minute + ":" + "0" + second;
            } else {
                return minute + ":" + second;
            }
        }
    }

    public void timerCancel() {
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    @Override
    public void onOrderSubmitSuccess(OrderDetailsResponse data) {
        mParentsLayout.setVisibility(View.VISIBLE);
        mNoNet.setVisibility(View.GONE);
//        loadingView.setVisibility(View.GONE);
        if (data != null) {
            dataBean = data.getMeituan().getData();
            mPayStatus = dataBean.getPay_status();

            if (mPayStatus == Constant.PAY_STATUS_SUCCESS) {
                timerCancel();
                String storeName = dataBean.getPoi_name();
                String recipientPhone = dataBean.getRecipient_phone();
                String recipientAddress = dataBean.getRecipient_address();
                String recipient_name = dataBean.getRecipient_name();
                String foodNameOne = dataBean.getFood_list().get(0).getName();
                int count = dataBean.getFood_list().size();

                Intent intent = new Intent();
                intent.putExtra(Constant.PIC_URL, mPicUrl);
                intent.putExtra(Constant.ORDER_ID, mOrderId);
                intent.putExtra(Constant.STORE_NAME, storeName);
                intent.putExtra(Constant.USER_NAME, recipient_name);
                intent.putExtra(Constant.USER_ADDRESS, recipientAddress);
                intent.putExtra(Constant.USER_PHONE, recipientPhone);
                intent.putExtra(Constant.PRODUCT_NAME, foodNameOne);
                intent.putExtra(Constant.PRODUCT_COUNT, count);
                intent.putExtra(Constant.EXPECTED_TIME, mExpectedTime);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setClass(this, PaySuccessActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    @Override
    public void onSubmitFailure(String msg) {
        loadingView.setVisibility(View.GONE);
        mParentsLayout.setVisibility(View.GONE);
        mNoNet.setVisibility(View.VISIBLE);
//        if (!NetUtil.getNetWorkState(this)) {
//            mParentsLayout.setVisibility(View.GONE);
//            mNoNet.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onCheckOrderOwnerSuccess(OrderOwnerBean bean) {
        loadingView.setVisibility(View.GONE);
        if (bean != null && bean.getIov() != null && bean.getIov().getData() != null && bean.getIov().getData().getEnabled() == 1) {
            timeTick();
        } else {
            //订单不属于该账号提示用户
            getPresenter().startCheckOrderOwnerDialog(mContext);
        }
    }

    @Override
    public void onCheckOrderOwnerError(String error) {
        loadingView.setVisibility(View.GONE);
        timeTick();
    }

    @Override
    public void onLogoutSuccess(LogoutBean data) {
        CacheUtils.saveAddrTime(0);
        CacheUtils.saveAddress("");
        startLoginAct();
    }

    @Override
    public void onLogoutError(String msg) {
        ToastUtils.show(mContext, getResources().getText(R.string.logout_failed), Toast.LENGTH_SHORT);
    }

    private void startLoginAct() {
        Intent intent = new Intent(mContext, TakeawayLoginActivity.class);
        intent.putExtra(Constant.STORE_ID, getIntent().getStringExtra(Constant.STORE_ID));
        intent.putExtra(Constant.TOTAL_COST, getIntent().getDoubleExtra(Constant.TOTAL_COST, 0));
        intent.putExtra(Constant.ORDER_ID, getIntent().getLongExtra(Constant.ORDER_ID, 0));
        intent.putExtra(Constant.SHOP_NAME, getIntent().getStringExtra(Constant.SHOP_NAME));
        intent.putExtra(Constant.PAY_URL, getIntent().getStringExtra(Constant.PAY_URL));
        intent.putExtra(Constant.PIC_URL, getIntent().getStringExtra(Constant.PIC_URL));
        startActivity(intent);
        finish();
    }
}
