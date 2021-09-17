package com.example.tulipsante.pulse.oximeter.view.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tulipsante.pulse.oximeter.device.usb2serial.bean.BaudRate;
import com.example.tulipsante.R;


public class BaudRateDialog extends Dialog {

    private BaudRateDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static Dialog showDialog(Context context, BaudRate band, OnValueListener listener) {
        Dialog dialog = new BaudRateDialog(context, R.style.dialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.dialog_baud_rate);
        dialog.setCancelable(false);

        EditText edBaudRate = dialog.findViewById(R.id.edit_baund_rate);
        EditText edDataBit = dialog.findViewById(R.id.edit_data_bit);
        EditText edStopBit = dialog.findViewById(R.id.edit_stop_bit);
        EditText edParity = dialog.findViewById(R.id.edit_parity);
        EditText edFlowControl = dialog.findViewById(R.id.edit_flow_control);
        TextView confirm = dialog.findViewById(R.id.confirm);
        TextView cancel = dialog.findViewById(R.id.cancel);
        edBaudRate.setText(String.valueOf(band.getBaudRate()));
        edDataBit.setText(String.valueOf(band.getDataBit()));
        edStopBit.setText(String.valueOf(band.getStopBit()));
        edParity.setText(String.valueOf(band.getParity()));
        edFlowControl.setText(String.valueOf(band.getFlowControl()));

        confirm.setOnClickListener(v -> {
            if (listener != null) {
                int baudRate = Integer.parseInt(edBaudRate.getText().toString());
                byte dataBit = Byte.parseByte(edDataBit.getText().toString());
                byte stopBit = Byte.parseByte(edStopBit.getText().toString());
                byte parity = Byte.parseByte(edParity.getText().toString());
                byte flowControl = Byte.parseByte(edFlowControl.getText().toString());
                BaudRate obj = new BaudRate(baudRate, dataBit, stopBit, parity, flowControl);
                listener.onValue(obj);
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
        void onValue(BaudRate baudRate);
    }

}
