package com.baidu.iov.dueros.waimai.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.utils.AccessibilityClient;

import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class ConfirmDialog extends Dialog {
    public ConfirmDialog(@NonNull Context context) {
        super(context);
        prefix.add(context.getResources().getString(R.string.prefix_choice));
        prefix.add(context.getResources().getString(R.string.prefix_check));
        prefix.add(context.getResources().getString(R.string.prefix_open));
    }

    private Context mContext;

    public static Button positiveBtn;

    public static Button negativeBtn;

    private ArrayList<String> prefix = new ArrayList<>();

    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private Bitmap bitmap;
        private String positiveButtonText;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private OnClickListener closeButtonCliceListener;
        private SpannableString spannableMessage;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setIcon(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setIcon(int bitmap) {
            this.bitmap = BitmapFactory.decodeResource(context.getResources(), bitmap);
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setSpannableMessage(SpannableString spannableMessage) {
            this.spannableMessage = spannableMessage;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getString(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getString(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setCloseButton(DialogInterface.OnClickListener listener) {
            this.closeButtonCliceListener = listener;
            return this;
        }


        public ConfirmDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_confirm, null);

            final ConfirmDialog dialog = new ConfirmDialog(context);
            dialog.getWindow().setWindowAnimations(R.style.Dialog);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            if (spannableMessage != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(spannableMessage);
            } else {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }


            if (closeButtonCliceListener != null) {
                ((Button) layout.findViewById(R.id.close))
                        .setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                closeButtonCliceListener.onClick(dialog,
                                        DialogInterface.BUTTON_POSITIVE);
                            }
                        });
            }

            if (positiveButtonText != null) {
                positiveBtn =  layout.findViewById(R.id.dialog_positive);
                positiveBtn .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    positiveBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEUTRAL);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.dialog_positive).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                negativeBtn =  layout.findViewById(R.id.dialog_negative);
                negativeBtn .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    negativeBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.dialog_negative).setVisibility(
                        View.GONE);
            }

            dialog.setContentView(layout);
            return dialog;
        }
    }

    @Override
    public void dismiss() {
        AccessibilityClient.getInstance().register(mContext,true,prefix, null);
        super.dismiss();
    }

    @Override
    public void show() {
        AccessibilityClient.getInstance().unregister(mContext);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.show();
    }
}
