package com.example.emptyactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class subActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
    }
}