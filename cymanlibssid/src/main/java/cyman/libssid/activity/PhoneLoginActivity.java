package cyman.libssid.activity;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.telephony.TelephonyManager;
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
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import cyman.libssid.R;
import cyman.libssid.dialog.PhoneAndLogoutDialogClass;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.CommonUtils;
import cyman.libssid.util.CustomTextInputLayout;
import cyman.libssid.util.NetworkUtils;

public class PhoneLoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    CustomTextInputLayout middleNameTextInputLayout;
    EditText phoneEdittext, componentIdEdittext;
    TextView next;
    CountryCodePicker countryCodePicker;
    String selected_country_code_name = "KY", selected_country_code = "", phone, component_id, first_name = "", middle_name = "", last_name = "", dob = "";
    private GoogleApiClient mCredentialsApiClient;
    private static final int RC_HINT = 1000;
    private ProgressDialog mProgressDialog;
    PhoneAndLogoutDialogClass.OnLoginVerificationListenerDone onLoginClickListenerDone;

    ScrollView scrollView;
    static OnLoginClickListenerDone listenerDone;

    public interface OnLoginClickListenerDone {

        void onSuccess(String sucess);
        void onError(String error);
    }


    public static void LoginManagerConnect(Activity context, String packg, OnLoginClickListenerDone clickListener){
        listenerDone=clickListener;
        context.startActivity(new Intent(context,PhoneLoginActivity.class));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);



//        startActivity(new Intent(this,PhoneNumberActivity.class));

        mCredentialsApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


        onLoginClickListenerDone=new PhoneAndLogoutDialogClass.OnLoginVerificationListenerDone() {
            @Override
            public void onSuccess(String sucess) {

                Log.d("ClickListenerDone","---------------------------------"+sucess);

                mProgressDialog.show();
                apprequest(sucess);



            }

            @Override
            public void onError(String error) {

            }
        };


//        firstNameTextInputLayout=(CustomTextInputLayout)findViewById(R.id.first_name_textInput_layout) ;
        middleNameTextInputLayout=(CustomTextInputLayout)findViewById(R.id.middle_name_textInput_layout) ;
        scrollView=(ScrollView) findViewById(R.id.scrollView) ;
        next=(TextView)findViewById(R.id.tv_login) ;
        scrollView.setVisibility(View.INVISIBLE);


        phoneEdittext=(EditText)findViewById(R.id.phone_edittext) ;
//        emailEdittext=(EditText)findViewById(R.id.email_edittext) ;
        componentIdEdittext=(EditText)findViewById(R.id.component_id_edittext) ;
        countryCodePicker=(CountryCodePicker) findViewById(R.id.country_code_picker) ;
        countryCodePicker.setCountryForNameCode(selected_country_code_name);
        countryCodePicker.setAutoDetectedCountry(true);

        selected_country_code_name =countryCodePicker.getSelectedCountryNameCode();
        selected_country_code =countryCodePicker.getSelectedCountryCodeWithPlus();


        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code_name =countryCodePicker.getSelectedCountryNameCode();
                selected_country_code =countryCodePicker.getSelectedCountryCodeWithPlus();
            }
        });


        mProgressDialog = CommonUtils.showLoadingDialog(this);
        mProgressDialog.show();
        appregister();



        phoneEdittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showHint();
            }
        });

