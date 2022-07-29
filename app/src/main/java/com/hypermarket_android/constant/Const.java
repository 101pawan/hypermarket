package com.hypermarket_android.constant;

import android.content.Context;
import android.widget.Toast;

public class Const {


    public static final String currency="$";

    public static void showToast(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String lang = "en";




}
