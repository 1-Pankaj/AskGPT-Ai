package com.pankaj.askgpt_ai

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView


class MessageAdapter(var messageList: List<Message>) : RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_items, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messageList[position]
        if (message.getsentby() == message.SENT_BY_ME) {
            holder.leftChatView.visibility = View.GONE
            holder.rightChatView.visibility = View.VISIBLE
            holder.rightTextView.text = message.getmessage()
        } else {
            holder.rightChatView.visibility = View.GONE
            holder.leftChatView.visibility = View.VISIBLE
            holder.leftTextView.text = message.getmessage()
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

        init {
            leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat)
            rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat)
            leftTextView = itemView.findViewById<TextView>(R.id.left_chat_text)
            rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text)
        }
    }
}