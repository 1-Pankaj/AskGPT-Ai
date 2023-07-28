package com.pankaj.askgpt_ai

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.postDelayed
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val bottomsheet = findViewById<FrameLayout>(R.id.bottomsheet)
        val click = findViewById<CardView>(R.id.click)
        val messageTypeCard = findViewById<CardView>(R.id.messageTypeCard)
        val settingIcon = findViewById<ImageView>(R.id.settingIcon)
        val parent = findViewById<CoordinatorLayout>(R.id.parent)
        val textStart = findViewById<TextView>(R.id.textStart)
        val iconStart = findViewById<ImageView>(R.id.iconStart)
        val mainIllustration = findViewById<ImageView>(R.id.mainIllustration)
        val unleashText = findViewById<TextView>(R.id.unleashText)
        val powertext = findViewById<TextView>(R.id.powertext)
        val gptText = findViewById<TextView>(R.id.gptText)

        BottomSheetBehavior.from(bottomsheet).apply {
            peekHeight = 0
            this.state = BottomSheetBehavior.STATE_HIDDEN
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(applicationContext)

        val maAuth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance().reference

        bottomsheet.visibility = FrameLayout.VISIBLE

        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            messageTypeCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            settingIcon.setImageResource(R.drawable.setting)
            bottomsheet.setBackgroundResource(R.drawable.bottom_sheet)
            parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
            textStart.setTextColor(Color.parseColor("#000000"))
            iconStart.setImageResource(R.drawable.arrow_out)
            mainIllustration.setImageResource(R.drawable.flipped_illustration)
        }
        else{
            //night stuff
            messageTypeCard.setCardBackgroundColor(Color.parseColor("#6B6B6B"))
            settingIcon.setImageResource(R.drawable.setting_night)
            bottomsheet.setBackgroundResource(R.drawable.bottomsheet_night)
            parent.setBackgroundColor(Color.parseColor("#000000"))
            textStart.setTextColor(Color.parseColor("#FFFFFF"))
            iconStart.setImageResource(R.drawable.arrow_night)
            mainIllustration.setImageResource(R.drawable.flipped_night)
        }

        val handler = Handler(Looper.getMainLooper())

        val right_to_left = AnimationUtils.loadAnimation(this, R.anim.right_to_left)
        val text_animation = AnimationUtils.loadAnimation(this, R.anim.edittext1_anim)
        val text_animation2 = AnimationUtils.loadAnimation(this, R.anim.edittext2_anim)
        val text_animation3 = AnimationUtils.loadAnimation(this, R.anim.edittext3_anim)
        val text_animation4 = AnimationUtils.loadAnimation(this, R.anim.edittext4_anim)
        val remove_items = AnimationUtils.loadAnimation(this, R.anim.remove_items)
        val main_illustration = AnimationUtils.loadAnimation(this, R.anim.main_illustration)
        val fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        right_to_left.duration = 2000
        text_animation.duration = 2000
        text_animation2.duration = 2000
        text_animation3.duration = 2000
        text_animation4.duration = 2000
        remove_items.duration = 1500
        main_illustration.duration = 1500
        fade_out.duration = 200

        mainIllustration.startAnimation(right_to_left)
        unleashText.startAnimation(text_animation2)
        powertext.startAnimation(text_animation3)
        gptText.startAnimation(text_animation4)
        click.startAnimation(text_animation3)

        click.setOnClickListener{
            BottomSheetBehavior.from(bottomsheet).apply {
                peekHeight = 120
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }
            mainIllustration.startAnimation(main_illustration)
            unleashText.startAnimation(remove_items)
            powertext.startAnimation(remove_items)
            gptText.startAnimation(remove_items)
            click.startAnimation(remove_items)

            handler.postDelayed({
                unleashText.startAnimation(fade_out)
                powertext.startAnimation(fade_out)
                gptText.startAnimation(fade_out)
                click.startAnimation(fade_out)
            }, 1500)
        }


    }
}