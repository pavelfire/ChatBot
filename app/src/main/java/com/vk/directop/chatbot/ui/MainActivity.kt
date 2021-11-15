package com.vk.directop.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vk.directop.chatbot.R
import com.vk.directop.chatbot.data.Message
import com.vk.directop.chatbot.databinding.ActivityMainBinding
import com.vk.directop.chatbot.utils.BotResponse
import com.vk.directop.chatbot.utils.Constants.OPEN_GOOGLE
import com.vk.directop.chatbot.utils.Constants.OPEN_SEARCH
import com.vk.directop.chatbot.utils.Constants.RECEIVE_ID
import com.vk.directop.chatbot.utils.Constants.SEND_ID
import com.vk.directop.chatbot.utils.Time
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Ivan", "Galina", "Tanya", "Kolyan", "Luigi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // init binding
        setContentView(binding.root)

        recyclerView()

        clickEvents()

        val random = (0..3).random()
        customMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")

    }

    private fun clickEvents(){
        binding.btnSend.setOnClickListener {
            sendMessage()
        }
        binding.etMessage.setOnClickListener {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            binding.etMessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val response = BotResponse.basicResponses(message)

                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val serchTerm: String? = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$serchTerm")
                        startActivity(site)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }


        }
    }


    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}