package com.example.tulipsante.pulse.oximeter.view.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.tulipsante.R;
import com.example.tulipsante.pulse.oximeter.bean.EWorMode;


import java.util.List;

public class SelectDialog extends Dialog {

    private SelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static Dialog showDialog(Context context, String title, List<String> list, String checkItem, OnValueListener listener) {
        Dialog dialog = new SelectDialog(context, R.style.dialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_select);

        TextView tvTitle = dialog.findViewById(R.id.title);
        RadioGroup rgContainer = dialog.findViewById(R.id.rg_container);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);
        tvTitle.setText(title);

        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);

            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(i);
            radioButton.setButtonDrawable(0);

            Drawable drawableLeft = ContextCompat.getDrawable(context, R.drawable.radio_check);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            radioButton.setCompoundDrawablePadding((int) context.getResources().getDimension(R.dimen.drawable_padding));

            if (EWorMode.usb.toString().equals(item)) {
                radioButton.setText(R.string.work_mode_usb);
            } else {
                radioButton.setText(R.string.work_mode_ble);
            }
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.black));

            if (item.equals(checkItem)) {
                radioButton.setChecked(true);
            }

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.select_height));
            rgContainer.addView(radioButton, layoutParams);
        }

        confirm.setOnClickListener(v -> {
            if (listener != null) {
                listener.onValue(rgContainer.getCheckedRadioButtonId());
            }
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
        return dialog;
    }

    public interface OnValueListener {
        void onValue(int position);
    }

}
