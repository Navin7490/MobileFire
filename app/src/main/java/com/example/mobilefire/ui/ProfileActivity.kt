package com.example.mobilefire.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mobilefire.R

class ProfileActivity : AppCompatActivity() {
    var btnLogout: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btnLogout = findViewById(R.id.btnLogout)
        btnLogout!!.setOnClickListener {

        }
    }
}