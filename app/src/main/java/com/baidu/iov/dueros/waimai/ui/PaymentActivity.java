package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderSubmitBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.SubmitOrderPresenter;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baidu.iov.dueros.waimai.ui.AddressListActivity.ADDRESS_DATA;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.POI_INFO;
import static com.baidu.iov.dueros.waimai.ui.FoodListActivity.PRODUCT_LIST_BEAN;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.ORDER_ID;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.PAY_URL;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.SHOP_NAME;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.TOTAL_COST;

public class PaymentActivity extends BaseActivity<SubmitOrderPresenter, SubmitOrderPresenter.SubmitOrderUi>
        implements SubmitOrderPresenter.SubmitOrderUi {

    private TextView mTimerTv;
    private TextView mAmountTv;
    private TextView mOrderIdTv;
    private TextView mShopNameTv;
    private ImageView mPayUrlImg;
    private int mCount = 0;
    private Long mOrderId;
    private int mPayStatus;

    private final static int PAY_STATUS_SUCCESS = 3;


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

        NumberFormat nf = new DecimalFormat("##.##");
        Intent intent = getIntent();
        if (intent != null) {

            double amount = intent.getDoubleExtra(TOTAL_COST, 0);
            mOrderId = intent.getLongExtra(ORDER_ID, 0);
            String shopName = intent.getStringExtra(SHOP_NAME);
            String payUrl = intent.getStringExtra(PAY_URL);

            if (amount != 0) {
                mAmountTv.setText(String.format(getResources().getString(R.string.cost_text), nf.format(amount)));
            } else {
                mAmountTv.setText("0.00");
            }

            mAmountTv.setText(String.valueOf(amount));
            mOrderIdTv.setText(String.valueOf(mOrderId));
            mShopNameTv.setText(shopName);
            createQRImage(payUrl, 200, 200, mPayUrlImg);

        }


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
            imageView.setImageBitmap(bitmap);

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

                String user_phone = "18201010600";
                String phone = Encryption.encrypt(user_phone);
                OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq(mOrderId, phone);
                getPresenter().requestOrderDetails(mOrderDetailsReq);
                mCount = 0;
            }
        }

        @Override
        public void onFinish() {

            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
            if (mPayStatus != PAY_STATUS_SUCCESS){
                
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


            mPayStatus = data.getMeituan().getData().getPay_status();
            if (mPayStatus == PAY_STATUS_SUCCESS){
                timerCancel();
                Intent intent = new Intent();
                intent.setClass(this, PaySuccessActivity.class);
                startActivity(intent);
            }

        }
    }

    @Override
    public void onSubmitFailure(String msg) {

    }
}
