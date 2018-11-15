package com.baidu.iov.dueros.waimai.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.request.OrderDetailsReq;
import com.baidu.iov.dueros.waimai.net.entity.response.OrderDetailsResponse;
import com.baidu.iov.dueros.waimai.presenter.SubmitOrderPresenter;
import com.baidu.iov.dueros.waimai.utils.Encryption;
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

import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.ORDER_ID;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.PAY_URL;
import static com.baidu.iov.dueros.waimai.ui.SubmitOrderActivity.PIC_URL;
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
    private String mPicUrl;

    public final static String USER_NAME = "user_name";
    public final static String USER_PHONE = "user_phone";
    public final static String USER_ADDRESS = "user_address";
    public final static String STORE_NAME = "store_name";
    public final static String PRODUCT_COUNT = "product_count";
    public final static String PRODUCT_NAME = "product_name";

    private int mPayStatus;

    public final static String TO_SHOW_SHOP_CART = "show_shop_card";
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

        NumberFormat nf = new DecimalFormat("#.#");
        Intent intent = getIntent();
        if (intent != null) {

            double amount = intent.getDoubleExtra(TOTAL_COST, 0);
            mOrderId = intent.getLongExtra(ORDER_ID, 0);
            mPicUrl = intent.getStringExtra(PIC_URL);
            String shopName = intent.getStringExtra(SHOP_NAME);
            String payUrl = intent.getStringExtra(PAY_URL);

            if (amount != 0) {
                mAmountTv.setText(String.format(getResources().getString(R.string.cost_text), nf.format(amount)));
            } else {
                mAmountTv.setText("0");
            }

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

                OrderDetailsReq mOrderDetailsReq = new OrderDetailsReq();
                mOrderDetailsReq.setId(mOrderId);
                getPresenter().requestOrderDetails(mOrderDetailsReq);
                mCount = 0;
            }
        }

        @Override
        public void onFinish() {

            mTimerTv.setText(String.format(getResources().getString(R.string.count_down_timer), "00:00"));
            if (mPayStatus != PAY_STATUS_SUCCESS) {

                ConfirmDialog dialog = new ConfirmDialog.Builder(PaymentActivity.this)
                        .setTitle(R.string.pay_title)
                        .setMessage(R.string.pay_time_out)
                        .setNegativeButton(R.string.anew_submit, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent();
                                intent.setClass(PaymentActivity.this, FoodListActivity.class);
                                intent.putExtra(TO_SHOW_SHOP_CART, true);
                                startActivity(intent);

                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.back_store, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(PaymentActivity.this, FoodListActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .setCloseButton(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
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

            if (mPayStatus == PAY_STATUS_SUCCESS) {
                timerCancel();

                String storeName = dataBean.getPoi_name();
                String recipientPhone = dataBean.getRecipient_phone();
                String recipientAddress = dataBean.getRecipient_address();
                String recipient_name = dataBean.getRecipient_name();
                String foodNameOne = dataBean.getFood_list().get(0).getName();
                int count = dataBean.getFood_list().size();

                Intent intent = new Intent();
                intent.putExtra(PIC_URL, mPicUrl);
                intent.putExtra(ORDER_ID, mOrderId);
                intent.putExtra(STORE_NAME, storeName);
                intent.putExtra(USER_NAME, recipient_name);
                intent.putExtra(USER_ADDRESS, recipientAddress);
                intent.putExtra(USER_PHONE, recipientPhone);
                intent.putExtra(PRODUCT_NAME, foodNameOne);
                intent.putExtra(PRODUCT_COUNT, count);
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
