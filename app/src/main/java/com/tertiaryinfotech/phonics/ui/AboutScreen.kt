package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.phonics.BuildConfig

private const val DEVELOPER_URL = "https://www.tertiaryinfotech.com"
private const val REFERENCE_URL = "https://en.wikipedia.org/wiki/Phonics"
private const val AUDIO_SOURCE_URL = "https://en.wiktionary.org"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val uri = androidx.compose.ui.platform.LocalUriHandler.current
    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text("About", fontWeight = FontWeight.SemiBold) },
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
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // App card
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brand.primary),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Filled.GraphicEq, contentDescription = null, tint = Color.White)
                        }
                        Text("Phonics", color = Brand.ink, fontSize = 20.sp,
                            fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp))
                    }
                    Text(
                        "A pronunciation and phonics trainer for adults. Learn every English sound, " +
                            "master the irregular spelling rules, and practise intonation that expresses " +
                            "real emotion — all with British or American voice-over.",
                        color = Brand.mutedInk, fontSize = 14.sp, lineHeight = 19.sp,
                    )
                }
            }

            // Developer card
            Text("DEVELOPER", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Business, contentDescription = null,
                            tint = Brand.ink, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Tertiary Infotech Academy Pte. Ltd.", color = Brand.ink,
                            fontWeight = FontWeight.SemiBold)
                    }
                    Row(
                        Modifier.fillMaxWidth().clickable { uri.openUri(DEVELOPER_URL) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Filled.Language, contentDescription = null,
                            tint = Brand.secondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("tertiaryinfotech.com", color = Brand.secondary,
                            textDecoration = TextDecoration.Underline)
                    }
                }
            }

            // Reference / learning source card
            Text("LEARNING REFERENCE", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Curriculum structure follows standard synthetic-phonics practice.",
                        color = Brand.mutedInk, fontSize = 13.sp)
                    Row(
                        Modifier.fillMaxWidth().clickable { uri.openUri(REFERENCE_URL) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null,
                            tint = Brand.secondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Phonics — Wikipedia", color = Brand.secondary,
                            textDecoration = TextDecoration.Underline)
                    }
                }
            }

            // Voice-over / audio attribution (CC BY-SA)
            Text("VOICE-OVER", color = Brand.mutedInk, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            AppCard {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Word pronunciations are real human recordings from Wiktionary / Wikimedia " +
                            "Commons (CC BY-SA), bundled for offline use. Other speech uses Android's " +
                            "on-device en-GB / en-US voices. No account, no tracking.",
                        color = Brand.mutedInk, fontSize = 13.sp, lineHeight = 18.sp,
                    )
                    Row(
                        Modifier.fillMaxWidth().clickable { uri.openUri(AUDIO_SOURCE_URL) },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null,
                            tint = Brand.secondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Wiktionary pronunciation audio", color = Brand.secondary,
                            fontSize = 14.sp, textDecoration = TextDecoration.Underline)
                    }
                }
            }

            // Version
            AppCard {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("Version", color = Brand.ink, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    Text("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                        color = Brand.mutedInk)
                }
            }
        }
    }
}
