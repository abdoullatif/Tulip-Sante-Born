package com.example.tulipsante.pulse.oximeter.util.view.progress;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.tulipsante.R;


public class ProgressUtil extends Dialog {
    private static ProgressUtil dialogHud;

    private ProgressUtil(Context context) {
        super(context);
    }

    private ProgressUtil(Context context, int theme) {
        super(context, theme);
    }

    public static ProgressUtil showProgressHUD(Context context, CharSequence message, boolean cancelable, OnCancelListener cancelListener) {
        dialogHud = new ProgressUtil(context, R.style.ProgressHUD);
        dialogHud.setTitle("");
        dialogHud.setContentView(R.layout.progress_hud);
        if (message == null || message.length() == 0) {
            dialogHud.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialogHud.findViewById(R.id.message);
            txt.setText(message);
        }
        dialogHud.setCancelable(cancelable);
        dialogHud.setOnCancelListener(cancelListener);
        dialogHud.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    dialog.dismiss();
                }
                return false;
            }
        });
        dialogHud.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialogHud.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialogHud.getWindow().setAttributes(lp);
        dialogHud.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialogHud.show();
        return dialogHud;
    }

    public static void dismissProgressHUD() {
        if (dialogHud == null) {
            return;
        }
        if (dialogHud.isShowing()) {
            dialogHud.dismiss();
        }
    }

}
