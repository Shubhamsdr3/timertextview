package com.pandey.shubham.timertextviewexample

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
        taskHandler = Handler()
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
            totalDurationInSeconds = 0
            var timeInSeconds = 0
            var  timeInMins = 0
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

    fun startDecrementTimer() {
        stopTimer()
        var minutes = 0
        var seconds: Int
        if (totalDurationInSeconds > 60) {
            minutes = totalDurationInSeconds / 60
            seconds = totalDurationInSeconds % 60
        } else {
            seconds = totalDurationInSeconds
        }
        taskHandler = Handler()
        if (!isRunning) {
            decreaseTimerTask = object : Runnable {
                override fun run() {
                    taskHandler?.postDelayed(this, 1000)
                    if (seconds > 0) {
                        seconds--
                    } else if (minutes > 0) {
                        minutes--
                        seconds = 60
                    } else {
                        stopTimer()
                    }
                    updateText(seconds, minutes)
                }
            }
            if (decreaseTimerTask != null) {
                isRunning = true
                taskHandler?.post(decreaseTimerTask!!)
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
    }
}