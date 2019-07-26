package cyman.libssid.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import cyman.libssid.R;
import cyman.libssid.activity.PhoneLoginActivity;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.PinEntryEditText;
import cyman.libssid.util.avloaderindicator.AVLoadingIndicatorView;

public class EmailVerificationDialog extends Dialog {

    private String email = "";
    private Context activity;
    private PinEntryEditText edittext_email_verification;
    private TextView btn_positive, btn_negative, resend_code_tv;
    private AVLoadingIndicatorView avLoadingIndicatorView;


    public interface OnEmailVerificationListenerDone {

        void onSuccess(String sucess);
        void onError(String error);
    }

    static OnEmailVerificationListenerDone onEmailVerificationListenerDone;



    public EmailVerificationDialog(Context activity, String email,OnEmailVerificationListenerDone onEmailVerificationListener) {
        super(activity);
        this.activity = activity;
        this.email = email;
        this.onEmailVerificationListenerDone=onEmailVerificationListener;
    }

    public void setEmailCode(String email, String emailCode) {
        if (email != null) {
            this.email = email;
        }
        edittext_email_verification.setText(emailCode);
    }


    @Override
    protected void onStart() {
        super.onStart();
        edittext_email_verification.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.email_verification_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        edittext_email_verification = findViewById(R.id.edittext_email_verification);
        btn_negative = findViewById(R.id.btn_negative);
        btn_positive = findViewById(R.id.btn_positive);
        resend_code_tv = findViewById(R.id.resend_code_tv);
        avLoadingIndicatorView = findViewById(R.id.progressbar);
//        edittext_email_verification.mNumChars = 4;



        resend_code_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiHitForResend(edittext_email_verification);
            }
        });

        edittext_email_verification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    edittext_email_verification.setError(null);
                    btn_positive.setEnabled(true);
                } else {
                    btn_positive.setEnabled(false);
                    /*btn_positive.setEnabled(false);
                    edittext_email_verification.setError(activity.getResources().getString(R.string.code_cant_be_blank));*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 4) {
                    apiHitForVerify(edittext_email_verification);
                }
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edittext_email_verification.getText().toString().trim())) {
                    if (edittext_email_verification.getText().toString().trim().length() == 4) {
                        apiHitForVerify(edittext_email_verification);
                    } else {
                        edittext_email_verification.setError(activity.getResources().getString(R.string.invaild_code));
                    }

                } else {
                    edittext_email_verification.setError(activity.getResources().getString(R.string.code_cant_be_blank));
//                    Toast.makeText(activity, activity.getResources().getString(R.string.code_cant_be_blank), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void apiHitForVerify(View view) {

        edittext_email_verification.setVisibility(View.INVISIBLE);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        hideKeyboard(view);
        btn_negative.setVisibility(View.GONE);
        btn_positive.setVisibility(View.GONE);
        resend_code_tv.setVisibility(View.INVISIBLE);

        /*OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();*/
        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;

        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
//        postRequestBuilder.setOkHttpClient(okHttpClient); // passing a custom okHttpClient
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.EMAIL_VERIFICATION_MODE);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        postRequestBuilder.addBodyParameter("email", email);
        postRequestBuilder.addBodyParameter("verificationCode", edittext_email_verification.getText().toString().trim());
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                edittext_email_verification.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                resend_code_tv.setVisibility(View.VISIBLE);
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);

                try {
                    Toast.makeText(activity, response.getString("message"), Toast.LENGTH_SHORT).show();
                    if (response.getString("status").equals("1")) {

                        onEmailVerificationListenerDone.onSuccess("1");
                        dismiss();
                    }else {

                        onEmailVerificationListenerDone.onError("0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                // handle error
                edittext_email_verification.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);
                resend_code_tv.setVisibility(View.VISIBLE);
                Log.d("email_verification_err", ": " + anError.getMessage());
//                showKeyboard(view);
            }
        });
    }

    private void apiHitForResend(View view) {
        edittext_email_verification.setVisibility(View.GONE);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        btn_positive.setEnabled(false);
        btn_negative.setEnabled(false);
        hideKeyboard(view);

        String url = AppConstants.BASE_URL + AppConstants.MULTIPLE_USAGE_PART_URL;
        ANRequest.PostRequestBuilder postRequestBuilder;
        Priority priority;
        ANRequest anRequest;

        postRequestBuilder = new ANRequest.PostRequestBuilder(url);
        priority = Priority.MEDIUM;
        postRequestBuilder.addBodyParameter("mode", AppConstants.RESEND_EMAIL_CODE_MODE);
        postRequestBuilder.addBodyParameter("email", email);
        postRequestBuilder.setPriority(priority);
        postRequestBuilder.addHeaders("x-api-key", "123456");
        anRequest = postRequestBuilder.build();
        anRequest.getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                // do anything with response
                edittext_email_verification.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                btn_positive.setEnabled(true);
                btn_negative.setEnabled(true);

                edittext_email_verification.requestFocus();
                Toast.makeText(activity, activity.getResources().getString(R.string.code_is_sent_to_email), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(ANError anError) {
                // handle error
                edittext_email_verification.setVisibility(View.VISIBLE);
                avLoadingIndicatorView.setVisibility(View.GONE);
                Log.d("email_resend_err", ": " + anError.getMessage());
            }
        });

    }


    private void hideKeyboard(View view) {

//        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Log.d("cac",": true");
            }
        }
    }

    public void setBlankToCodeEdittext() {
        if (edittext_email_verification != null) {
            edittext_email_verification.setText("");
        }
    }

    private void showKeyboard(View view){
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)activity.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm!=null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}
