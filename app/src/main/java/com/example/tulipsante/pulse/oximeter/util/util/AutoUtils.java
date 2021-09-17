package com.example.tulipsante.pulse.oximeter.util.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AutoUtils {
    private static int designWidth;
    private static int designHeight;

    private static int displayWidth;
    private static int displayHeight;
    private static int scaleHeight;

    private static double textDisplayRate;
    private static double textScaleRate;

    /**
     * FIT_SCREEN_SPACING_WIDGET    满屏--拉伸X，Y控件大小按屏幕拉伸、布局按屏幕拉伸
     * FIT_SCALE_SPACING_WIDGET     页面--拉伸X，Y控件大小按比例拉伸、布局按比例拉伸
     */
    public enum AutoMode {
        FIT_SCREEN_SPACING_WIDGET, FIT_SCALE_SPACING_WIDGET,
    }

    public static void setSize(Context ctx, boolean hasStatusBar, int dWidth, int dHeight) {
        if (ctx == null || dWidth < 1 || dHeight < 1) return;

        int scrWidth;
        int scrHeight;

        // 获取屏幕宽高，方案一，从窗口管理器获取...
        // 获取屏幕宽高，方案二，从资源获取
        DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        scrWidth = displayMetrics.widthPixels;
        scrHeight = displayMetrics.heightPixels;

        // 去状态栏
        if (hasStatusBar) {
            scrHeight -= getStatusBarHeight(ctx);
        }

        AutoUtils.designWidth = dWidth;
        AutoUtils.designHeight = dHeight;

        AutoUtils.displayWidth = scrWidth;
        AutoUtils.displayHeight = scrHeight;
        AutoUtils.scaleHeight = scrWidth * dHeight / dWidth;

        double designDiagonal = Math.sqrt(Math.pow(AutoUtils.designWidth, 2) + Math.pow(AutoUtils.designHeight, 2));
        double displayDiagonal = Math.sqrt(Math.pow(AutoUtils.displayWidth, 2) + Math.pow(AutoUtils.displayHeight, 2));
        double scaleDiagonal = Math.sqrt(Math.pow(AutoUtils.displayWidth, 2) + Math.pow(AutoUtils.scaleHeight, 2));

        AutoUtils.textDisplayRate = displayDiagonal / designDiagonal;
        AutoUtils.textScaleRate = scaleDiagonal / designDiagonal;
    }

    public static void auto(Activity act) {
        if (act == null || displayWidth < 1 || displayHeight < 1) return;

        View view = act.getWindow().getDecorView();
        auto(view, AutoMode.FIT_SCALE_SPACING_WIDGET);
    }

    public static void auto(Activity act, AutoMode autoMode) {
        if (act == null || displayWidth < 1 || displayHeight < 1) return;

        View view = act.getWindow().getDecorView();
        auto(view, autoMode);
    }

    public static void auto(View view) {
        if (view == null || displayWidth < 1 || displayHeight < 1) return;

        AutoMode mode = AutoMode.FIT_SCALE_SPACING_WIDGET;

        AutoUtils.autoSize(view, mode);
        AutoUtils.autoPadding(view, mode);
        AutoUtils.autoTextSize(view, mode);
        AutoUtils.autoMargin(view, mode);

        if (view instanceof ViewGroup) {
            auto((ViewGroup) view, mode);
        }
    }

    public static void auto(View view, AutoMode autoMode) {
        if (view == null || displayWidth < 1 || displayHeight < 1) return;

        AutoUtils.autoSize(view, autoMode);
        AutoUtils.autoPadding(view, autoMode);
        AutoUtils.autoTextSize(view, autoMode);

        AutoUtils.autoMargin(view, autoMode);

        if (view instanceof ViewGroup) {
            auto((ViewGroup) view, autoMode);
        }
    }

    public static int getScaleValue(int designValue) {
        return getDisplayWidthValue(designValue);
    }

    private static void auto(ViewGroup viewGroup, AutoMode autoMode) {
        int count = viewGroup.getChildCount();

        for (int i = 0; i < count; i++) {

            View child = viewGroup.getChildAt(i);

            if (child != null) {
                auto(child, autoMode);
            }
        }
    }

    private static void autoMargin(View view, AutoMode autoMode) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams))
            return;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp == null) return;

        lp.leftMargin = getDisplayWidthValue(lp.leftMargin);
        lp.rightMargin = getDisplayWidthValue(lp.rightMargin);
        if (autoMode == AutoMode.FIT_SCREEN_SPACING_WIDGET) {
            lp.topMargin = getDisplayHeightValue(lp.topMargin);
            lp.bottomMargin = getDisplayHeightValue(lp.bottomMargin);
        } else if (autoMode == AutoMode.FIT_SCALE_SPACING_WIDGET) {
            lp.topMargin = getScaleHeightValue(lp.topMargin);
            lp.bottomMargin = getScaleHeightValue(lp.bottomMargin);
        }
    }

    private static void autoPadding(View view, AutoMode autoMode) {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();

        l = getDisplayWidthValue(l);
        r = getDisplayWidthValue(r);
        if (autoMode == AutoMode.FIT_SCREEN_SPACING_WIDGET) {
            t = getDisplayHeightValue(t);
            b = getDisplayHeightValue(b);
        } else if (autoMode == AutoMode.FIT_SCALE_SPACING_WIDGET) {
            t = getScaleHeightValue(t);
            b = getScaleHeightValue(b);
        }

        view.setPadding(l, t, r, b);
    }

    private static void autoSize(View view, AutoMode autoMode) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();

        if (lp == null) return;

        if (lp.width > 0) {
            lp.width = getDisplayWidthValue(lp.width);
        }

        if (lp.height > 0) {
            if (autoMode == AutoMode.FIT_SCREEN_SPACING_WIDGET) {
                lp.height = getDisplayHeightValue(lp.height);
            } else if (autoMode == AutoMode.FIT_SCALE_SPACING_WIDGET) {
                lp.height = getScaleHeightValue(lp.height);
            }
        }
    }

    private static void autoTextSize(View view, AutoMode autoMode) {
        if (view instanceof TextView) {
            double designPixels = ((TextView) view).getTextSize();
            double displayPixels = 0;
            if (autoMode == AutoMode.FIT_SCREEN_SPACING_WIDGET) {
                displayPixels = textDisplayRate * designPixels;
            } else if (autoMode == AutoMode.FIT_SCALE_SPACING_WIDGET) {
                displayPixels = textScaleRate * designPixels;
            }

            ((TextView) view).setIncludeFontPadding(false);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) displayPixels);


        }
    }

    private static int getDisplayWidthValue(int designWidthValue) {
        if (designWidthValue < 2) {
            return designWidthValue;
        }
        return designWidthValue * displayWidth / designWidth;
    }

    private static int getDisplayHeightValue(int designHeightValue) {
        if (designHeightValue < 2) {
            return designHeightValue;
        }
        return designHeightValue * displayHeight / designHeight;
    }

    private static int getScaleHeightValue(int designHeightValue) {
        if (designHeightValue < 2) {
            return designHeightValue;
        }
        return designHeightValue * scaleHeight / designHeight;
    }

    private static int getStatusBarHeight(Context context) {
        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(
                        resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

}