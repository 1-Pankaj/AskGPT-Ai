package com.pankaj.askgpt_ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(resources.getString(R.string.mode) == "Day"){
            //day stuff

        }
        else{
            //night stuff

        }
    }
}