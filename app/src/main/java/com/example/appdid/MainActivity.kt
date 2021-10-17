package com.example.appdid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn2=findViewById<Button>(R.id.btn_2)

        btn2.setOnClickListener {
            Toast.makeText(applicationContext,"Hello",Toast.LENGTH_SHORT).show()
        }
    }
}