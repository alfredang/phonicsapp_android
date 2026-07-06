package com.tertiaryinfotech.phonics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tertiaryinfotech.phonics.data.ContentLibrary
import com.tertiaryinfotech.phonics.data.Emotion
import com.tertiaryinfotech.phonics.data.PracticeKind
import com.tertiaryinfotech.phonics.speech.Speaker

/** In-tab destinations of the Practice tab. */
sealed interface PracticeDestination {
    data object Home : PracticeDestination
    data object Emotion : PracticeDestination
    data class Kind(val kind: PracticeKind) : PracticeDestination
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeHomeScreen(speaker: Speaker, onOpen: (PracticeDestination) -> Unit) {
    Scaffold(
        containerColor = Brand.background,
        topBar = {
            TopAppBar(
                title = { Text("Practice", fontWeight = FontWeight.SemiBold) },
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
                    "Practice out loud",
                    "Listen first, then read each line aloud and compare. " +
                        "A few minutes a day builds clear, confident speech.",
                )
            }
            item {
                // Intonation & emotion gets pride of place.
                NavTile(
                    title = "Intonation & Emotion",
                    subtitle = "Hear how one sentence changes with feeling — happy, sad, angry, a question, and more.",
                    icon = Icons.Filled.TheaterComedy,
                    tint = Brand.coral,
                ) { onOpen(PracticeDestination.Emotion) }
            }
            items(PracticeKind.entries) { kind ->
                NavTile(
                    title = kind.title,
                    subtitle = kind.blurb,
                    icon = kind.icon,
                    tint = Brand.secondary,
                ) { onOpen(PracticeDestination.Kind(kind)) }
            }
        }
    }
}

// region Line-based drills (tongue twisters, short messages, sentences)

@Composable
fun PracticeLinesScreen(kind: PracticeKind, speaker: Speaker, onBack: () -> Unit) {
    SubScreen(kind.title, speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { Text(kind.blurb, color = Brand.mutedInk, fontSize = 14.sp) }
            items(ContentLibrary.lines(kind)) { line ->
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(line.text, color = Brand.ink, fontSize = 18.sp,
                            fontWeight = FontWeight.Medium, lineHeight = 24.sp)
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Pill(line.focus, Brand.secondary.copy(alpha = 0.12f), Brand.secondary)
                            Spacer(Modifier.weight(1f))
                            SpeakButton(isActive = speaker.nowPlaying == line.text, tint = Brand.secondary) {
                                speaker.speak(line.text, rate = speaker.speechRate)
                            }
                        }
                    }
                }
            }
        }
    }
}

// endregion

// region Minimal pairs

@Composable
fun MinimalPairsScreen(speaker: Speaker, onBack: () -> Unit) {
    SubScreen("Minimal Pairs", speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(PracticeKind.MINIMAL_PAIRS.blurb, color = Brand.mutedInk, fontSize = 14.sp)
            }
            items(ContentLibrary.minimalPairs) { pair ->
                AppCard {
                    Column(
                        Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            WordTile(pair.first, speaker, Modifier.weight(1f))
                            Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = Brand.mutedInk)
                            WordTile(pair.second, speaker, Modifier.weight(1f))
                        }
                        Text(pair.contrast, color = Brand.mutedInk, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun WordTile(word: String, speaker: Speaker, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brand.chip)
            .clickable { speaker.speakWord(word) }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(word, color = Brand.ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.width(8.dp))
        Icon(
            if (speaker.nowPlaying == word) Icons.Filled.VolumeUp else Icons.Filled.PlayArrow,
            contentDescription = "Play $word",
            tint = Brand.secondary,
            modifier = Modifier.size(20.dp),
        )
    }
}

// endregion

// region Intonation & Emotion

/**
 * Demonstrates how intonation conveys emotion: the learner picks a sentence,
 * then hears it spoken through each emotion preset (pitch + rate + volume shaped).
 */
@Composable
fun EmotionPracticeScreen(speaker: Speaker, onBack: () -> Unit) {
    var sentenceIndex by rememberSaveable { mutableIntStateOf(0) }
    val sentence = ContentLibrary.emotionScripts[sentenceIndex]

    SubScreen("Intonation & Emotion", speaker, onBack) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                ScreenHeader(
                    "Say it with feeling",
                    "The words stay the same — the meaning changes with your pitch, pace, and stress. " +
                        "Pick a line, then tap each emotion to hear the difference.",
                )
            }
            item {
                // Sentence card with a cycle control
                AppCard {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("“$sentence”", color = Brand.ink, fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold, lineHeight = 27.sp)
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = {
                                sentenceIndex = (sentenceIndex + 1) % ContentLibrary.emotionScripts.size
                            }) {
                                Icon(Icons.Filled.Repeat, contentDescription = null, tint = Brand.coral)
                                Text("  Next line", color = Brand.coral, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(Modifier.weight(1f))
                            TextButton(onClick = { speaker.speak(sentence, emotion = Emotion.all.first()) }) {
                                Icon(Icons.Filled.PlayCircleOutline, contentDescription = null, tint = Brand.mutedInk)
                                Text("  Neutral", color = Brand.mutedInk, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
            item { SectionHeader("Emotions", Icons.Filled.TheaterComedy, Brand.coral) }
            items(Emotion.all.chunked(2)) { rowEmotions ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowEmotions.forEach { emotion ->
                        EmotionCard(emotion, Modifier.weight(1f)) {
                            speaker.speak(sentence, emotion = emotion)
                        }
                    }
                    if (rowEmotions.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EmotionCard(emotion: Emotion, modifier: Modifier = Modifier, onClick: () -> Unit) {
    AppCard(modifier = modifier, onClick = onClick) {
        Column(
            Modifier.fillMaxWidth().heightIn(min = 130.dp).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(emotion.icon, contentDescription = null, tint = Brand.coral)
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(Brand.coral),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Play",
                        tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Text(emotion.name, color = Brand.ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(emotion.cue, color = Brand.mutedInk, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}

// endregion
