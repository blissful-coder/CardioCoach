package com.example.cardiocoach

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.*
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.hardware.Sensor.TYPE_PROXIMITY as TYPE_PROXIMITY1

var valPushUps = 0
var down = 0
var up = 0
var pushView: TextView?=null


class pushUps : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_ups)
        pushView = findViewById<TextView>(R.id.textView6)
        countPushUps()
    }

    private fun countPushUps() {

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) ?: return
        sensorManager.registerListener(object: SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracyLevel: Int) {
                Log.d("TAG", "Accuracy has changed to $accuracyLevel.")
            }

            override fun onSensorChanged(sensorEvent: SensorEvent?) {
                sensorEvent ?: return
                val shouldDataBeRejected = sensorEvent.accuracy <= SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM
                if (shouldDataBeRejected) {
                    return
                }

                val proximityDistance = sensorEvent.values.firstOrNull() ?: return
                Log.d("TAG", "Proximity distance: ${proximityDistance}cm")

                val isNear = proximityDistance <= 0.5f

                val backgroundColor = if(isNear) android.R.color.holo_blue_light else android.R.color.holo_green_light


                if(isNear){
                    down = 1
                }
                else{
                    up = 1
                }
                if(down + up == 2){
                    down = 0
                    up = 0
                    valPushUps++
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pushView!!.text = valPushUps.toString()
                    pushView!!.setBackgroundColor(getColor(backgroundColor))
                }


            }
        }, proximitySensor, 0)

    }

    override fun onStop() {
        super.onStop()
        valPushUps = 0
        up = 0
        down = 0
    }
}