package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val FEEDBACK_WHATSAPP = "6588666375"        // +65 8866 6375

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen() {
    val uri = LocalUriHandler.current
    var title by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text("Feedback", fontWeight = FontWeight.SemiBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Brand.background),
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("We'd love your feedback", color = Brand.ink,
                fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                "Found a word that sounds off? Have an idea for a new lesson? " +
                    "Send us a note and it'll open in WhatsApp.",
                color = Brand.mutedInk, fontSize = 14.sp, lineHeight = 19.sp,
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                placeholder = { Text("Short summary") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                placeholder = { Text("Your message…") },
                minLines = 6,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = {
                    val body = buildString {
                        if (title.isNotBlank()) append("*").append(title.trim()).append("*\n")
                        append(message.trim())
                    }
                    val text = java.net.URLEncoder.encode(body, "UTF-8")
                    uri.openUri("https://wa.me/$FEEDBACK_WHATSAPP?text=$text")
                },
                enabled = title.isNotBlank() || message.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Brand.primary),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Send via WhatsApp", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
