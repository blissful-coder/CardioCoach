package com.example.cardiocoach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

const val COMMAND_KEY = "commandTime"
const val RUN_KEY = "runTime"
class StartScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_screen)
        findViewById<EditText>(R.id.editrun).setText("3")
        findViewById<EditText>(R.id.editcommand).setText("2")
    }

    fun startCoach(view: View){
        var runLong = 3*60*1000L
        var commandLong = 1000L

        var runTimeVal = findViewById<EditText>(R.id.editrun).text.toString()
        if(runTimeVal!="") {
            runLong = runTimeVal.toLong() * 60 * 1000
        }
        var commandTimeVal = findViewById<EditText>(R.id.editcommand).text.toString()
        if(commandTimeVal!=""){
            commandLong = commandTimeVal.toLong() * 1000
        }


        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(RUN_KEY, runLong)
            putExtra(COMMAND_KEY, commandLong)
        }

//        val text = "Command: ${commandLong} seconds, Run: ${runLong} minutes"
//        val duration = Toast.LENGTH_SHORT
//        val toast = Toast.makeText(applicationContext, text, duration)
//        toast.show()
        startActivity(intent)
    }


    fun startPushExercise(view: View){
        val text = "Start Push Ups"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(this, text, duration)
        toast.show()
        val intent = Intent(this, pushUps::class.java)
        startActivity(intent)
    }
}