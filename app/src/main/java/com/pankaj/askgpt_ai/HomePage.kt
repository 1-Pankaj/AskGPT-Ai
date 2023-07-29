package com.pankaj.askgpt_ai

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class HomePage : AppCompatActivity() {

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType();
    var client = OkHttpClient()
    lateinit var messageList : List<Message>
    lateinit var messageAdapter : MessageAdapter
    lateinit var messageRecycleView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)



        try {






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
            val settings_bottomsheet = findViewById<FrameLayout>(R.id.settings_bottomsheet)
            val settingCard = findViewById<CardView>(R.id.settingCard)
            val closeSettings = findViewById<CardView>(R.id.closeSetiings)
            messageRecycleView = findViewById<RecyclerView>(R.id.messageRecycleView)
            val messageBox = findViewById<EditText>(R.id.messageBox)
            val sendButton = findViewById<CardView>(R.id.sendButton)

            messageList = ArrayList<Message>()
            messageAdapter = MessageAdapter(messageList)
            messageRecycleView.adapter = messageAdapter
            val linearLayoutManager = LinearLayoutManager(this)
            linearLayoutManager.stackFromEnd = true
            messageRecycleView.layoutManager = linearLayoutManager


            settings_bottomsheet.visibility = View.VISIBLE

            BottomSheetBehavior.from(bottomsheet).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
            BottomSheetBehavior.from(settings_bottomsheet).apply {
                peekHeight = 0
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            Paper.init(applicationContext)

            val maAuth = FirebaseAuth.getInstance()
            val dbRef = FirebaseDatabase.getInstance().reference

            bottomsheet.visibility = FrameLayout.VISIBLE

            if (resources.getString(R.string.mode) == "Day") {
                //day stuff
                messageTypeCard.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                settingIcon.setImageResource(R.drawable.setting)
                bottomsheet.setBackgroundResource(R.drawable.bottom_sheet)
                parent.setBackgroundColor(Color.parseColor("#FFFFFF"))
                textStart.setTextColor(Color.parseColor("#000000"))
                iconStart.setImageResource(R.drawable.arrow_out)
                mainIllustration.setImageResource(R.drawable.flipped_illustration)
                settings_bottomsheet.setBackgroundResource(R.drawable.settings_bottomsheet)
            } else {
                //night stuff
                messageTypeCard.setCardBackgroundColor(Color.parseColor("#6B6B6B"))
                settingIcon.setImageResource(R.drawable.setting_night)
                bottomsheet.setBackgroundResource(R.drawable.bottomsheet_night)
                parent.setBackgroundColor(Color.parseColor("#000000"))
                textStart.setTextColor(Color.parseColor("#FFFFFF"))
                iconStart.setImageResource(R.drawable.arrow_night)
                mainIllustration.setImageResource(R.drawable.flipped_night)
                settings_bottomsheet.setBackgroundResource(R.drawable.settingsbottomsheet_night)
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
            val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)

            right_to_left.duration = 2000
            text_animation.duration = 2000
            text_animation2.duration = 2000
            text_animation3.duration = 2000
            text_animation4.duration = 2000
            remove_items.duration = 1500
            main_illustration.duration = 1500
            fade_out.duration = 500

            mainIllustration.startAnimation(right_to_left)
            unleashText.startAnimation(text_animation2)
            powertext.startAnimation(text_animation3)
            gptText.startAnimation(text_animation4)
            click.startAnimation(text_animation3)

            click.setOnClickListener {
                BottomSheetBehavior.from(bottomsheet).apply {
                    peekHeight = 170
                    this.state = BottomSheetBehavior.STATE_HALF_EXPANDED
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

            settingCard.setOnClickListener {
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 300
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
                BottomSheetBehavior.from(bottomsheet).apply {
                    peekHeight = 150
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
            closeSettings.setOnClickListener {
                BottomSheetBehavior.from(settings_bottomsheet).apply {
                    peekHeight = 0
                    this.state = BottomSheetBehavior.STATE_COLLAPSED
                    this.state = BottomSheetBehavior.STATE_HIDDEN
                }
                BottomSheetBehavior.from(bottomsheet).apply {
                    peekHeight = 150
                    this.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }


            sendButton.setOnClickListener {
                if (messageBox.text.toString().trim().isEmpty()) {

                } else {
                    val messageText = messageBox.text.toString().trim()
                    messageBox.setText("")
                    try {
                        addToChat(messageText, "me")
                        callAPI(messageText);
                    }
                    catch (e : Exception){
                        Log.d("dax", e.message.toString())
                    }
                }

            }
        }
        catch (e : Exception){
            Log.d("dax", e.message.toString())
        }
    }
    fun addToChat(message: String, sentBy : String){
        runOnUiThread(Runnable {
            kotlin.run {
                (messageList as ArrayList<Message>).add(Message(message, sentBy))
                messageAdapter.notifyDataSetChanged()
                messageRecycleView.smoothScrollToPosition(messageAdapter.itemCount)
            }
        })
    }

    fun addResponse(response: String?) {

        if (response != null) {
            addToChat(response.toString().trim(), "bot")
        }
    }
    fun callAPI(question : String){
        //okhttp
        val jsonBody = JSONObject()
        try {

            jsonBody.put("model", "text-davinci-003")
            jsonBody.put("prompt", question)
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        }
        catch (e : Exception){
            Log.d("dax", e.message.toString())
        }


        val body = RequestBody.create(JSON, jsonBody.toString())
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to "+e.message.toString());
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    Log.d("dax", response.body!!.string())

//
                    try {
                            var jsonObject = JSONObject(response.body.toString())
                            var jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getString("text")
                            Log.d("dax", result.toString())

                    } catch (e: JSONException) {
                        Log.d("dax", e.message.toString())
                    }
                }
                else{
                    addResponse("Failed to load response due to "+ response.body!!.string())
                }
            }

        })
    }
}