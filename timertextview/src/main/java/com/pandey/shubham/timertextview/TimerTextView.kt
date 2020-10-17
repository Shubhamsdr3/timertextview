package com.pandey.shubham.timertextview

import androidx.appcompat.widget.AppCompatTextView

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet

/**
 * @author pandey_shubham 9/10/2020
 */
class TimerTextView : AppCompatTextView {

    /**
     * Note: Beware of this.taskHandler and View.getHandler()
     */
    private var taskHandler : Handler? = null

    /**
     * This task handles the increasing counter
     */
    private var increaseTimerTask: Runnable? = null

    /**
     * This task handles the decreasing count
     */
    private var decreaseTimerTask: Runnable? = null

    private var isRunning = false

    private var totalDurationInSeconds: Int = 0

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context, attributeSet) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet? = null, defyStyle: Int = 0) : super(context, attributeSet, defyStyle) {
        initialize()
    }

    private fun initialize() {
        var timeInSeconds = 0
        var  timeInMins = 0
        taskHandler = Handler()

        increaseTimerTask = object : Runnable {
            override fun run() {
                taskHandler?.postDelayed(this, 1000)
                updateText(timeInSeconds, timeInMins)
                timeInSeconds++
                if (timeInSeconds > 60) {
                    timeInMins += timeInSeconds / 60
                    timeInSeconds = 0
                }
                totalDurationInSeconds++
            }
        }
    }

    private fun updateText(timeInSeconds: Int, timeInMins: Int) {
        text = when {
            timeInSeconds < 10 -> {
                "0$timeInMins:0$timeInSeconds"
            }
            timeInMins < 10 -> {
                "0$timeInMins:$timeInSeconds"
            }
            else -> {
                "$timeInMins:$timeInSeconds"
            }
        }
    }

    fun setInitialTime(totalDurationInSeconds: Int) {
        val minutes = totalDurationInSeconds / 60
        val seconds = totalDurationInSeconds % 60
        updateText(seconds, minutes)
    }

    fun setTypeFace(typeface: Typeface) {
        this.typeface = typeface
    }

    fun startIncrementTimer() {
        if (!isRunning) {
            isRunning = true
            if (increaseTimerTask != null) {
                taskHandler?.post(increaseTimerTask!!)
            }
        }
    }

    /**
     * Returns total duration in seconds
     */
    fun getTotalDuration(): Int {
        return totalDurationInSeconds
    }

    fun resetTimer() {
        text = "00:00"
        if (increaseTimerTask != null) {
            taskHandler?.removeCallbacks(increaseTimerTask!!)
        }
        totalDurationInSeconds = 0
        increaseTimerTask = null
        isRunning = false
        initialize()
        startIncrementTimer()
    }

    //TODO:SHUBHAM
    fun startDecrementTimer(startTimeInSec: Int) {
        var minutes = 0
        var seconds: Int
        if (startTimeInSec > 60) {
            minutes = startTimeInSec / 60
            seconds = startTimeInSec % 60
        } else {
            seconds = startTimeInSec
        }
        decreaseTimerTask = object : Runnable {
            override fun run() {
                seconds--
                updateText(seconds, minutes)
            }
        }
    }

    /**
     * Don't forget to remove callback onStop()/onDestroy()
     */
    fun stopTimer() {
        isRunning = false
        if (increaseTimerTask != null) {
            taskHandler?.removeCallbacks(increaseTimerTask!!)
        }
        taskHandler = null
        totalDurationInSeconds = 0
    }
}