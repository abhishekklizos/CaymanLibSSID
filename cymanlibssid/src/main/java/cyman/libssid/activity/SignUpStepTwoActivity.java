package cyman.libssid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import cyman.libssid.R;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.CustomTextInputLayout;
import cyman.libssid.util.NetworkUtils;
import cyman.libssid.util.PasswordValidator;

public class SignUpStepTwoActivity extends AppCompatActivity {

    CustomTextInputLayout confirmPasswordTextInputLayout,passwordTextInputLayout;
    EditText passwordEdittext,confirmPasswordEdittext;
    String first_name = "", middle_name = "", last_name = "", dob = "", email = "", phone = "",
            component_id = "", address1 = "", address2 = "", selected_city_details = "",
            selected_state_details = "", selected_country_details = "", zipcode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step_two);


        TextView textView=(TextView)findViewById(R.id.tv_login);

        confirmPasswordTextInputLayout=(CustomTextInputLayout)findViewById(R.id.confirm_password_textInput_layout);
        passwordTextInputLayout=(CustomTextInputLayout)findViewById(R.id.password_textInput_layout);
        passwordEdittext=(EditText)findViewById(R.id.password_edittext);
        confirmPasswordEdittext=(EditText) findViewById(R.id.confirm_password_edittext);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishToNext();

            }
        });


        setupParent(findViewById(R.id.parnt));
    }



    public void finishToNext() {
        String password = passwordEdittext.getText().toString().trim();
        String confirm_password = confirmPasswordEdittext.getText().toString().trim();
        if (!TextUtils.isEmpty(password)) {
            if (new PasswordValidator().validate(password)) {
                if (password.equals(confirm_password)) {
                    if (NetworkUtils.isNetworkConnected(this)) {
//                        showLoading();
//                        hitApi(first_name, middle_name, last_name, dob, email,
//                                phone, address1, address2, selected_city_details,
//                                selected_state_details, selected_country_details, zipcode,
//                                confirm_password);

                        Intent intent=new Intent();
                        intent.putExtra("Key","You have been successfully authenticated");
                        setResult(10,intent);
                        finish();
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    confirmPasswordTextInputLayout.setError(this.getResources().getString(R.string.password_dont_match));
                }
            } else {
                passwordTextInputLayout.setErrorEnabled(true);
                passwordTextInputLayout.setError(this.getResources().getString(R.string.invalid_password));
                passwordEdittext.requestFocus();
                showSoftKeyBoard(passwordEdittext);
            }
        } else {
            passwordTextInputLayout.setErrorEnabled(true);
            passwordTextInputLayout.setError(this.getResources().getString(R.string.password_can_not_be_blank));
            passwordEdittext.requestFocus();
            showSoftKeyBoard(passwordEdittext);
        }
    }


    private void hitApi(String first_name, String middle_name, String last_name, String dob,
                        String email, String phone, String address1, String address2,
                        String selected_city_details, String selected_state_details,
                        String selected_country_details, String zipcode, String confirm_password) {



        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;
        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.SIGNUP_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        postRequestBuilder.addBodyParameter("firstName", first_name);
        postRequestBuilder.addBodyParameter("middleName", middle_name);
        postRequestBuilder.addBodyParameter("lastName", last_name);
        postRequestBuilder.addBodyParameter("dateofBirth", dob);
        postRequestBuilder.addBodyParameter("email", email);
        postRequestBuilder.addBodyParameter("phoneNo", phone);
        postRequestBuilder.addBodyParameter("addressOne", address1);
        postRequestBuilder.addBodyParameter("addressTwo", address2);
        postRequestBuilder.addBodyParameter("city", selected_city_details);
        postRequestBuilder.addBodyParameter("state", selected_state_details);
        postRequestBuilder.addBodyParameter("country", selected_country_details);
        postRequestBuilder.addBodyParameter("zipcode", zipcode);
        postRequestBuilder.addBodyParameter("password", confirm_password);

        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
//                hideLoading();


                Intent intent=new Intent();
                intent.putExtra("Key","You have been successfully authenticated");
                setResult(10,intent);
                finish();

            }

            @Override
            public void onError(ANError anError) {
                // handle error
//                hideLoading();
                Log.d("phone_verification_err", ": " + anError.getMessage());
            }
        });
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void showSoftKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }


    private void setupParent(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

}
