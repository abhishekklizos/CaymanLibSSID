package cyman.libssid.activity;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import cyman.libssid.R;
import cyman.libssid.api.CommonResponse;
import cyman.libssid.api.Response;
import cyman.libssid.dialog.EmailVerificationDialog;
import cyman.libssid.dialog.PhoneAndLogoutDialogClass;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.CommonUtils;
import cyman.libssid.util.CustomTextInputLayout;
import cyman.libssid.util.NetworkUtils;

public class PhoneLoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    /*Define Layout Attributes*/
    CustomTextInputLayout email_textInput_layout,phoneno_textInput_layout;
    EditText email_edittext, phone_edittext;
    TextView login_click;


    /*Define Variable*/
    String  phone="",email="";
    private GoogleApiClient mCredentialsApiClient;
    private static final int RC_HINT = 1000;
    private ProgressDialog mProgressDialog;
    Response commonResponse=new Response();
    PhoneAndLogoutDialogClass.OnLoginVerificationListenerDone onLoginClickListenerDone;
    EmailVerificationDialog.OnEmailVerificationListenerDone onEmailVerificationListenerDone;
    static OnLoginClickListenerDone listenerDone;
    Boolean isEmailCheck=false;

    public interface OnLoginClickListenerDone {

        void onSuccess(String sucess);
        void onError(String error);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        mProgressDialog = CommonUtils.showLoadingDialog(this);

        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        onLoginClickListenerDone=new PhoneAndLogoutDialogClass.OnLoginVerificationListenerDone() {
            @Override
            public void onSuccess(String sucess) {

                if (isEmailCheck){

                    mProgressDialog.show();
                    apprequest(sucess);
                }
                else {
                    mProgressDialog.show();
                    email_textInput_layout.setVisibility(View.VISIBLE);
                    showHint("email");
                }


            }

            @Override
            public void onError(String error) {


            }
        };


        onEmailVerificationListenerDone=new EmailVerificationDialog.OnEmailVerificationListenerDone() {
            @Override
            public void onSuccess(String sucess) {


//                apprequest(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Intent intent=new Intent();
                intent.putExtra("MESSAGE",commonResponse);
                setResult(10,intent);
                finish();
            }

            @Override
            public void onError(String error) {

            }
        };

        /*Define Layout*/

        email_textInput_layout=(CustomTextInputLayout)findViewById(R.id.email_textInput_layout);
        phoneno_textInput_layout=(CustomTextInputLayout)findViewById(R.id.phoneno_textInput_layout);

        login_click=(TextView)findViewById(R.id.tv_login) ;

        email_edittext=(EditText)findViewById(R.id.email_edittext) ;
        phone_edittext=(EditText)findViewById(R.id.phone_edittext) ;
        email_textInput_layout.setVisibility(View.GONE);

        /*Show Progress for Check App is registered or not*/

         commonResponse=(Response) getIntent().getSerializableExtra("response");

         Log.d("response","------------------------------"+new Gson().toJson(commonResponse));




        mProgressDialog.show();

        showHint("phone");


        /*Listner for phone*/

        phoneno_textInput_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHint("phone");
            }
        });

        /*Listner for mail check*/

        email_textInput_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHint("email");

            }
        });

        /*Listner For Login*/

        login_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextFragment();
            }
        });


    }





    private void nextFragment() {

         email = email_edittext.getText().toString().trim();
         phone = phone_edittext.getText().toString().trim();

        if (NetworkUtils.isNetworkConnected(this)) {
            if (checkValidation("", "")) {


            }
        } else {

            Toast.makeText(this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
        }
    }


    private void verifyCredential(final String type) {

        String phoneforApi="";



        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        final ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;

        if (type.equals("email")){

            postRequestBuilder.addBodyParameter("mode", "check-email");
            postRequestBuilder.addBodyParameter("email", email);

        }
        else if (type.equals("phone")){


            if (phone == null || phone.length() < 3) {
                phoneforApi = phone;
            } else {
                phoneforApi = phone.substring(phone.length() - 10);

            }


            Log.d("phoneforApi","---"+phoneforApi);
            postRequestBuilder.addBodyParameter("mode", AppConstants.PHONE_VERIFICATION_MODE);
            postRequestBuilder.addBodyParameter("phoneNo", phoneforApi);
        }


        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();

                Log.d("phone_verification_rpns", ":--- " +response.toString());
                try {
                    if (type.equals("email")){

                        if (response.getString("status").equals("1")){

                            appRegestration();
                        }
                        else {

//                            email_textInput_layout.setError(getResources().getString(R.string.email_already_exists));
//                            email_edittext.requestFocus();
                            apprequest(FirebaseAuth.getInstance().getUid());
//                            new EmailVerificationDialog(PhoneLoginActivity.this,email,onEmailVerificationListenerDone).show();
                        }



                    }
                    else if (type.equals("phone")){

                        if (response.getString("status").equals("1")) {
                            phoneno_textInput_layout.setErrorEnabled(false);
                            isEmailCheck=false;
                            new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, email, phone,onLoginClickListenerDone).show();

                        } else {
//
                            isEmailCheck=true;
                            new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, email, phone,onLoginClickListenerDone).show();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }


    private void apiHitForCheckEmail() {

        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;

        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;

        postRequestBuilder.addBodyParameter("mode", "check-email");
        postRequestBuilder.addBodyParameter("email", email);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("check_reponse_chk_email", ": " + response.toString());

                if (response != null) {
                    String message, status;
                    try {
                        message = response.getString("message");
                        status = response.getString("status");

                        if (status.equals("1")) {


                        } else {
                            if (message != null) {
                                Toast.makeText(PhoneLoginActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                            email_textInput_layout.setError(getResources().getString(R.string.email_already_exists));
                            email_edittext.requestFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {

                Toast.makeText(PhoneLoginActivity.this, anError.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error_change_pass", ": " + anError.getMessage());
            }
        });
    }


    private boolean checkValidation(String value,String type) {


        if (TextUtils.isEmpty(value.trim())) {
            return false;
        }


        return true;
    }





    private void showHint(String type) {

        if (type.equals("email")){

            Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
            startActivityForResult(googlePicker, 123);
        }
        else if (type.equals("phone")){

            HintRequest hintRequest = new HintRequest.Builder()
                    .setHintPickerConfig(new CredentialPickerConfig.Builder()
                            .setShowCancelButton(true)
                            .build())
                    .setPhoneNumberIdentifierSupported(true)
                    .build();

            PendingIntent intent =
                    Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest);
            try {
                startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {

            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProgressDialog.hide();

        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);

//                Log.d("phone",cred.getId());
                if (cred.getId().length() > 4)
                {
                    phone_edittext.setText(cred.getId());
                    phone = phone_edittext.getText().toString().trim();


                    verifyCredential("phone");


                }
                else
                {
                    phone_edittext.setText(cred.getId());
                }
            }
        }
        else if (requestCode == 123 && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            email_edittext.setText(""+accountName);
            email=email_edittext.getText().toString().trim();

            verifyCredential("email");
        }
//        else {
//
////            phone_edittext.setText("+918017749625");
////            phone = phone_edittext.getText().toString().trim();
////            verifyCredential("phone");
//        }
//        phone_edittext.setText("+918017749625");
//        phone = phone_edittext.getText().toString().trim();
//        verifyCredential("phone");

//        listenerDone.onSuccess("You have been successfully authenticated");
//        onBackPressed();
//        finish();
    }

    public void sendEmailVerification() {
        // [START send_email_verification]
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Abhishek", "Email sent.");
                        }
                    }
                });
        // [END send_email_verification]
    }

    public void showPopup(final String userNonce){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Custom Dialog");
        dialog.show();
        Button declineButton = (Button) dialog.findViewById(R.id.textView5);
        Button acceptButton = (Button) dialog.findViewById(R.id.button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                appRequestAccepet(userNonce);
                dialog.dismiss();
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listenerDone.onError("Failed");
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","Decline");
                setResult(11,intent);
                finish();
                dialog.dismiss();
            }
        });

    }



    private void apprequest(String uid) {

        String url = AppConstants.BASE_URL + AppConstants.SOCIAL_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.APP_REQUEST_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("access-token", commonResponse.getAppKey());
        postRequestBuilder.addBodyParameter("uid", uid);
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("Abhishek", ": ------1" + response.toString());

                    if (response.getString("status").equals("1")) {

                        Log.d("Abhishek", ": ------2" + response.toString());
                        String userNonce=response.getJSONObject("response").getString("userNonce");
                        mProgressDialog.hide();
                        showPopup(userNonce);

                    } else {
                        Log.d("Abhishek", ": ------3" + response.toString());
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent=new Intent();
//                        intent.putExtra("MESSAGE",response.toString());
                        intent.putExtra("message","1234");
                        setResult(10,intent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Abhishek", ": ------4" + response.toString());
                }

                mProgressDialog.hide();
            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                mProgressDialog.hide();
                Log.d("Abhishek", ": ------5");
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","Already Regestered");
                setResult(11,intent);
                finish();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }


    private void appRequestAccepet(String userNonce) {
        mProgressDialog.show();
        String url = AppConstants.BASE_URL + AppConstants.SOCIAL_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.APP_REQUEST_ACCEPET_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("access-token", "Dvyaw9pUECWZxg32ASibMsJ6vX8NKFQKH8V572wF4mWTac0dZoLjxgVblPNoJ6zi");
        postRequestBuilder.addBodyParameter("userNonce", userNonce);
        postRequestBuilder.addBodyParameter("walletName", "SSID_U_USER_85");
        postRequestBuilder.addBodyParameter("walletKey", "178bec35a98c39ebdd6b68fddcae012f");

        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("phone_verification_rpns", ": " + response.toString());

                    if (response.getString("status").equals("1")) {
                        mProgressDialog.hide();
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE",response.toString());
                        setResult(10,intent);
                        finish();

                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE",response.toString());
                        setResult(11,intent);
                        finish();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.hide();
            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                mProgressDialog.hide();
                Toast.makeText(PhoneLoginActivity.this,"Error", Toast.LENGTH_LONG).show();
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","1234567890");
                setResult(11,intent);
                finish();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }



    private void appRegestration() {
        mProgressDialog.show();
        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.SIGNUP_MODE);
        postRequestBuilder.setPriority(priority);

        postRequestBuilder.addHeaders("x-api-key", "123456");
        postRequestBuilder.addBodyParameter("email", email);
        postRequestBuilder.addBodyParameter("phoneNo", phone);
        postRequestBuilder.addBodyParameter("password", FirebaseAuth.getInstance().getCurrentUser().getUid());

        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("phone_verification_rpns", ": " + response.toString());

                    commonResponse = new Gson().fromJson(response.getJSONObject("response").toString(), Response.class);
                    if (response.getString("status").equals("1")) {
                        mProgressDialog.hide();
                        new EmailVerificationDialog(PhoneLoginActivity.this,email,onEmailVerificationListenerDone).show();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mProgressDialog.hide();
            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                mProgressDialog.hide();

                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



}
