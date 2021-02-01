package com.example.mobilefire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    EditText etphone;
    Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ccp=findViewById(R.id.ccp);
        etphone=findViewById(R.id.Et_phone);
        btnsignup=findViewById(R.id.Btn_registre);
        ccp.registerCarrierNumberEditText(etphone);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),VerifyOtp_Activity.class);
                intent.putExtra("mobile",ccp.getFullNumberWithPlus().replace("",""));
                startActivity(intent);
            }
        });
    }
}