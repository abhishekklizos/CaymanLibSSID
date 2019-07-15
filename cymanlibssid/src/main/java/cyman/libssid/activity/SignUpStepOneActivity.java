package cyman.libssid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import cyman.libssid.R;
import cyman.libssid.dialog.PhoneAndLogoutDialogClass;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.CommonUtils;
import cyman.libssid.util.CustomTextInputLayout;
import cyman.libssid.util.NetworkUtils;

public class SignUpStepOneActivity extends AppCompatActivity {

    CustomTextInputLayout middleNameTextInputLayout,firstNameTextInputLayout;
    EditText phoneEdittext,emailEdittext,componentIdEdittext;
    TextView next;
    String selected_country_code_name = "KY", selected_country_code = "",email,phone,component_id,first_name = "", middle_name = "", last_name = "",dob="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step_one);


        firstNameTextInputLayout=(CustomTextInputLayout)findViewById(R.id.first_name_textInput_layout) ;
        middleNameTextInputLayout=(CustomTextInputLayout)findViewById(R.id.middle_name_textInput_layout) ;
        next=(TextView)findViewById(R.id.tv_login) ;


        phoneEdittext=(EditText)findViewById(R.id.phone_edittext) ;
        emailEdittext=(EditText)findViewById(R.id.email_edittext) ;
        componentIdEdittext=(EditText)findViewById(R.id.component_id_edittext) ;


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

    private void nextFragment() {

         email = emailEdittext.getText().toString().trim();
         phone = phoneEdittext.getText().toString().trim();
         component_id = componentIdEdittext.getText().toString().trim();

        if (NetworkUtils.isNetworkConnected(this)) {
            if (checkValidation(email, phone, component_id)) {
                phone = selected_country_code + phone;

                verifyPhoneNumber();
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

                        apiHitForCheckEmail();
                    } else {
                        Toast.makeText(SignUpStepOneActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
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

        if (TextUtils.isEmpty(email.trim())) {
            firstNameTextInputLayout.setError(getResources().getString(R.string.email_can_not_be_blank));
            emailEdittext.requestFocus();
            return false;
        }

        if (!CommonUtils.isEmailValid(email.trim())) {
            firstNameTextInputLayout.setError(getResources().getString(R.string.invalid_email));
           emailEdittext.requestFocus();
            return false;
        }

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

                            new PhoneAndLogoutDialogClass(AppConstants.OTP_INPUT, SignUpStepOneActivity.this, first_name, middle_name, last_name, dob, email, phone, component_id).show();
                        } else {
                            if (message != null) {
                                Toast.makeText(SignUpStepOneActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                            firstNameTextInputLayout.setError(getResources().getString(R.string.email_already_exists));
                            emailEdittext.requestFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(ANError anError) {

                Toast.makeText(SignUpStepOneActivity.this, anError.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("error_change_pass", ": " + anError.getMessage());
            }
        });
    }


}
