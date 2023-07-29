package com.pankaj.askgpt_ai

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.postDelayed
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import java.lang.reflect.Type


class HomePage : AppCompatActivity() {

    var messageList: List<Message>? = null
    var messageAdapter: MessageAdapter? = null
    lateinit var messageRecycleView: RecyclerView

    val JSON: MediaType = "application/json; charset=utf-8".toMediaType();
    var client = OkHttpClient()


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
            val closeSettings = findViewById<CardView>(R.id.closeSettings)
            val closeSettingsIcon = findViewById<ImageView>(R.id.closeSettingsIcon)
            messageRecycleView = findViewById<RecyclerView>(R.id.messageRecycleView)
            val messageBox = findViewById<EditText>(R.id.messageBox)
            val sendButton = findViewById<CardView>(R.id.sendButton)



            messageList = ArrayList<Message>()
            messageAdapter = MessageAdapter(messageList as ArrayList<Message>, applicationContext)
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
                closeSettingsIcon.setImageResource(R.drawable.close)

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
                closeSettingsIcon.setImageResource(R.drawable.close_night)

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
                click.isEnabled = false
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


            addToChat("Hi there! welcome to AskGPT-Ai, a fast and accurate " +
                    "response based application, " + "start by asking a question. " +
                    "Each time you refresh or reload the application, " +
                    "a new chat will be created and previous chat data will be wiped for " +
                    "better end-to-end encryption, AskGPT now!\n\nLong press this message for more options", "bot")

            sendButton.setOnClickListener {
                if (messageBox.text.toString().trim().isEmpty()) {

                } else {
                    val messageText = messageBox.text.toString().trim()
                    messageBox.setText("")
                    addToChat(messageText, "me")
                    try {
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


    fun addToChat(message: String?, sentBy: String?) {
        runOnUiThread(Runnable {
            kotlin.run {
                (messageList as ArrayList<Message>).add(Message(message, sentBy))
                messageAdapter?.notifyDataSetChanged()
                messageRecycleView.smoothScrollToPosition(messageAdapter!!.getItemCount());
            }
        })

    }
    fun addResponse(response: String?) {

        try {
            if (response != null) {
                addToChat(response, "bot")
            };
        }
        catch (e: Exception){
            Log.d("dax", e.message.toString())
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
            addResponse("Failure to load response due to "+e.message.toString())
        }


        val body = RequestBody.create(JSON, jsonBody.toString())
        val request: Request = Request.Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer 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")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failure to load response due to "+e.message.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){

                    try {
                            var jsonObject = JSONObject(response.body?.string())
                            val jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getString("text")
                            Log.d("dax", result.toString())
                            addResponse(result.toString().trim())

                    } catch (e: JSONException) {
                        Log.d("dax", e.message.toString())
                    }
                }
                else{
                    addResponse("Failure to load response, try again!")
                }
            }

        })
    }
    class MessageAdapter(var messageList: List<Message>, var context: Context) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_items, parent, false)
            return MyViewHolder(view)
        }
        fun copyToClipboard(text: CharSequence){
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label",text)
            clipboard.setPrimaryClip(clip)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val message = messageList[position]
            val bottom_to_top = AnimationUtils.loadAnimation(context, R.anim.bottom_to_top)
            val top_to_bottom = AnimationUtils.loadAnimation(context, R.anim.top_to_bottom)
            bottom_to_top.duration = 400
            top_to_bottom.duration = 400
            val handler = Handler(Looper.getMainLooper())

            if (message.getsentby() == message.SENT_BY_ME) {
                holder.leftChatView.visibility = View.GONE
                holder.rightChatView.visibility = View.VISIBLE
                holder.rightTextView.text = message.getmessage()

            } else {
                holder.rightChatView.visibility = View.GONE
                holder.leftChatView.visibility = View.VISIBLE
                holder.leftTextView.text = message.getmessage()

            }

            holder.leftChatView.setOnLongClickListener(){
//            Toast.makeText(holder.leftChatView.context, messageList.get(position).getmessage().toString(), Toast.LENGTH_SHORT).show()

                holder.popupCard.visibility = View.VISIBLE
                holder.popupCard.startAnimation(bottom_to_top)
                true
            }
            holder.copyCard.setOnClickListener{
                copyToClipboard(messageList.get(position).getmessage().toString())
                holder.popupCard.startAnimation(top_to_bottom)
                handler.postDelayed({
                    holder.popupCard.visibility = View.GONE
                },400)
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            holder.closeCard.setOnClickListener{
                holder.popupCard.startAnimation(top_to_bottom)
                handler.postDelayed({
                    holder.popupCard.visibility = View.GONE
                },400)
            }
            holder.readingMode.setOnClickListener{
                val intent = Intent(context.applicationContext, ReadingMode::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("text", messageList.get(position).getmessage().toString())
                context.applicationContext.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return messageList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var leftChatView: LinearLayout
            var rightChatView: LinearLayout
            var leftTextView: TextView
            var rightTextView: TextView
            var popupCard: CardView
            var copyCard: CardView
            var readingMode: CardView
            var closeCard: CardView
            init {
                leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat)
                rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat)
                leftTextView = itemView.findViewById<TextView>(R.id.left_chat_text)
                rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text)
                popupCard = itemView.findViewById<CardView>(R.id.popupCard)
                copyCard = itemView.findViewById<CardView>(R.id.copyCard)
                readingMode = itemView.findViewById<CardView>(R.id.readingMode)
                closeCard = itemView.findViewById<CardView>(R.id.closeCard)

            }
        }
    }

}
