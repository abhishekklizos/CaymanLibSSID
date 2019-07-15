package cyman.libssid.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import cyman.libssid.R;
import cyman.libssid.activity.SignUpStepTwoActivity;
import cyman.libssid.util.AppConstants;
import cyman.libssid.util.PinEntryEditText;
import cyman.libssid.util.avloaderindicator.AVLoadingIndicatorView;

public class PhoneAndLogoutDialogClass extends Dialog implements View.OnClickListener{
    private FirebaseAuth mAuth;
    boolean b_otp_check = true;
    private String type = "";
    private TextView resend_otp_tv;
    private AVLoadingIndicatorView progressbar;
    private Activity activity;
    public Dialog d;
    private PinEntryEditText edittext_phone;
    private TextView btn_negative, btn_positive, tv_title;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String string_user_input;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    CountDownTimer countDownTimer = null;
    String first_name = "", middle_name = "", last_name = "", dob = "", email = "",
            phone = "", component_id = "";


    public PhoneAndLogoutDialogClass(String type, Activity a, String first_name, String middle_name, String last_name,
                                     String dob, String email, String phone, String component_id) {
        super(a);
        // TODO Auto-generated constructor stub
        this.type = type;
        this.activity = a;
        this.phone = phone;
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.component_id = component_id;
    }


    public PhoneAndLogoutDialogClass(String type, Activity activity) {
        super(activity);
        this.type = type;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.phone_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        btn_negative = findViewById(R.id.btn_negative);
        btn_positive = findViewById(R.id.btn_positive);
        progressbar = findViewById(R.id.progressbar);
        resend_otp_tv = findViewById(R.id.resend_otp_tv);
        tv_title = findViewById(R.id.tv_title);

        edittext_phone = findViewById(R.id.edittext_phone);
//        edittext_phone.mNumChars = 6;
        btn_negative.setOnClickListener(this);
        btn_positive.setOnClickListener(this);


        edittext_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("checK_one", ": " + true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edittext_phone.setError(false);
                Log.d("checK_two", ": " + true);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                Log.d("checK_three", ": " + true);
                if (!TextUtils.isEmpty(s.toString().trim()) && b_otp_check) {
                    if (s.toString().trim().length() == 6) {
                        b_otp_check = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                string_user_input = s.toString().trim();
                                Log.d("abc_def", string_user_input);
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, string_user_input);
                                signInWithPhoneAuthCredential(credential);
                            }
                        }, 200);
                    }
                }


            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);
                hideLoader();
                edittext_phone.setText(phoneAuthCredential.getSmsCode());
                showResendOTP();
                Log.d("completed: ", phoneAuthCredential.getSmsCode());
//                type = AppConstants.OTP_INPUT;
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }
                }, 1000);*/
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("failed_verification: ", e.getMessage());
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);
                hideLoader();
                edittext_phone.setText("");
                tv_title.setText(activity.getResources().getText(R.string.verification_failed));
                edittext_phone.setVisibility(View.GONE);
                btn_positive.setVisibility(View.GONE);
                Toast.makeText(activity, activity.getResources().getString(R.string.verification_failed), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                hideLoader();
                btn_negative.setVisibility(View.VISIBLE);
                btn_positive.setVisibility(View.VISIBLE);
                Log.d("sent: ", s);
                Toast.makeText(activity, activity.getResources().getString(R.string.otp_sent_to_ph), Toast.LENGTH_SHORT).show();
                tv_title.setText(activity.getResources().getString(R.string.enter_otp));
//                type = AppConstants.OTP_INPUT;
                edittext_phone.setText("");
                verificationId = s;
                mResendToken = forceResendingToken;
                showResendOTP();
                startCountdown();
                super.onCodeSent(s, forceResendingToken);
            }
        };


        resend_otp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phone, mResendToken);
            }
        });

        if (type.equals(AppConstants.LOG_OUT_CONFIRM)) {
            edittext_phone.setVisibility(View.GONE);
            tv_title.setText(activity.getResources().getString(R.string.are_you_sure_to_logout));
            btn_positive.setText(activity.getResources().getString(R.string.yes));
            btn_negative.setText(activity.getResources().getString(R.string.no));
            resend_otp_tv.setVisibility(View.GONE);
        } else if (type.equals(AppConstants.OTP_INPUT)) {
            PhoneAuthentication(phone);
        }

    }


    private void startCountdown() {

        resend_otp_tv.setEnabled(false);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                resend_otp_tv.setText(hms);
            }

            public void onFinish() {
                if (activity != null) {
                    resend_otp_tv.setText(activity.getString(R.string.resend_otp));
                    resend_otp_tv.setEnabled(true);
                }
            }
        }.start();
    }

    private void stopCounter() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            resend_otp_tv.setText(activity.getString(R.string.resend_otp));
            resend_otp_tv.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_negative) {
            dismiss();
        } else if (i == R.id.btn_positive) {//                submitToGoToNext();
            if (type.equals(AppConstants.OTP_INPUT)) {
                string_user_input = edittext_phone.getText().toString().trim();
                if (!TextUtils.isEmpty(string_user_input)) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, string_user_input);
                    signInWithPhoneAuthCredential(credential);
                } else {
                    edittext_phone.requestFocus();
                    edittext_phone.setError(activity.getResources().getString(R.string.otp_required));
                }
            } else if (type.equals(AppConstants.LOG_OUT_CONFIRM)) {
//                    ((HomeActivity) activity).logOut();
                dismiss();
            }
        }
    }

    private void PhoneAuthentication(String phoneNumber) {
        Log.d("check_ph_dia", ": " + phoneNumber);
        showLoader();
        hideResendOTP();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                AppConstants.PHONE_VERIFICATION_TIMEOUT,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showLoader();
        hideKeyboard();
        btn_negative.setVisibility(View.INVISIBLE);
        btn_positive.setVisibility(View.INVISIBLE);
        hideResendOTP();
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                stopCounter();
                hideLoader();
                if (task.isSuccessful()) {
                    hideResendOTP();
                    b_otp_check = false;
                    submitToGoToNext();
                    Log.d("task_check_result", ": " + task.getResult());
                } else {
                    b_otp_check = true;
                    stopCounter();
                    showResendOTP();
                    Log.d("task_check_excep", ": " + task.getException().getMessage());
                    btn_negative.setVisibility(View.VISIBLE);
                    btn_positive.setVisibility(View.VISIBLE);
                    edittext_phone.setError(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            edittext_phone.requestFocus();
                            showKeyboard(edittext_phone);
                            edittext_phone.setText(null);
                        }
                    }, 500);
                    Toast.makeText(activity, activity.getResources().getString(R.string.verification_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitToGoToNext() {
       activity.startActivity(new Intent(activity, SignUpStepTwoActivity.class));
        dismiss();
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        hideResendOTP();
        showLoader();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                AppConstants.PHONE_VERIFICATION_TIMEOUT,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void showLoader() {
        edittext_phone.setVisibility(View.INVISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        btn_positive.setVisibility(View.GONE);
    }

    private void hideLoader() {
        edittext_phone.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        btn_positive.setVisibility(View.VISIBLE);
    }

    private void showResendOTP() {
        resend_otp_tv.setVisibility(View.VISIBLE);
    }

    private void hideResendOTP() {
        resend_otp_tv.setVisibility(View.GONE);
        stopCounter();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private void showKeyboard(View view){
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)activity.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
