package com.example.cardiocoach

import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.Path
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var defaultTime: Long?= 180000
    private var mySoundtl: MediaPlayer? = null
    private var mySoundbr: MediaPlayer? = null
    private var mySoundbl: MediaPlayer? = null
    private var mySoundtr: MediaPlayer? = null
    private var timerObject: CountDownTimer? = null

    var countMainRun: Int = 0
    var play = true
    var pause = false
    var alreadyPlaying = false
    var offset = 80f
    var centerX: Float = Resources.getSystem().getDisplayMetrics().widthPixels/2f - offset
    var centerY: Float = Resources.getSystem().getDisplayMetrics().heightPixels/2f - offset
    var tlX = 1.5f*offset;
    var tlY = 3f*offset;
    var trX = Resources.getSystem().getDisplayMetrics().widthPixels - offset*3f;
    var trY = 3f*offset;
    var blX = offset;
    var blY = Resources.getSystem().getDisplayMetrics().heightPixels - 5.5f*offset;
    var brX = Resources.getSystem().getDisplayMetrics().widthPixels - offset*3f;
    var brY = Resources.getSystem().getDisplayMetrics().heightPixels - 5.5f*offset;
    var coachSays = 0
    var mainHandler = Handler(Looper.getMainLooper())
    var commandDelay: Long = 2000
    var jobDelay: Long = 180000

    private val myRunnable = object : Runnable {
        override fun run() {
            coachSays = (1..4).random()
            var text = ""
            if (coachSays % 4 == 0) {
                text = "Top Left"
                runTopLeftAndBack()
            }
            else if (coachSays % 4 == 1) {
                text = "Top Right"
                runTopRightAndBack()
            }
            else if (coachSays % 4 == 2) {
                text = "Bottom Left"
                runBottomLeftAndBack()
            }
            else {
                text = "Bottom Right"
                runBottomRightAndBack()
            }
//            val duration = Toast.LENGTH_SHORT
//            val toast = Toast.makeText(applicationContext, text, duration)
//            toast.show()
            mainHandler.postDelayed(this, commandDelay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jobDelay = intent.getLongExtra(RUN_KEY,180000)
        commandDelay = intent.getLongExtra(COMMAND_KEY,2000)
        defaultTime = jobDelay
        findViewById<TextView>(R.id.cTimer).setText("Exercise time: "+ (jobDelay/60000) + " minutes")
    }

//    override fun onStart() {
//        super.onStart()
//        commandDelay = intent.getLongExtra(COMMAND_KEY,3000)
//        commandDelay = intent.getLongExtra(RUN_KEY,2000)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        commandDelay = intent.getLongExtra(COMMAND_KEY,3000)
//        commandDelay = intent.getLongExtra(RUN_KEY,2000)
//    }

    fun runExercise(view: View) {
        // Do something in response to button click
        if (jobDelay == 0L) {
            jobDelay = defaultTime!!;
        } else {
            findViewById<TextView>(R.id.cTimer).setText("Exercise time: " + (jobDelay / 60000) + " minutes")
        }
        play = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            countMainRun++
            if(play && alreadyPlaying==false){
                alreadyPlaying = true
                timerObject = object : CountDownTimer(jobDelay, 1000) {
                    val TimerView = findViewById<TextView>(R.id.cTimer)
                    override fun onTick(millisUntilFinished: Long) {
                        TimerView.setText( "" + (millisUntilFinished/60000) + " Minutes " + (millisUntilFinished / 1000)%60  + " Seconds remaining")
                        jobDelay = millisUntilFinished
                    }
                    override fun onFinish() {
                        TimerView.setText("Done!")
                    }
                }.start()
                mainHandler.post(myRunnable)
            }
        } else {
            // Create animator without using curved path
            val text = "Not supported in versions lower that Android LOLLIPOP!!!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
    }

    private fun runBottomRightAndBack() {
        mySoundbr = MediaPlayer.create(this, R.raw.br)
        mySoundbr?.start()
        val view1 = findViewById<View>(R.id.imageView1)
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(brX,brY)
            moveTo(brX,brY)
            lineTo(centerX, centerY)
        }
        val animator = ObjectAnimator.ofFloat(view1, View.X, View.Y, path).apply {
            duration = commandDelay
            start()
        }
    }

    private fun runBottomLeftAndBack() {
        mySoundbl = MediaPlayer.create(this, R.raw.bl)
        mySoundbl?.start()
        val view1 = findViewById<View>(R.id.imageView1)
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(blX,blY)
            moveTo(blX,blY)
            lineTo(centerX, centerY)
        }
        val animator = ObjectAnimator.ofFloat(view1, View.X, View.Y, path).apply {
            duration = commandDelay
            start()
        }
    }

    private fun runTopLeftAndBack() {
        mySoundtl = MediaPlayer.create(this, R.raw.tl)
        mySoundtl?.start()
        val view1 = findViewById<View>(R.id.imageView1)
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(tlX,tlY)
            moveTo(tlX,tlY)
            lineTo(centerX, centerY)
        }
        val animator = ObjectAnimator.ofFloat(view1, View.X, View.Y, path).apply {
            duration = commandDelay
            start()
        }
    }

    private fun runTopRightAndBack() {
        mySoundtr = MediaPlayer.create(this, R.raw.tr)
        mySoundtr?.start()
        val view1 = findViewById<View>(R.id.imageView1)
        val path = Path().apply {
            moveTo(centerX, centerY)
            lineTo(trX,trY)
            moveTo(trX,trY)
            lineTo(centerX, centerY)
        }
        val animator = ObjectAnimator.ofFloat(view1, View.X, View.Y, path).apply {
            duration = commandDelay
            start()
        }
    }

    fun pauseExercise(view: View) {
        play = false
        alreadyPlaying = false
        timerObject?.cancel()
        mainHandler.removeCallbacks(myRunnable)
        val text = "Pausing the Exercise."
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    fun stopExercise(view: View) {
        play = false
        alreadyPlaying = false
        jobDelay = 0L
        findViewById<TextView>(R.id.cTimer).setText("Done")
        timerObject?.cancel()
        timerObject = null
        mainHandler?.removeCallbacks(myRunnable)
        val text = "Stopping the Exercise."
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()
    }

    override fun onStop() {
        super.onStop()
        play = false
        timerObject?.cancel()
        timerObject = null
        mainHandler?.removeCallbacks(myRunnable)
        mySoundbl?.stop()
        mySoundbl?.release()
        mySoundbr?.stop()
        mySoundbr?.release()
        mySoundtl?.stop()
        mySoundtl?.release()
        mySoundtr?.stop()
        mySoundtr?.release()
    }
}