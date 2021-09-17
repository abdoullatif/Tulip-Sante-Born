package com.example.tulipsante.pulse.oximeter.util.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LanguageUtil {

    /**
     * support appcompat库 或 androidx appcompat库 <= 1.0.2
     * <p>
     * 对于7.0及以上，需要在每个Activity、Service 里设置语言
     * 定义一个BaseActivity，BaseService重写attachBaseContext方法，在此方法里进行语言设置(createConfigurationContext)
     * 重启后自动执行createConfigurationContext切换语言
     * <p>
     * 对于7.0以下，在app 初始化的时候，设置指定语言 resources.updateConfiguration
     * 切换语言只需要调用 resources.updateConfiguration，再重启页面
     *
     * @param context  上下文
     * @param language 语言
     */
    public static Context changeAppLanguage(Context context, String language) {
        if (TextUtils.isEmpty(language)) {
            return context;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Resources resources = context.getResources();
            Locale locale = getLocaleByLanguage(language);

            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(configuration, dm);
        } else {
            Resources resources = context.getResources();
            Locale locale = getLocaleByLanguage(language);

            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(configuration);
        }

        return context;
    }

    /**
     * androidx appcompat库 > 1.0.2
     * <p>
     * 对于7.0及以上，需要在每个Activity、Service 里设置语言
     * 定义一个BaseActivity，BaseService重写attachBaseContext方法，在此方法里进行语言设置(createConfigurationContext)
     * 重启后自动执行createConfigurationContext切换语言
     * <p>
     * 对于7.0以下，同上
     *
     * @param context  上下文
     * @param language 语言
     * @return
     */
    public static Context attachBaseContext(Context context, String language) {
        if (TextUtils.isEmpty(language)) {
            return context;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Configuration configuration = new Configuration(context.getResources().getConfiguration());
            Locale locale = getLocaleByLanguage(language);
            configuration.setLocale(locale);
            return context.createConfigurationContext(configuration);
        } else {
            Configuration configuration = context.getResources().getConfiguration();
            Locale locale = getLocaleByLanguage(language);
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            return context.createConfigurationContext(configuration);
        }
    }

    private static Locale getLocaleByLanguage(String language) {
        Locale locale;
        if (language.equals("ch")) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            locale = Locale.ENGLISH;
        }
        return locale;
    }
}