//        emailEdittext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
//                startActivityForResult(googlePicker, 123);
//            }
//        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SignUpStepOneActivity.this,SignUpStepTwoActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                startActivity(intent);
//                finish();

                nextFragment();
            }
        });




    }




    private String getMyPhoneNO() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;
    }

    private void nextFragment() {

//         email = emailEdittext.getText().toString().trim();
         phone = phoneEdittext.getText().toString().trim();
         component_id = componentIdEdittext.getText().toString().trim();
        Log.d("phone","----4-------"+component_id);
        if (NetworkUtils.isNetworkConnected(this)) {
            if (checkValidation("", phone, component_id)) {
//                phone = selected_country_code + phone;

                Log.d("phone","-1-------------"+phone);
                Log.d("phone","----2-------"+component_id);
                Log.d("phone","-3-------------"+phone);


//                verifyPhoneNumber();


//                phone=component_id+" "+phone;
                Log.d("phone", ":------ " + phone);
                new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, first_name, middle_name, last_name, dob, "", phone, component_id,onLoginClickListenerDone).show();

            }
        } else {

            Toast.makeText(this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
        }
    }


    private void verifyPhoneNumber() {

        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.PHONE_VERIFICATION_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        postRequestBuilder.addBodyParameter("phoneNo", phone);
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("phone_verification_rpns", ": " + response.toString());

                    if (response.getString("status").equals("1")) {
                        middleNameTextInputLayout.setErrorEnabled(false);

//                        apiHitForCheckEmail();


                        phone=component_id+" "+phone;
                        Log.d("phone", ":------ " + phone);
                        new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, first_name, middle_name, last_name, dob, "", phone, component_id,onLoginClickListenerDone).show();

                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        phoneEdittext.requestFocus();
                        middleNameTextInputLayout.setErrorEnabled(true);
                        middleNameTextInputLayout.setError(response.getString("message"));
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

    private boolean checkValidation(String email, String phone, String component_id) {

//        if (TextUtils.isEmpty(email.trim())) {
//            firstNameTextInputLayout.setError(getResources().getString(R.string.email_can_not_be_blank));
//            emailEdittext.requestFocus();
//            return false;
//        }
//
//        if (!CommonUtils.isEmailValid(email.trim())) {
//            firstNameTextInputLayout.setError(getResources().getString(R.string.invalid_email));
//           emailEdittext.requestFocus();
//            return false;
//        }

        if (TextUtils.isEmpty(phone.trim())) {
            middleNameTextInputLayout.setError(getResources().getString(R.string.phone_can_not_be_blank));
            phoneEdittext.requestFocus();
            return false;
        }

        if (phone.length() != 10) {
            middleNameTextInputLayout.setError(getResources().getString(R.string.invalid_phone));
            phoneEdittext.requestFocus();
            return false;
        }




        return true;
    }


//    private void apiHitForCheckEmail() {
//
//        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
//        ANRequest.PostRequestBuilder postRequestBuilder;
//        Priority priority;
//        ANRequest anRequest;
//
//        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
//        priority = Priority.MEDIUM;
//
//        postRequestBuilder.addBodyParameter("mode", "check-email");
////        postRequestBuilder.addBodyParameter("email", email);
//
//        postRequestBuilder.setPriority(priority);
//        postRequestBuilder.addHeaders("x-api-key", "123456");
//
//        anRequest = postRequestBuilder.build();
//        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("check_reponse_chk_email", ": " + response.toString());
//
//                if (response != null) {
//                    String message, status;
//                    try {
//                        message = response.getString("message");
//                        status = response.getString("status");
//
//                        if (status.equals("1")) {
//
//
//                            phone=component_id+" "+phone;
//                            Log.d("phone", ":------ " + phone);
//                            new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, first_name, middle_name, last_name, dob, "", phone, component_id).show();
//                        } else {
//                            if (message != null) {
//                                Toast.makeText(PhoneLoginActivity.this, message, Toast.LENGTH_LONG).show();
//                            }
////                            firstNameTextInputLayout.setError(getResources().getString(R.string.email_already_exists));
////                            emailEdittext.requestFocus();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(ANError anError) {
//
//                Toast.makeText(PhoneLoginActivity.this, anError.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("error_change_pass", ": " + anError.getMessage());
//            }
//        });
//    }



    private void showHint() {
//        ui.clearKeyboard();
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
            Log.e("Abhi", "Could not start hint picker Intent", e);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Abhi", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Abhi", "GoogleApiClient is suspended with cause code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Abhi", "GoogleApiClient failed to connect: " + connectionResult);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);

                if (cred.getId().length() > 4)
                {
//                    lastFourDigits = cred.getId().substring(cred.getId().length() - 10);
//                    phoneEdittext.setText(cred.getId().substring(cred.getId().length() - 10));
                    phoneEdittext.setText(cred.getId());

                    phone = phoneEdittext.getText().toString().trim();
//                    component_id = componentIdEdittext.getText().toString().trim();
//                    phone=component_id+" "+phone;
                    Log.d("phone", ":------ " + phone);
                    new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, PhoneLoginActivity.this, first_name, middle_name, last_name, dob, "", phone, "",onLoginClickListenerDone).show();

                }
                else
                {
//                    lastFourDigits = cred.getId();
                    phoneEdittext.setText(cred.getId());
                }
//                phoneEdittext.setText(cred.getId());
            } else {
//                ui.focusPhoneNumber();
            }
        }

//        if (requestCode == 123 && resultCode == RESULT_OK) {
//            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//            emailEdittext.setText(""+accountName);
//
//            sendEmailVerification();
//        }


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




    private void appregister() {

        String url = AppConstants.BASE_URL + AppConstants.SOCIAL_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.APP_VERIFICATION_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");


        postRequestBuilder.addBodyParameter("appName", "run2play");
        postRequestBuilder.addBodyParameter("accessToken", "Dvyaw9pUECWZxg32ASibMsJ6vX8NKFQKH8V572wF4mWTac0dZoLjxgVblPNoJ6zi");

        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("phone_verification_rpns", ": " + response.toString());

                    if (response.getString("status").equals("1")) {

                        scrollView.setVisibility(View.VISIBLE );
                        showHint();

                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        onBackPressed();

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
                onBackPressed();
                Log.d("phone_verification_err", ": " + anError.getMessage());
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
        postRequestBuilder.addHeaders("access-token", "Dvyaw9pUECWZxg32ASibMsJ6vX8NKFQKH8V572wF4mWTac0dZoLjxgVblPNoJ6zi");
        postRequestBuilder.addBodyParameter("uid", uid);


        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();
                try {
                    Log.d("phone_verification_rpns", ": " + response.toString());

                    if (response.getString("status").equals("1")) {

                        String userNonce=response.getJSONObject("response").getString("userNonce");
                        mProgressDialog.hide();
                        showPopup(userNonce);

                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        onBackPressed();

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
                onBackPressed();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }



    public void showPopup(final String userNonce){

        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");

        // set values for custom dialog components - text, image and button

        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.textView5);
        Button acceptButton = (Button) dialog.findViewById(R.id.button);

        // if decline button is clicked, close the custom dialog
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                apprequestAccepet(userNonce);
                dialog.dismiss();
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listenerDone.onError("Failed");
                onBackPressed();
                dialog.dismiss();
            }
        });

    }



    private void apprequestAccepet(String userNonce) {
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
                        listenerDone.onSuccess(response.toString());
                        onBackPressed();
                        finish();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        onBackPressed();

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
                onBackPressed();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }


}
