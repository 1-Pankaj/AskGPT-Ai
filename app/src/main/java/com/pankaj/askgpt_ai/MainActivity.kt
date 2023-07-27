package com.pankaj.askgpt_ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val buttonStart = findViewById<MaterialButton>(R.id.buttonStart)
        val illustration = findViewById<ImageView>(R.id.illustration)
        val textDesc = findViewById<TextView>(R.id.textDesc)
        val textPolicy = findViewById<TextView>(R.id.textPolicy)

        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            illustration.setImageResource(R.drawable.illustration)
        }
        else{
            //night stuff
            illustration.setImageResource(R.drawable.illustration_night)
        }

        val illustration_anim = AnimationUtils.loadAnimation(this, R.anim.illustration_anim)
        val fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fade_out.duration = 1200
        fade_in.duration = 1200
        illustration_anim.duration = 1200

        buttonStart.setOnClickListener(){
            illustration.startAnimation(illustration_anim)
            textDesc.startAnimation(fade_out)
            textPolicy.startAnimation(fade_out)
            buttonStart.startAnimation(fade_out)
        }
    }
}