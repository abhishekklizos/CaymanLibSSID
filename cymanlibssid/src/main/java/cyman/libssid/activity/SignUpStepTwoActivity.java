package cyman.libssid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cyman.libssid.R;

public class SignUpStepTwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step_two);

        TextView textView=(TextView)findViewById(R.id.tv_login);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.putExtra("Key","You have been successfully authenticated");
                setResult(10,intent);
                finish();
            }
        });
    }
}
