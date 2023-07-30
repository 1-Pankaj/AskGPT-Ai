package com.pankaj.askgpt_ai

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior


class ReadingMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_mode)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val readingModeCard = findViewById<FrameLayout>(R.id.readingModeCard)
        val readingModeText = findViewById<TextView>(R.id.readingModeText)
        val readingModeCloseCard = findViewById<CardView>(R.id.closeReadingModeCard)
        val readingModeCloseIcon = findViewById<ImageView>(R.id.closeReadingModeIcon)

        readingModeText.text = intent.extras?.getString("text")

        BottomSheetBehavior.from(readingModeCard).apply {
            peekHeight = (dpHeight / 2).toInt()
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        readingModeCloseCard.setOnClickListener{
            finish()
        }

        if(resources.getString(R.string.mode) == "Day"){
            readingModeCard.setBackgroundResource(R.drawable.readingmode_bottomsheet)
            readingModeCloseIcon.setImageResource(R.drawable.close)
        }
        else{
            readingModeCard.setBackgroundResource(R.drawable.readingmode_night)
            readingModeCloseIcon.setImageResource(R.drawable.close_night)
        }
    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Exiting reading mode", Toast.LENGTH_SHORT).show()
        finish()

        super.onBackPressed()
    }
}