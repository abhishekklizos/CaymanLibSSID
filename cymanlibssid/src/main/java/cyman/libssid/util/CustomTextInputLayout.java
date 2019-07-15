package cyman.libssid.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Field;


public class CustomTextInputLayout extends TextInputLayout {


    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setErrorEnabled(boolean enabled) {
        super.setErrorEnabled(enabled);

        if (!enabled) {
            return;
        }

        try {
            Field errorViewField = TextInputLayout.class.getDeclaredField("mErrorView");
            errorViewField.setAccessible(true);
            TextView errorView = (TextView) errorViewField.get(this);
            if (errorView != null) {
                errorView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.END;
                errorView.setLayoutParams(params);
            }
        }
        catch (Exception e) {
            // At least log what went wrong
            e.printStackTrace();
        }
    }
}