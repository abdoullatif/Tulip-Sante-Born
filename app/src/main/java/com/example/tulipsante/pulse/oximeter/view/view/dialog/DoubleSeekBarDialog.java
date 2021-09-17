package com.example.tulipsante.pulse.oximeter.view.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tulipsante.R;

public class DoubleSeekBarDialog extends Dialog {

    public DoubleSeekBarDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static Dialog showDialog(Context context, String title, int min, int max, int lower, int upper, OnValueListener listener) {
        Dialog dialog = new DoubleSeekBarDialog(context, R.style.dialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_doubleseekbar);

        TextView tvTitle = dialog.findViewById(R.id.title);
        EditText edMin = dialog.findViewById(R.id.edit_min);
        EditText edMax = dialog.findViewById(R.id.edit_max);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);

        tvTitle.setText(title + String.format("(%d-%d)", min, max));
        edMin.setText(String.valueOf(lower));
        edMax.setText(String.valueOf(upper));
        confirm.setOnClickListener(v -> {
            if (listener != null) {
                int low = Integer.parseInt(edMin.getText().toString());
                int up = Integer.parseInt(edMax.getText().toString());
                if (low < min) {
                    Toast.makeText(context, context.getString(R.string.toast_lower_limit_less) + min, Toast.LENGTH_SHORT).show();
                } else if (up > max) {
                    Toast.makeText(context, context.getString(R.string.toast_upper_limit_greater) + max, Toast.LENGTH_SHORT).show();
                } else if (low > up) {
                    Toast.makeText(context, context.getString(R.string.toast_lower_limit_upper_limit), Toast.LENGTH_SHORT).show();
                } else {
                    listener.onValue(Integer.parseInt(edMin.getText().toString()), Integer.parseInt(edMax.getText().toString()));
                    dialog.dismiss();
                }
            }
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
        void onValue(int lower, int upper);
    }
}
