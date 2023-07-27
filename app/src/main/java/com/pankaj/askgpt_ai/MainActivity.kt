package com.pankaj.askgpt_ai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
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
        val cardBack = findViewById<CardView>(R.id.cardBack)
        val cardBackImage = findViewById<ImageView>(R.id.cardBackImage)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val buttonContinue = findViewById<MaterialButton>(R.id.buttonContinue)
        val cardContinue = findViewById<CardView>(R.id.cardContinue)

        cardContinue.visibility = CardView.GONE
        cardBack.visibility = CardView.GONE


        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            illustration.setImageResource(R.drawable.illustration)
            cardBackImage.setImageResource(R.drawable.back)
            emailEditText.setBackgroundResource(R.drawable.edittext)
        }
        else{
            //night stuff
            illustration.setImageResource(R.drawable.illustration_night)
            cardBackImage.setImageResource(R.drawable.back_night)
            emailEditText.setBackgroundResource(R.drawable.edittext_night)
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
            fade_in.duration = 1500
            cardBack.startAnimation(fade_in)
            cardBack.visibility = CardView.VISIBLE
            cardContinue.startAnimation(fade_in)
            cardContinue.visibility = CardView.VISIBLE
        }

        cardBack.setOnClickListener(){

        }
    }
}