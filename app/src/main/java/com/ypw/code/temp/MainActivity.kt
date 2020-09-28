package com.ypw.code.temp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bubbleView.setCount(100)
    }

    var count = 100
    fun onClick(v: View) {
        count -= 10
        bubbleView.setCount(count)
    }
}