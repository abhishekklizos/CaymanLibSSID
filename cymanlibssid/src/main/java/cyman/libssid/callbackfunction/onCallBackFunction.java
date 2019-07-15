package cyman.libssid.callbackfunction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cyman.libssid.activity.LoginActivity;

public class onCallBackFunction {



    public interface OnLoginClickListener {
        void OnLoginClickListener(String success);
        void onSuccess();
        void onError();
    }

    static OnLoginClickListener listener;


    public static void LoginManager(Activity context, String packg, OnLoginClickListener clickListener){
        listener=clickListener;

        Intent intent=new Intent(context, LoginActivity.class);
        context.startActivity(intent);// Activity is started with requestCode 2

    }

}
