package com.baidu.iov.dueros.waimai.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.presenter.SubmitOrderPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.utils.VoiceManager;
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

public class PaymentActivity extends BaseActivity<SubmitOrderPresenter, SubmitOrderPresenter.SubmitOrderUi>
        implements SubmitOrderPresenter.SubmitOrderUi, View.OnClickListener {

    private TextView mTimerTv;
    private TextView mAmountTv;
    private TextView mOrderIdTv;
    private TextView mShopNameTv;
    private ImageView mPayUrlImg;
    private ImageView mBackBtn;
    private int mCount = 0;
    private Long mStoreId;
    private Long mOrderId;
    private String mPicUrl;
    private int mExpectedTime;
    private int mPayStatus;


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
        timerStart();

    }


    private void initView() {

        mAmountTv = findViewById(R.id.amount);
        mOrderIdTv = findViewById(R.id.order_id);
        mShopNameTv = findViewById(R.id.shop_name);
        mPayUrlImg = findViewById(R.id.qr_code);
        mTimerTv = findViewById(R.id.tv_pay_time);
        mBackBtn = findViewById(R.id.back);

        mBackBtn.setOnClickListener(this);


        NumberFormat nf = new DecimalFormat("##.##");
        Intent intent = getIntent();
        if (intent != null) {
            double amount = intent.getDoubleExtra(Constant.TOTAL_COST, 0);
            mStoreId = intent.getLongExtra(Constant.STORE_ID, 0);
            mOrderId = intent.getLongExtra(Constant.ORDER_ID, 0);
            mPicUrl = intent.getStringExtra(Constant.PIC_URL);
            mExpectedTime = intent.getIntExtra(Constant.EXPECTED_TIME, 0);
            String shopName = intent.getStringExtra(Constant.SHOP_NAME);
            String payUrl = intent.getStringExtra(Constant.PAY_URL);
            boolean isNeedVoice = intent.getBooleanExtra(Constant.IS_NEED_VOICE_FEEDBACK, false);
            if (isNeedVoice) {
                VoiceManager.getInstance().playTTS(PaymentActivity.this, getString(R.string.pay));
            }
            if (amount != 0) {
                mAmountTv.setText(String.format(getResources().getString(R.string.cost_text), nf.format(amount)));
            } else {
                mAmountTv.setText("0");
            }

            mOrderIdTv.setText(String.valueOf(mOrderId));
            mShopNameTv.setText(shopName);
            createQRImage(payUrl, 200, 200, mPayUrlImg);
            Lg.getInstance().d("PaymentActivity", "payUrl = " + payUrl);

        }


    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initView();
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

    private CountDownTimer mTimer = new CountDownTimer(15 * 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), formatTime(millisUntilFinished)));
            mCount++;
            if (mCount == 5) {
                OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq();
                mOrderDetailsReq.setId(mOrderId);
                getPresenter().requestOrderDetails(mOrderDetailsReq);
                mCount = 0;
            }
        }

        @Override
        public void onFinish() {

            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
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
                                intent.putExtra(Constant.STORE_ID, mStoreId);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setPositiveButton(R.string.back_store, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(PaymentActivity.this, FoodListActivity.class);
                                intent.putExtra(Constant.STORE_ID, mStoreId);
                                startActivity(intent);
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setCloseButton(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
//                dialog.setCancelable(false);
                dialog.show();
            }
        }
    };


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
        mTimer.cancel();
    }

    public void timerStart() {
        mTimer.start();
    }


    @Override
    public void onOrderSubmitSuccess(OrderDetailsResponse data) {
        if (data != null) {

            OrderDetailsResponse.MeituanBean.DataBean dataBean = data.getMeituan().getData();
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
            }

        }
    }

    @Override
    public void onSubmitFailure(String msg) {

    }
}
