package com.hypermarket_android.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.Log;
import com.hypermarket_android.constant.Const;

import java.util.Locale;

/**
 * Created by NV on 23-04-2018.
 */

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static void onCreate(Context context) {

        String lang;
        if (getLanguage(context).isEmpty()) {
            lang = getPersistedData(context, Locale.getDefault().getLanguage());
        } else {
            lang = getLanguage(context);
        }

        setLocale(context, lang);
    }

    public static void onCreate(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static void setLocale(Context context, String language) {
        persist(context, language);
        Const.lang = ""+language;
      //  changeLang(context, language);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            updateResources(context, language);
        } else {
            updateResourcesLegacy(context, language);
        }

    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static void updateResourcess(Context context, String language) {

        Locale locale = new Locale(language);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
resources.updateConfiguration(configuration,context.getResources().getDisplayMetrics());
      /*  if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
          *//*  Locale.getDefault(Locale.Category.DISPLAY);
//            Locale.setDefault(Locale.Category.DISPLAY, locale);
//            configuration.setLocales(new LocaleList(locale));

            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            Locale.setDefault(Locale.Category.DISPLAY, locale);
            configuration.setLocales(localeList);
            context.createConfigurationContext(configuration);*//*
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.createConfigurationContext(configuration);

        }
        else */


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }else{
            configuration.locale = locale;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
       }

      /*  PrefManager prefManager = new PrefManager(context);
        if(language.equals("en")) {
            prefManager.setselectLanguage("English");
        } else if(language.equals("ar")){
            prefManager.setselectLanguage("Arabic");
        }*/
       //        configuration.locale = locale;
    }


    @TargetApi(Build.VERSION_CODES.P)
    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Log.d("LocaleHelper", "language above 24: "+language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        LocaleList localeList = new LocaleList(locale);
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLocales(localeList);

        configuration.setLayoutDirection(locale);
        context.createConfigurationContext(configuration);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    @SuppressWarnings("deprecation")
    public static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Log.d("LocaleHelper", "language below 24: "+language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale);
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    public static ContextWrapper changeLang(Context context, String lang_code){
        Locale sysLocale;

        Resources rs = context.getResources();
        Configuration config = rs.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }
        if (!lang_code.equals("") && !sysLocale.getLanguage().equals(lang_code)) {
            Locale locale = new Locale(lang_code);
            Locale.setDefault(locale);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context = context.createConfigurationContext(config);
            } else {
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
            }
        }

        return new ContextWrapper(context);
    }

}