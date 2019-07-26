package cyman.libssid.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import cyman.libssid.R;
import cyman.libssid.api.CommonResponse;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.CommonUtils;
import cyman.libssid.util.CustomTextInputLayout;

public class LoginActivity extends AppCompatActivity {


    CustomTextInputLayout email_textInput_layout,password_textInput_layout;
    EditText email_edittext,password_edittext;
    TextView tv_login;
    static String appKey="";

    static ProgressDialog mProgressDialog;

    static OnLoginClickListenerDone listenerDone;

    public interface OnLoginClickListenerDone {

        void onSuccess(String sucess);
        void onError(String error);
    }


    public static void LoginManagerConnect(final Activity context, String packg, OnLoginClickListenerDone clickListener){
        listenerDone=clickListener;
        appKey=packg;

        mProgressDialog = CommonUtils.showLoadingDialog(context);

        mProgressDialog.show();

        String url = AppConstants.BASE_URL + AppConstants.SOCIAL_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.HIGH;
        postRequestBuilder.addBodyParameter("mode", AppConstants.APP_VERIFICATION_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");


        postRequestBuilder.addBodyParameter("appName", "run2play");
        postRequestBuilder.addBodyParameter("accessToken", packg);

        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {

                CommonResponse commonResponse = new Gson().fromJson(response.toString(), CommonResponse.class);
//                hideLoading();
                Log.d("phone_verification_rpns", ": " + response.toString());

                if (commonResponse.getStatus().equals("1")) {

                    mProgressDialog.hide();
                    commonResponse.getResponse().setAppKey(appKey);
                    Intent intent=new Intent(context,PhoneLoginActivity.class);
                    intent.putExtra("response",commonResponse.getResponse());
                    context.startActivityForResult(intent,10);

                } else {
                    listenerDone.onSuccess("Not a Valid Key");
//                    context.onBackPressed();
//                    context.finish();
                    Log.d("phone_verification_rpns", ": 123" );
                    mProgressDialog.hide();
                }


            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                mProgressDialog.hide();
                listenerDone.onSuccess("Not a Valid Key");
//                context.onBackPressed();
//                context.finish();
                Log.d("phone_verification_rpns", ": 213" );
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });




    }




    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = CommonUtils.showLoadingDialog(this);




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

//    private boolean isVarified() {
//
//        if (email_edittext.getText().toString()!=null && email_edittext.getText().toString().length()>0)
//        {
//            if (password_edittext.getText().toString()!=null && password_edittext.getText().toString().length()>0)
//            {
//                return true;
//            }
//            else {
//                password_edittext.setError(getString(R.string.password_can_not_be_blank));
//                return false;
//            }
//
//        }
//        else {
//
//            email_textInput_layout.setError(getString(R.string.email_can_not_be_blank));
//            return false;
//        }
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("requestCode","-------1---5------"+requestCode);
        Log.d("requestCode","------2-----5------"+resultCode);
//        Log.d("requestCode","------2--5---------"+data.getExtras().getString("Key"));


//        Log.d("requestCodeLogn", ": " + data.getExtras().getString("MESSAGE"));

//        if(requestCode==10)
//        {
//
//            listenerDone.onSuccess(data.getExtras().getString("MESSAGE"));
//            onBackPressed();
//            finish();
//        }
//        else {
//            listenerDone.onSuccess("Failed");
//            onBackPressed();
//            finish();
//        }

//        listenerDone.onSuccess("You have been successfully authenticated");
//        onBackPressed();
//        finish();
    }



//    private static Boolean appRegister(Activity activity) {
//
//        String url = AppConstants.BASE_URL + AppConstants.SOCIAL_USAGE_PART_URL;
//        ANRequest.PostRequestBuilder postRequestBuilder;
//        Priority priority;
//        ANRequest anRequest;
//        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
//        priority = Priority.HIGH;
//        postRequestBuilder.addBodyParameter("mode", AppConstants.APP_VERIFICATION_MODE);
//        postRequestBuilder.setPriority(priority);
//        postRequestBuilder.addHeaders("x-api-key", "123456");
//
//
//        postRequestBuilder.addBodyParameter("appName", "run2play");
//        postRequestBuilder.addBodyParameter("accessToken", "Dvyaw9pUECWZxg32ASibMsJ6vX8NKFQKH8V572wF4mWTac0dZoLjxgVblPNoJ6zi");
//
//        anRequest = postRequestBuilder.build();
//        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                CommonResponse commonResponse = new Gson().fromJson(response.toString(), CommonResponse.class);
////                hideLoading();
//                Log.d("phone_verification_rpns", ": " + response.toString());
//
//                if (commonResponse.getStatus().equals("1")) {
//                    verified=true;
//
//                    Log.d("phone_verification_rpns", ": 321" );
//                    mProgressDialog.hide();
//
//                } else {
//                    verified=false;
//                    Log.d("phone_verification_rpns", ": 123" );
//                    mProgressDialog.hide();
//                }
//
//
//            }
//
//            @Override
//            public void onError(ANError anError) {
//                // handle error
////                hideLoading();
//                mProgressDialog.hide();
//                verified=false;
//                Log.d("phone_verification_rpns", ": 213" );
//                Log.d("phone_verification_err", ": " + anError.getMessage());
//            }
//        });
//
//        return verified;
//    }


}
