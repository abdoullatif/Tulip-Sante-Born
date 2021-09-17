package com.example.tulipsante.pulse.oximeter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tulipsante.pulse.oximeter.view.view.BackFrameLayout;


public abstract class BaseFrg extends Fragment {
    protected BackFrameLayout mLayout;

    protected abstract View getLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    protected abstract void initView(View container);

    protected void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    protected void onBackClick() {
        hideInput();
        getFragmentManager().popBackStack();
    }

    //隐藏键盘
    protected void hideInput() {
        if (mLayout != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mLayout.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("onCreateView", this.getClass().getSimpleName());
        View view = getLayout(inflater, container, savedInstanceState);
        initView(view);
        mLayout = new BackFrameLayout(view.getContext());
        mLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLayout.addView(view);

        if (mLayout != null) {
            //请求焦点
            mLayout.setFocusable(true);
            mLayout.setFocusableInTouchMode(true);
            mLayout.requestFocus();

            //设置mView可以消耗事件，防止Activity拿到事件
            mLayout.setClickable(true);

            //设置fragment处理back
            mLayout.setOnBackListener(new BackFrameLayout.OnBackListener() {
                @Override
                public void onBack() {
                    onBackPressed();
                }
            });

            // 设置fragment处理back，EditText获取到焦点后会不执行
            // mLayout.setOnKeyListener(new View.OnKeyListener())

            //隐藏键盘
            hideInput();
        }

        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        Log.e("onDestroyView", this.getClass().getSimpleName());
        super.onDestroyView();
    }
}
