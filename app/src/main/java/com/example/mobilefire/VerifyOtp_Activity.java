package com.example.mobilefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOtp_Activity extends AppCompatActivity {
    EditText etotp;
    Button btnverify;
    String phonenumber;
    String otpid;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp_);
        phonenumber=getIntent().getStringExtra("mobile").toString();
        etotp=findViewById(R.id.Et_Otp);
        btnverify=findViewById(R.id.Btn_Verify);
        mAuth=FirebaseAuth.getInstance();
        initiateotp();
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=etotp.getText().toString();
                if (otp.isEmpty()){
                    Toast.makeText(VerifyOtp_Activity.this, "enter otp", Toast.LENGTH_SHORT).show();
                }
                else if (otp.length()!=6){
                    Toast.makeText(VerifyOtp_Activity.this, "invalide otp", Toast.LENGTH_SHORT).show();

                }
                else {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpid, otp);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }
        });


    }

    private void initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                        otpid=s;
                        Toast.makeText(VerifyOtp_Activity.this, "OTP Send  On "+phonenumber+" Mobile number", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                    {
                       // signInWithPhoneAuthCredential(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(VerifyOtp_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });      // OnVerificationStateChangedCallbacks

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyOtp_Activity.this, "Login success", Toast.LENGTH_SHORT).show();
                           // finish();
                            // ...
                        } else {
                            Toast.makeText(VerifyOtp_Activity.this, "wrong otp", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}