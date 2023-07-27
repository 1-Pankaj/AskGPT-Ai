package com.pankaj.askgpt_ai

import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        val googleCard = findViewById<CardView>(R.id.googleCard)

        cardContinue.visibility = CardView.GONE
        cardBack.visibility = CardView.GONE


        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            illustration.setImageResource(R.drawable.illustration)
            cardBackImage.setImageResource(R.drawable.back)
            emailEditText.setBackgroundResource(R.drawable.edittext)
            googleCard.setCardBackgroundColor(Color.parseColor("#EDEDED"))
        }
        else{
            //night stuff
            illustration.setImageResource(R.drawable.illustration_night)
            cardBackImage.setImageResource(R.drawable.back_night)
            emailEditText.setBackgroundResource(R.drawable.edittext_night)
            googleCard.setCardBackgroundColor(Color.parseColor("#606060"))
        }

        val illustration_anim = AnimationUtils.loadAnimation(this, R.anim.illustration_anim)
        val fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val textpolicy_anim = AnimationUtils.loadAnimation(this, R.anim.textpolicy_anim)
        textpolicy_anim.duration = 600
        fade_out.duration = 600
        fade_in.duration = 600
        illustration_anim.duration = 600

        buttonStart.setOnClickListener(){
            illustration_anim.setInterpolator(null)
            illustration.startAnimation(illustration_anim)
            textDesc.startAnimation(fade_out)
            textpolicy_anim.setInterpolator(null)
            textPolicy.startAnimation(textpolicy_anim)
            buttonStart.startAnimation(fade_out)
            cardBack.startAnimation(fade_in)
            cardBack.visibility = CardView.VISIBLE
            cardContinue.startAnimation(fade_in)
            cardContinue.visibility = CardView.VISIBLE
        }

        cardBack.setOnClickListener(){
            illustration_anim.setInterpolator(ReverseInterpolator())
            illustration.startAnimation(illustration_anim)
            textDesc.startAnimation(fade_in)
            textpolicy_anim.setInterpolator(ReverseInterpolator())
            textPolicy.startAnimation(textpolicy_anim)
            buttonStart.startAnimation(fade_in)
            cardBack.startAnimation(fade_out)
            cardBack.visibility = CardView.GONE
            cardContinue.startAnimation(fade_out)
            cardContinue.visibility = CardView.GONE
        }
    }
    class ReverseInterpolator : Interpolator {
        override fun getInterpolation(paramFloat: Float): Float {
            return Math.abs(paramFloat - 1f)
        }
    }
}