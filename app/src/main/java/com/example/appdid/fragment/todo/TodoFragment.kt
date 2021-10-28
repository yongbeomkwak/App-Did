package com.example.appdid.fragment.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appdid.databinding.FragmentTodoBinding

class TodoFragment: Fragment() {    // Todo리스트 Fragment
    private var _binding: FragmentTodoBinding? = null

    private lateinit var button2: Button

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        button2 = binding.button2
        button2.setOnClickListener(button2ClickListener)
        return root
    }

    val button2ClickListener : View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            Toast.makeText(context, "Todo Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}