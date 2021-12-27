package com.example.mcoroutinesapp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {


    lateinit var displayAdvice: TextView
    lateinit var getAdvice: Button
    val Advurl = " https://api.adviceslip.com/advice"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.active_main)
        displayAdvice = findViewById(R.id.tv)
        getAdvice = findViewById(R.id.btn)

        getAdvice.setOnClickListener {
            requestAdvice()
        }
    }


    private fun requestAdvice() {
        CoroutineScope(Dispatchers.IO).launch {
            val someAdvice = async {
                randomAdvice()
            }.await()
            if (someAdvice != null) {
                updateAdvice(someAdvice)
            }
        }
    }

    private fun randomAdvice(): String {
        var resps = ""
        try {
            resps = URL(Advurl).readText(Charsets.UTF_8)
        } catch (e: Exception) {
            println("Error is $e")
        }
        return resps
    }

    private suspend fun updateAdvice(data: String) {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val slip = jsonObject.getJSONObject("slip")
            val id = slip.getInt("id")
            val advice = slip.getString("advice")

            displayAdvice.text = advice

        }

    }


}