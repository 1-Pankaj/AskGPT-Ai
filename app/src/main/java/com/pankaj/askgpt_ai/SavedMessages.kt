package com.pankaj.askgpt_ai

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SavedMessages : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_messages)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        val dbRef = FirebaseDatabase.getInstance().reference
        val mAuth = FirebaseAuth.getInstance()

        val closeSavedMessageCard = findViewById<MaterialCardView>(R.id.closeSavedMessagesCard)

        closeSavedMessageCard.setOnClickListener{
            finish()
        }

        var messageList :List<SavedMessages.SavedMessage>
        messageList = ArrayList<SavedMessages.SavedMessage>()
        val messageAdapter = SavedMessagesAdapter(messageList, applicationContext)
        val messageRecycleView = findViewById<RecyclerView>(R.id.recyclerViewSavedMessages)
        messageRecycleView.adapter = messageAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        messageRecycleView.layoutManager = linearLayoutManager

        val bottomSheetSaved = findViewById<FrameLayout>(R.id.bottomSheetSaved)

        BottomSheetBehavior.from(bottomSheetSaved).apply {
            peekHeight = 500
            this.state = BottomSheetBehavior.STATE_EXPANDED
        }

        if(resources.getString(R.string.mode) == "Day"){
            bottomSheetSaved.setBackgroundResource(R.drawable.settings_bottomsheet)
        }else{
            bottomSheetSaved.setBackgroundResource(R.drawable.settingsbottomsheet_night)
        }

        dbRef.child("Users").child(mAuth.currentUser?.uid.toString()).child("messages")
            .addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val ds: DataSnapshot? = null
                for (ds in snapshot.children){
                    var title = ds.key.toString()
                    var message = ds.value.toString()
                    (messageList as ArrayList<SavedMessages.SavedMessage>).add(SavedMessages.SavedMessage(title,message))
                    messageAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    class SavedMessagesAdapter(var messageList: List<SavedMessage>, var context: Context) : RecyclerView.Adapter<SavedMessagesAdapter.MyViewHolder>(){

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.saved_messages, parent, false)
            return MyViewHolder(view)
        }



        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            var lastPosition = position
            val message = messageList[position]
            holder.titleText.text = message.gettitle().toString()
            holder.messageText.text = message.getmessage().toString()



            try {
                holder.savedMessageCard.setOnClickListener {
                    val intent = Intent(context.applicationContext, ReadingMode::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mode", "text")
                    intent.putExtra("text", messageList.get(position).messageText.toString())

                    context.applicationContext.startActivity(intent)
                }
            }
            catch (e: Exception){
                Log.d("dax", e.message.toString())
            }

        }

        override fun getItemCount(): Int {
            return messageList.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

            var savedMessageCard: CardView
            var titleText: TextView
            var messageText: TextView
            var divider: ImageView

            init {
                savedMessageCard = itemView.findViewById(R.id.savedMessageCard)
                titleText = itemView.findViewById(R.id.titleText)
                messageText = itemView.findViewById(R.id.messageText)
                divider = itemView.findViewById(R.id.divider)
            }
        }

    }

    class SavedMessage{
        var titleText: String? = null
        var messageText: String? = null

        fun gettitle(): String? {
            return titleText
        }

        fun settitle(titleText: String) {
            this.titleText = titleText
        }

        fun getmessage(): String? {
            return messageText
        }

        fun setmessage(messageText: String) {
            this.messageText = messageText
        }

        constructor(title: String?, message: String?){
            this.titleText = title
            this.messageText = message
        }

    }
}