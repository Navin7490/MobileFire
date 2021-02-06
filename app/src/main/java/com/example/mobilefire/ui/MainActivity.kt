package com.example.mobilefire.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilefire.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker

class MainActivity : AppCompatActivity() {
    var etName:EditText? = null
    var etEmail:EditText? = null
    var etPassword:EditText? = null

    var ccp: CountryCodePicker? = null
    var etphone: EditText? = null
    var btnsignup: Button? = null
    var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        ccp = findViewById(R.id.ccp)
        etphone = findViewById(R.id.Et_phone)
        btnsignup = findViewById(R.id.Btn_registre)
        ccp!!.registerCarrierNumberEditText(etphone)

        auth = FirebaseAuth.getInstance()

        btnsignup!!.setOnClickListener(View.OnClickListener {
            val name=etName!!.text.toString()
            val email = etEmail!!.text.toString()
            val fullPhone =  ccp!!.fullNumberWithPlus.replace("", "")
            val password = etPassword!!.toString()

            when {
                name.isEmpty() -> {
                    etName!!.error = "Enter Name"
                    etName!!.requestFocus()
                }
                email.isEmpty() -> {
                    etEmail!!.error = "Enter Email"
                    etEmail!!.requestFocus()
                }
                password.isEmpty() -> {
                    etPassword!!.error = "Enter Password"
                    etPassword!!.requestFocus()
                }
                password.length < 6 -> {
                    etPassword!!.error = "Password must be long"
                    etPassword!!.requestFocus()
                }
                else -> {

                    auth!!.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener { task->

                                if (task.isSuccessful){
                                    Log.e("mainActivity" ,"createUserWithEmailAndPassword")


                                    insertData(name,email,fullPhone)
                                    val intent = Intent(applicationContext, VerifyOtp_Activity::class.java)
                                    val uid=auth!!.uid
                                    intent.putExtra("uid" , uid)
                                    intent.putExtra("name" , name)
                                    intent.putExtra("email" , email)
                                    intent.putExtra("password" , password)
                                    intent.putExtra("mobile", fullPhone)
                                    startActivity(intent)
                                }

                            }
                            .addOnFailureListener {  e->
                                Log.e("mainActivity" ,"createUserWithEmailAndPassword=>${e.message}")

                                Toast.makeText(this,"${e.message}",Toast.LENGTH_LONG).show()

                            }

                }
            }


        })
    }

    fun insertData(name:String,email:String,phone:String){
        val users = hashMapOf(
                "Name" to name,
                "Email" to email,
                "Phone" to phone
        )
   val db = Firebase.firestore
        db.collection("Users")
                .add(users)
                .addOnSuccessListener {

                  Log.e("mainActivity" ,"insertData")

                }
                .addOnFailureListener { e->
                    Log.e("mainActivity" ,"${e.message}")


                }
    }
}