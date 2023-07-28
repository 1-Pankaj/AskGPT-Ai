package com.pankaj.askgpt_ai

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
        val click = findViewById<MaterialButton>(R.id.click)
        val messageTypeCard = findViewById<CardView>(R.id.messageTypeCard)
        val settingIcon = findViewById<ImageView>(R.id.settingIcon)
        val parent = findViewById<CoordinatorLayout>(R.id.parent)
        BottomSheetBehavior.from(bottomsheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        click.setOnClickListener{
            BottomSheetBehavior.from(bottomsheet).apply {
                peekHeight = 200
                this.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        Paper.init(applicationContext)

        val maAuth = FirebaseAuth.getInstance()
        val dbRef = FirebaseDatabase.getInstance().reference
        if(resources.getString(R.string.mode) == "Day"){
            //day stuff
            messageTypeCard.setCardBackgroundColor(Color.parseColor("#EFEDED"))
            settingIcon.setImageResource(R.drawable.setting)
            bottomsheet.setBackgroundResource(R.drawable.bottom_sheet)
            parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        else{
            //night stuff
            messageTypeCard.setCardBackgroundColor(Color.parseColor("#6B6B6B"))
            settingIcon.setImageResource(R.drawable.setting_night)
            bottomsheet.setBackgroundResource(R.drawable.bottomsheet_night)
            parent.setBackgroundColor(Color.parseColor("#000000"))
        }
    }
}