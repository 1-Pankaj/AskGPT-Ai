package com.pankaj.askgpt_ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ReadingMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_mode)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        val readingModeCard = findViewById<FrameLayout>(R.id.readingModeCard)
        val readingModeText = findViewById<TextView>(R.id.readingModeText)
        val readingModeCloseCard = findViewById<CardView>(R.id.closeReadingModeCard)
        val readingModeCloseIcon = findViewById<ImageView>(R.id.closeReadingModeIcon)

        readingModeText.text = intent.extras?.getString("text")
        BottomSheetBehavior.from(readingModeCard).apply {
            peekHeight = 500
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        readingModeCloseCard.setOnClickListener{
            BottomSheetBehavior.from(readingModeCard).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_COLLAPSED
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
            finish()
        }

        if(resources.getString(R.string.mode) == "Day"){
            readingModeCard.setBackgroundResource(R.drawable.settings_bottomsheet)
            readingModeCloseIcon.setImageResource(R.drawable.close)
        }
        else{
            readingModeCard.setBackgroundResource(R.drawable.readingmode_night)
            readingModeCloseIcon.setImageResource(R.drawable.close_night)
        }
    }
}