package cyman.libssid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import cyman.libssid.R;

public class AutoLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login);
    }

    public static void LoginManagerConnect(Activity context, String packg){
//        listenerDone=clickListener;
//        context.startActivity(new Intent(context,LoginActivity.class));

    }

}
