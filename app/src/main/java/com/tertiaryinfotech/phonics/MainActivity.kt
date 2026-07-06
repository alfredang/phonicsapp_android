package com.tertiaryinfotech.phonics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tertiaryinfotech.phonics.speech.Speaker
import com.tertiaryinfotech.phonics.ui.PhonicsTheme
import com.tertiaryinfotech.phonics.ui.RootScreen

class MainActivity : ComponentActivity() {

    private lateinit var speaker: Speaker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        speaker = Speaker(this)
        setContent {
            PhonicsTheme {
                RootScreen(speaker = speaker)
            }
        }
    }

    override fun onDestroy() {
        speaker.shutdown()
        super.onDestroy()
    }
}
