package com.pandey.shubham.timertextviewexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        timer_text.registerLifeCycle(lifecycle)

        timer_text.setInitialTime(10)

        increment.setOnClickListener {
            timer_text.startIncrementTimer()
        }

        decrement.setOnClickListener {
            timer_text.startDecrementTimer()
        }

        stop.setOnClickListener {
            timer_text.stopTimer()
        }
    }
}