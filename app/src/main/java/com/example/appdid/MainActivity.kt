package com.example.appdid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn1=findViewById<Button>(R.id.btn_1)

        btn1.setOnClickListener {
            Toast.makeText(applicationContext,"Hi",Toast.LENGTH_SHORT).show()
        }
    }
}