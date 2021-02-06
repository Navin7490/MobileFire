package com.example.mobilefire.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.mobilefire.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit

class VerifyOtp_Activity : AppCompatActivity() {
    var etotp: EditText? = null
    var pinView: PinView? = null
    var btnverify: Button? = null
    var phonenumber: String? = null
    var otpid: String? = null
    var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp_)
        phonenumber = intent.getStringExtra("mobile").toString()
        etotp = findViewById(R.id.Et_Otp)
        pinView = findViewById(R.id.pinViewOtp)
        btnverify = findViewById(R.id.Btn_Verify)
        mAuth = FirebaseAuth.getInstance()
        initiateotp()
        btnverify!!.setOnClickListener(View.OnClickListener { // String otp=etotp.getText().toString();
            val otp = pinView!!.getText().toString()
            if (otp.isEmpty()) {
                Toast.makeText(this@VerifyOtp_Activity, "enter otp", Toast.LENGTH_SHORT).show()
            } else if (otp.length != 6) {
                Toast.makeText(this@VerifyOtp_Activity, "invalid otp", Toast.LENGTH_SHORT).show()
            } else {
                val phoneAuthCredential = PhoneAuthProvider.getCredential(otpid!!, otp)
                signInWithPhoneAuthCredential(phoneAuthCredential)
            }
        })
    }

    private fun initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonenumber!!,  // Phone number to verify
                60,  // Timeout duration
                TimeUnit.SECONDS,  // Unit of timeout
                this,  // Activity (for callback binding)
                object : OnVerificationStateChangedCallbacks() {
                    override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                        otpid = s
                        Toast.makeText(this@VerifyOtp_Activity, "OTP Send  On $phonenumber Mobile number", Toast.LENGTH_LONG).show()
                    }

                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        val v = phoneAuthCredential.smsCode
                        pinView!!.setText(v)
                        signInWithPhoneAuthCredential(phoneAuthCredential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(this@VerifyOtp_Activity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) // OnVerificationStateChangedCallbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@VerifyOtp_Activity, ProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                        Toast.makeText(this@VerifyOtp_Activity, "Login success", Toast.LENGTH_SHORT).show()
                        // finish();
                        // ...
                    } else {
                        Toast.makeText(this@VerifyOtp_Activity, "wrong otp", Toast.LENGTH_SHORT).show()
                    }
                }
    }
}