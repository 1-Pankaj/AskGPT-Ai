package com.pankaj.askgpt_ai

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import yuku.ambilwarna.AmbilWarnaDialog


class ReadingMode : AppCompatActivity() {
    private var mDefaultColor = 0
    lateinit var readingModeText: TextView
    lateinit var mAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_mode)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        var data: String = ""
        var mode = "text"
        var text:Float = 13F

        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density

        val readingModeCard = findViewById<FrameLayout>(R.id.readingModeCard)
        readingModeText = findViewById<TextView>(R.id.readingModeText)
        val readingModeCloseCard = findViewById<CardView>(R.id.closeReadingModeCard)
        val readingModeCloseIcon = findViewById<ImageView>(R.id.closeReadingModeIcon)
        val colorPicker = findViewById<CardView>(R.id.colorPicker)
        val shareContent = findViewById<CardView>(R.id.shareContent)
        val copyContent = findViewById<CardView>(R.id.copyContent)
        val readingModeImage = findViewById<ImageView>(R.id.readingModeImage)
        val textSize = findViewById<CardView>(R.id.textSize)
        val textDecrease = findViewById<CardView>(R.id.textDecrease)
        textDecrease.visibility = View.GONE

        readingModeText.textSize = text

        mode = intent.extras?.getString("mode").toString()
        readingModeImage.visibility = View.GONE
        data = intent.extras?.getString("text").toString()
        readingModeText.text = data

        if(mode == "image"){
            readingModeImage.visibility = View.VISIBLE
            readingModeText.visibility = View.GONE
            Picasso.get().load(data).into(readingModeImage)
            colorPicker.visibility = View.GONE
        }

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

        colorPicker.setOnClickListener{
            openColorPickerDialogue()
        }
        shareContent.setOnClickListener{
            shareText(readingModeText.text.toString())
        }
        copyContent.setOnClickListener{
            copyToClipboard(data)
            Toast.makeText(applicationContext, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        if(mode == "image"){
            textSize.visibility = View.GONE
        }
        textSize.setOnClickListener{
            if(text == 24F){
                textSize.visibility = View.GONE
            }
            else{
                text = text + 1
                readingModeText.textSize = text
                textDecrease.visibility = View.VISIBLE
            }
        }

        textDecrease.setOnClickListener{

            if(text == 10F){
                textDecrease.visibility = View.GONE
            }
            else{
                text = text - 1
                readingModeText.textSize = text
                textSize.visibility = View.VISIBLE
            }

        }
    }

    override fun onBackPressed() {
        Toast.makeText(applicationContext, "Exiting reading mode", Toast.LENGTH_SHORT).show()
        finish()

        super.onBackPressed()
    }


    fun openColorPickerDialogue() {


        val colorPickerDialogue = AmbilWarnaDialog(this, mDefaultColor,
            object : AmbilWarnaDialog.OnAmbilWarnaListener {
                override fun onCancel(dialog: AmbilWarnaDialog?) {

                }

                override fun onOk(dialog: AmbilWarnaDialog?, color: Int) {
                    mDefaultColor = color
                    readingModeText.setTextColor(mDefaultColor)
                }

            })
        colorPickerDialogue.show()
    }
    private fun shareText(titlee: String) {


        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        intent.putExtra(Intent.EXTRA_TEXT, titlee)
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    fun copyToClipboard(text: CharSequence){
        val clipboard = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
}