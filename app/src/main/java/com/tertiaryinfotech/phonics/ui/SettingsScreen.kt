package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.phonics.speech.Accent
import com.tertiaryinfotech.phonics.speech.Speaker
import com.tertiaryinfotech.phonics.speech.VoiceGender

@Composable
fun SettingsScreen(speaker: Speaker, onBack: () -> Unit) {
    SubScreen("Settings", speaker = null, onBack = onBack) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Accent
            Text("ACCENT", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Accent.entries.forEachIndexed { i, accent ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    speaker.chooseAccent(accent)
                                    speaker.speak("Hello, this is the ${accent.label} voice.")
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(accent.flag, fontSize = 22.sp)
                            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(accent.label, color = Brand.ink, fontWeight = FontWeight.Medium)
                                Text(accent.subtitle, color = Brand.mutedInk, fontSize = 12.sp)
                            }
                            if (speaker.accent == accent) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = "Selected",
                                    tint = Brand.primary)
                            }
                        }
                        if (i == 0) Divider(color = Color(0x14000000))
                    }
                }
            }

            // Voice gender
            Text("VOICE", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    VoiceGender.entries.forEachIndexed { i, gender ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    speaker.chooseGender(gender)
                                    speaker.speak("Hello, this is the ${gender.label.lowercase()} voice.")
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                if (gender == VoiceGender.FEMALE) Icons.Filled.AccountCircle else Icons.Filled.Person,
                                contentDescription = null,
                                tint = Brand.secondary,
                                modifier = Modifier.size(24.dp),
                            )
                            Text(gender.label, color = Brand.ink, fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f).padding(start = 12.dp))
                            if (speaker.gender == gender) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = "Selected",
                                    tint = Brand.primary)
                            }
                        }
                        if (i == 0) Divider(color = Color(0x14000000))
                    }
                    Text(
                        "Uses a ${speaker.gender.label.lowercase()} ${speaker.accent.subtitle} voice " +
                            "when your device has one installed.",
                        color = Brand.mutedInk, fontSize = 12.sp, lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }
            }

            // Speaking speed
            Text("SPEAKING SPEED", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("🐢", fontSize = 18.sp)
                        Slider(
                            value = speaker.speechRate,
                            onValueChange = { speaker.chooseSpeechRate(it) },
                            valueRange = 0.6f..1.25f,
                            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                        )
                        Text("🐇", fontSize = 18.sp)
                    }
                    TextButton(onClick = {
                        speaker.speak("The quick brown fox jumps over the lazy dog.",
                            rate = speaker.speechRate)
                    }) {
                        Text("Test voice", color = Brand.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            AppCard {
                Text(
                    "Voice-over uses your device's built-in ${speaker.accent.subtitle} voice. " +
                        "For the highest quality, install or update the Speech Recognition & Synthesis " +
                        "engine in Android Settings › Accessibility › Text-to-speech output.",
                    color = Brand.mutedInk, fontSize = 13.sp, lineHeight = 18.sp,
                    modifier = Modifier.padding(16.dp),
                )
            }
            Spacer(Modifier.width(1.dp))
        }
    }
}
