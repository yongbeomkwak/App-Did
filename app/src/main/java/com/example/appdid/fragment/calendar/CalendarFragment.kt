package com.example.appdid.fragment.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appdid.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {   // 달력 Fragment
    private var _binding: FragmentCalendarBinding? = null

    private lateinit var button1: Button

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        button1 = binding.button1
        button1.setOnClickListener(button1ClickListener)
        return root
    }

    val button1ClickListener : View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            Toast.makeText(context, "Calendar Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}