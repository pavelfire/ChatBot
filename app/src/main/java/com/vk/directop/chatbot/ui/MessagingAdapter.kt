package com.vk.directop.chatbot.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vk.directop.chatbot.R
import com.vk.directop.chatbot.data.Message
import com.vk.directop.chatbot.databinding.MessageItemBinding
import com.vk.directop.chatbot.utils.Constants.RECEIVE_ID
import com.vk.directop.chatbot.utils.Constants.SEND_ID

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Message>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val currentMessage = messagesList[position]
        val binding: MessageItemBinding = MessageItemBinding.bind(holder.itemView)

        when (currentMessage.id) {
            SEND_ID -> {
                binding.tvMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                binding.tvBotMessage.visibility = View.GONE
            }
            RECEIVE_ID -> {
                binding.tvBotMessage.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                binding.tvMessage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    fun insertMessage(message: Message){
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
        //notifyDataSetChanged()
    }
}