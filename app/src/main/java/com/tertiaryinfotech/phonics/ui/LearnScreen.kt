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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tertiaryinfotech.phonics.data.Phoneme
import com.tertiaryinfotech.phonics.data.PhonicsCategory
import com.tertiaryinfotech.phonics.speech.Speaker

/** In-tab destinations of the Learn tab. */
sealed interface LearnDestination {
    data object Home : LearnDestination
    data class Category(val category: PhonicsCategory) : LearnDestination
    data class Detail(val phoneme: Phoneme) : LearnDestination
    data object Settings : LearnDestination
}

/** Shared sub-screen scaffold: back arrow + centered title + accent menu. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubScreen(
    title: String,
    speaker: Speaker?,
    onBack: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = { if (speaker != null) AccentMenu(speaker) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Brand.background),
            )
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnHomeScreen(speaker: Speaker, onOpen: (LearnDestination) -> Unit) {
    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text("Learn", fontWeight = FontWeight.SemiBold) },
                navigationIcon = { AccentMenu(speaker) },
                actions = {
                    IconButton(onClick = { onOpen(LearnDestination.Settings) }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Brand.primary)
                    }
                },
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
                    "Sound it out",
                    "Tap any sound to hear it in your chosen ${speaker.accent.label} accent. " +
                        "Work through each group at your own pace.",
                )
            }
            items(PhonicsCategory.entries) { category ->
                NavTile(
                    title = category.title,
                    subtitle = category.blurb,
                    icon = category.icon,
                    tint = Brand.accent(category),
                ) { onOpen(LearnDestination.Category(category)) }
            }
        }
    }
}

@Composable
fun PhonemeListScreen(
    category: PhonicsCategory,
    speaker: Speaker,
    onOpen: (Phoneme) -> Unit,
    onBack: () -> Unit,
) {
    SubScreen(category.title, speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(category.blurb, color = Brand.mutedInk, fontSize = 14.sp)
            }
            items(ContentLibrary.phonemes(category)) { phoneme ->
                PhonemeRow(phoneme, speaker) { onOpen(phoneme) }
            }
        }
    }
}

@Composable
private fun PhonemeRow(phoneme: Phoneme, speaker: Speaker, onOpen: () -> Unit) {
    val tint = Brand.accent(phoneme.category)
    AppCard(onClick = onOpen) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(phoneme.grapheme, color = tint, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column(
                Modifier.weight(1f).padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(phoneme.ipa, color = Brand.ink, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    phoneme.exampleWords.joinToString(" · "),
                    color = Brand.mutedInk, fontSize = 13.sp, maxLines = 1,
                )
            }
            SpeakButton(isActive = speaker.nowPlaying == phoneme.soundKey, tint = tint) {
                speaker.speakPhoneme(phoneme)
            }
        }
    }
}

@Composable
fun PhonemeDetailScreen(phoneme: Phoneme, speaker: Speaker, onBack: () -> Unit) {
    val tint = Brand.accent(phoneme.category)
    SubScreen(phoneme.grapheme, speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                // Hero
                AppCard {
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(phoneme.grapheme, color = tint, fontSize = 64.sp, fontWeight = FontWeight.Bold)
                        Text(phoneme.ipa, color = Brand.mutedInk, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Pill(phoneme.category.title, tint.copy(alpha = 0.14f), tint)
                        androidx.compose.material3.Button(
                            onClick = { speaker.speakPhoneme(phoneme) },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = tint),
                        ) {
                            Icon(Icons.Filled.Campaign, contentDescription = null)
                            Text("  Hear the sound", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            item {
                // How to say it
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionHeader("How to say it", Icons.Filled.RecordVoiceOver, tint)
                        Text(phoneme.soundDescription, color = Brand.ink, fontSize = 15.sp, lineHeight = 21.sp)
                    }
                }
            }
            item {
                // Example words
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeader("Example words", Icons.Filled.TextFormat, tint)
                        phoneme.exampleWords.forEachIndexed { i, word ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text(word, color = Brand.ink, fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                SpeakButton(isActive = speaker.nowPlaying == word, tint = tint) {
                                    speaker.speakWord(word)
                                }
                            }
                            if (i != phoneme.exampleWords.lastIndex) Divider(color = Color(0x14000000))
                        }
                    }
                }
            }
        }
    }
}
