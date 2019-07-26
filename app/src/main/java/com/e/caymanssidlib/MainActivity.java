package com.e.caymanssidlib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cyman.libssid.activity.LoginActivity;
import cyman.libssid.callbackfunction.onCallBackFunction;

public class MainActivity extends AppCompatActivity {

//    onCallBackFunction.OnLoginClickListener onLoginClickListener;
    LoginActivity.OnLoginClickListenerDone onLoginClickListenerDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        TextView textView=(TextView)findViewById(R.id.tv_login);




        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginActivity.LoginManagerConnect(MainActivity.this,"Dvyaw9pUECWZxg32ASibMsJ6vX8NKFQKH8V572wF4mWTac0dZoLjxgVblPNoJ6zi",onLoginClickListenerDone);
//                onCallBackFunction.LoginManager(MainActivity.this,"",onLoginClickListener);

            }
        });

        onLoginClickListenerDone=new LoginActivity.OnLoginClickListenerDone() {
            @Override
            public void onSuccess(String sucess) {

                TextView textView=(TextView)findViewById(R.id.tv_login);
                TextView authstatus=(TextView)findViewById(R.id.authstatus);
                textView.setText("Continue");
                authstatus.setText(""+sucess);
                Toast.makeText(MainActivity.this, ""+sucess, Toast.LENGTH_SHORT).show();
                textView.setEnabled(false);
                textView.setClickable(false);
            }

            @Override
            public void onError(String error) {

            }
        };


    }
}
