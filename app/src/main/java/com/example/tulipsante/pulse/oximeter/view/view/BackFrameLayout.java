package com.example.tulipsante.pulse.oximeter.view.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class BackFrameLayout extends FrameLayout {

    private OnBackListener mOnBackListener;

    public BackFrameLayout(Context context) {
        super(context);
    }

    public BackFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnBackListener(OnBackListener listener) {
        mOnBackListener = listener;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean is = super.dispatchKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mOnBackListener != null) {
                mOnBackListener.onBack();
                return true;
            }
        }

        return is;
    }

    public interface OnBackListener {
        void onBack();
    }
}
