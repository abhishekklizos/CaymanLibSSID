package cyman.libssid.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cyman.libssid.R;
import cyman.libssid.callbackfunction.onCallBackFunction;
import cyman.libssid.util.CustomTextInputLayout;

public class LoginActivity extends AppCompatActivity {


    CustomTextInputLayout email_textInput_layout,password_textInput_layout;
    EditText email_edittext,password_edittext;
    TextView tv_login;

    static OnLoginClickListenerDone listenerDone;

    public interface OnLoginClickListenerDone {

        void onSuccess(String sucess);
        void onError(String error);
    }


    public static void LoginManagerConnect(Activity context, String packg, OnLoginClickListenerDone clickListener){
        listenerDone=clickListener;
        context.startActivity(new Intent(context,PhoneLoginActivity.class));

    }




    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);





        TextView tvSignup=(TextView)findViewById(R.id.tv_signup);
         email_textInput_layout=(CustomTextInputLayout)findViewById(R.id.email_textInput_layout);
         password_textInput_layout=(CustomTextInputLayout)findViewById(R.id.password_textInput_layout);
         email_edittext=(EditText)findViewById(R.id.email_edittext);
         password_edittext=(EditText)findViewById(R.id.password_edittext);
         tv_login=(TextView) findViewById(R.id.tv_login);

        Spannable word = new SpannableString(getString(R.string.dont_have_account));
        tvSignup.setText(word + " ");
        Spannable word1 = new SpannableString(getString(R.string.signup));
        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        word1.setSpan(bss, 0, word1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        word1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_glossy_type)), 0, word1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvSignup.append(word1);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUpStepOneActivity.class);
                startActivityForResult(intent, 2001);

//                Intent intent = new Intent(ActivityOne.this,ActivityTwo.class);
//                startActivityForResult(intent,1010);
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    private boolean isVarified() {

        if (email_edittext.getText().toString()!=null && email_edittext.getText().toString().length()>0)
        {
            if (password_edittext.getText().toString()!=null && password_edittext.getText().toString().length()>0)
            {
                return true;
            }
            else {
                password_edittext.setError(getString(R.string.password_can_not_be_blank));
                return false;
            }

        }
        else {

            email_textInput_layout.setError(getString(R.string.email_can_not_be_blank));
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("requestCode","-------1---5------"+requestCode);
        Log.d("requestCode","------2-----5------"+resultCode);
        Log.d("requestCode","------2--5---------"+data.getExtras().getString("Key"));

        listenerDone.onSuccess("You have been successfully authenticated");
        onBackPressed();
        finish();
    }

}
