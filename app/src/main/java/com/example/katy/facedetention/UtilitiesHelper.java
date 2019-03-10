package com.example.katy.facedetention;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class UtilitiesHelper {

    public static String User_TABLE_NAME = "users";
    public static String Picture_TABLE_NAME = "pictures";
    public static String BUNDLE_USER_ID = "userid";
    public static String BUNDLE_PICTURE_ID = "picid";




    public static void setUsernameAndPassword(Context context, String username, String password, String userID){
        SharedPreferences.Editor editor = context.getSharedPreferences("automaticlogin", MODE_PRIVATE).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("userid", userID);
        editor.apply();
    }

    public static String getUsername(Context context){
        SharedPreferences prefs = context.getSharedPreferences("automaticlogin", MODE_PRIVATE);
        return prefs.getString("username", "");
    }

    public static String getPassword(Context context){
        SharedPreferences prefs = context.getSharedPreferences("automaticlogin", MODE_PRIVATE);
        return prefs.getString("password", "");
    }

    public static String getUserID(Context context){
        SharedPreferences prefs = context.getSharedPreferences("automaticlogin", MODE_PRIVATE);
        return prefs.getString("userid", "");
    }




    public static boolean isInternetConnected(Context mContext) {

        try {
            ConnectivityManager connect = null;
            connect = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connect != null) {
                NetworkInfo resultMobile = connect
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                NetworkInfo resultWifi = connect
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if ((resultMobile != null && resultMobile
                        .isConnectedOrConnecting())
                        || (resultWifi != null && resultWifi
                        .isConnectedOrConnecting())) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void showAlertDialog(Context c, String title, String message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(c);
        builder.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static void showToast(Context c, String message){
        Toast.makeText(c, message, Toast.LENGTH_LONG).show();
    }



    public static void showAlertBox(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setTitle(title)
                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }








}
