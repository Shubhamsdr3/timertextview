package com.pandey.shubham.timertextview

import androidx.appcompat.widget.AppCompatTextView

import android.content.Context
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author pandey_shubham 9/10/2020
 */
class TimerTextView : AppCompatTextView , LifecycleObserver {

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

    private var isPaused = false

    private var isIncrementing = false

    private var totalDurationInSeconds: Int = 0

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet? = null) : super(context, attributeSet) {

    }

    constructor(context: Context, attributeSet: AttributeSet? = null, defyStyle: Int = 0) : super(context, attributeSet, defyStyle) {

    }

    fun registerLifeCycle(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
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
        this.totalDurationInSeconds = totalDurationInSeconds
        val minutes = totalDurationInSeconds / 60
        val seconds = totalDurationInSeconds % 60
        updateText(seconds, minutes)
    }

    fun setTypeFace(typeface: Typeface) {
        this.typeface = typeface
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (isIncrementing) {
            startIncrementTimer()
        } else {
            startDecrementTimer()
        }
    }

    fun startIncrementTimer() {
        isIncrementing = true
        if (!isRunning && !isPaused) {
            var timeInSeconds = totalDurationInSeconds % 60
            var  timeInMins = totalDurationInSeconds / 60
            taskHandler = Handler(Looper.myLooper()!!)
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
            if (increaseTimerTask != null) {
                isRunning = true
                taskHandler?.post(increaseTimerTask!!)
            }
        } else if (isPaused)  {
            isPaused = false
            if (increaseTimerTask != null) {
                taskHandler?.post(increaseTimerTask!!)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun pauseTimer() {
        if (increaseTimerTask != null && isRunning) {
            isPaused = true
            taskHandler?.removeCallbacks(increaseTimerTask!!)
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
        startIncrementTimer()
    }

    fun startDecrementTimer() {
        stopTimer()
        var minutes = 0
        var seconds: Int
        isIncrementing = false
        if (totalDurationInSeconds > 60) {
            minutes = totalDurationInSeconds / 60
            seconds = totalDurationInSeconds % 60
        } else {
            seconds = totalDurationInSeconds
        }
        taskHandler = Handler(Looper.myLooper()!!)
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
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopTimer() {
        isPaused = false
        isRunning = false
        if (increaseTimerTask != null) {
            taskHandler?.removeCallbacks(increaseTimerTask!!)
        }
        taskHandler = null
    }
}