package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.phonics.data.ContentLibrary
import com.tertiaryinfotech.phonics.data.ExceptionRule
import com.tertiaryinfotech.phonics.speech.Speaker

/** In-tab destinations of the Rules tab. */
sealed interface RulesDestination {
    data object Home : RulesDestination
    data class Detail(val rule: ExceptionRule) : RulesDestination
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesHomeScreen(speaker: Speaker, onOpen: (ExceptionRule) -> Unit) {
    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text("Rules", fontWeight = FontWeight.SemiBold) },
                actions = { AccentMenu(speaker) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Brand.background),
            )
        },
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                ScreenHeader(
                    "When phonics breaks",
                    "English is full of exceptions. These rules cover the irregular spellings that " +
                        "don't sound out the way you'd expect — hear each one to lock it in.",
                )
            }
            items(ContentLibrary.exceptionRules) { rule ->
                NavTile(
                    title = rule.title,
                    subtitle = rule.summary,
                    icon = rule.icon,
                    tint = Brand.highlight,
                ) { onOpen(rule) }
            }
        }
    }
}

@Composable
fun RuleDetailScreen(rule: ExceptionRule, speaker: Speaker, onBack: () -> Unit) {
    SubScreen(rule.title, speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brand.highlight),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(rule.icon, contentDescription = null, tint = Color.White)
                        }
                        Text(rule.explanation, color = Brand.ink, fontSize = 15.sp, lineHeight = 21.sp)
                    }
                }
            }
            item {
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeader("Examples", Icons.AutoMirrored.Filled.List, Brand.highlight)
                        rule.examples.forEachIndexed { i, ex ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(ex.word, color = Brand.ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                    Text(ex.note, color = Brand.mutedInk, fontSize = 13.sp, lineHeight = 17.sp)
                                }
                                SpeakButton(isActive = speaker.nowPlaying == ex.word, tint = Brand.highlight) {
                                    speaker.speakWord(ex.word)
                                }
                            }
                            if (i != rule.examples.lastIndex) Divider(color = Color(0x14000000))
                        }
                    }
                }
            }
        }
    }
}
